/*
 * MIT License
 * 
 * Copyright (c) 2020 Chainmail Studios
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.chainmailstudios.astromine.common.item;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Objects;

/**
* Provides an alternative to {@link net.minecraft.item.SpawnEggItem} which doesn't force hue/color through {@link net.minecraft.client.color.item.ItemColors}.
*/
public class UncoloredSpawnEggItem extends Item {
	private static final Map<EntityType<?>, UncoloredSpawnEggItem> SPAWN_EGGS = Maps.newIdentityHashMap();
	private final EntityType<?> type;

	public UncoloredSpawnEggItem(EntityType<?> type, Item.Settings settings) {
		super(settings);
		this.type = type;
		SPAWN_EGGS.put(type, this);
	}

	public static Iterable<UncoloredSpawnEggItem> getAll() {
		return Iterables.unmodifiableIterable(SPAWN_EGGS.values());
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();

		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			ItemStack itemStack = context.getStack();
			BlockPos blockPos = context.getBlockPos();
			Direction direction = context.getSide();
			BlockState blockState = world.getBlockState(blockPos);

			if (blockState.isOf(Blocks.SPAWNER)) {
				BlockEntity blockEntity = world.getBlockEntity(blockPos);

				if (blockEntity instanceof MobSpawnerBlockEntity) {
					MobSpawnerLogic mobSpawnerLogic = ((MobSpawnerBlockEntity) blockEntity).getLogic();
					EntityType<?> entityType = this.getEntityType(itemStack.getTag());
					mobSpawnerLogic.setEntityId(entityType);
					blockEntity.markDirty();
					world.updateListeners(blockPos, blockState, blockState, 3);
					itemStack.decrement(1);
					return ActionResult.CONSUME;
				}
			}

			BlockPos blockPos3;
			if (blockState.getCollisionShape(world, blockPos).isEmpty()) {
				blockPos3 = blockPos;
			} else {
				blockPos3 = blockPos.offset(direction);
			}

			EntityType<?> entityType2 = this.getEntityType(itemStack.getTag());
			if (entityType2.spawnFromItemStack(world, itemStack, context.getPlayer(), blockPos3, SpawnReason.SPAWN_EGG, true, !Objects.equals(blockPos, blockPos3) && direction == Direction.UP) != null) {
				itemStack.decrement(1);
			}

			return ActionResult.CONSUME;
		}
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		BlockHitResult hitResult = rayTrace(world, user, RayTraceContext.FluidHandling.SOURCE_ONLY);

		if (hitResult.getType() != HitResult.Type.BLOCK) {
			return TypedActionResult.pass(itemStack);
		} else if (world.isClient) {
			return TypedActionResult.success(itemStack);
		} else {
			BlockHitResult blockHitResult = hitResult;
			BlockPos blockPos = blockHitResult.getBlockPos();

			if (!(world.getBlockState(blockPos).getBlock() instanceof FluidBlock)) {
				return TypedActionResult.pass(itemStack);
			} else if (world.canPlayerModifyAt(user, blockPos) && user.canPlaceOn(blockPos, blockHitResult.getSide(), itemStack)) {
				EntityType<?> entityType = this.getEntityType(itemStack.getTag());

				if (entityType.spawnFromItemStack(world, itemStack, user, blockPos, SpawnReason.SPAWN_EGG, false, false) == null) {
					return TypedActionResult.pass(itemStack);
				} else {
					if (!user.abilities.creativeMode) {
						itemStack.decrement(1);
					}

					user.incrementStat(Stats.USED.getOrCreateStat(this));
					return TypedActionResult.consume(itemStack);
				}
			} else {
				return TypedActionResult.fail(itemStack);
			}
		}
	}

	/**
	* Returns the registered {@link EntityType} associated with the given spawn egg tag.
	*
	* @param tag tag to check
	* @return {@link EntityType} of tag, or this item's type field
	*/
	public EntityType<?> getEntityType(CompoundTag tag) {
		if (tag != null && tag.contains("EntityTag", 10)) {
			CompoundTag entityTag = tag.getCompound("EntityTag");

			if (entityTag.contains("id", 8)) {
				return EntityType.get(entityTag.getString("id")).orElse(this.type);
			}
		}

		return this.type;
	}
}