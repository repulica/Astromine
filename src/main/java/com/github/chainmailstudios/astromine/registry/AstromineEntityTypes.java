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
package com.github.chainmailstudios.astromine.registry;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.entity.RocketEntity;
import com.github.chainmailstudios.astromine.common.entity.SpaceSlimeEntity;
import com.github.chainmailstudios.astromine.common.entity.SuperSpaceSlimeEntity;
import com.github.chainmailstudios.astromine.common.entity.projectile.BulletEntity;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;

public class AstromineEntityTypes {

	public static final EntityType<BulletEntity> BULLET_ENTITY_TYPE = register("bullet", new EntityType<>(BulletEntity::new, SpawnGroup.MISC, false, true, true, true, ImmutableSet.<Block>builder().build(), EntityDimensions.fixed(0.1875f, 0.125f), 4, 20));

	public static final EntityType<SpaceSlimeEntity> SPACE_SLIME = register("space_slime", FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, SpaceSlimeEntity::new).dimensions(EntityDimensions.changing(2.04F, 2.04F)).trackable(128, 4).build());

	public static final EntityType<SuperSpaceSlimeEntity> SUPER_SPACE_SLIME = register(
			"super_space_slime", FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, SuperSpaceSlimeEntity::new)
					.dimensions(EntityDimensions.changing(6.125F, 6.125F))
					.trackable(128, 4)
					.build());

	public static final EntityType<RocketEntity> ROCKET = register(
			"rocket", FabricEntityTypeBuilder.create(SpawnGroup.MISC, RocketEntity::new)
					.dimensions(EntityDimensions.changing(1.5f, 20f))
					.trackable(256, 4)
					.build());

	public static void initialize() {
		FabricDefaultAttributeRegistry.register(SPACE_SLIME, HostileEntity.createHostileAttributes());
		FabricDefaultAttributeRegistry.register(SUPER_SPACE_SLIME, SuperSpaceSlimeEntity.createAttributes());

		AttackEntityCallback.EVENT.register((playerEntity, world, hand, entity, entityHitResult) -> {
			if (entity instanceof SuperSpaceSlimeEntity) {
				if (world.random.nextInt(10) == 0) {
					SpaceSlimeEntity spaceSlimeEntity = AstromineEntityTypes.SPACE_SLIME.create(world);
					spaceSlimeEntity.setPos(entity.getX(), entity.getY(), entity.getZ());
					world.spawnEntity(spaceSlimeEntity);
				}
			}

			return ActionResult.PASS;
		});
	}

	private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
		return register(AstromineCommon.identifier(id), builder);
	}

	private static <T extends Entity> EntityType<T> register(Identifier id, EntityType.Builder<T> builder) {
		return Registry.register(Registry.ENTITY_TYPE, id, builder.build(id.getPath()));
	}

	/**
	* @param id   Name of EntityType instance to be registered
	* @param type EntityType instance to register
	* @return Registered EntityType
	*/
	private static <T extends Entity> EntityType<T> register(String id, EntityType<T> type) {
		return register(AstromineCommon.identifier(id), type);
	}

	private static <T extends Entity> EntityType<T> register(Identifier id, EntityType<T> type) {
		return Registry.register(Registry.ENTITY_TYPE, id, type);
	}

	static {
		SpawnRestriction.register(AstromineEntityTypes.SPACE_SLIME, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpaceSlimeEntity::canSpawnInDark);

		FabricDefaultAttributeRegistry.register(SPACE_SLIME, SpaceSlimeEntity.createMobAttributes());
		FabricDefaultAttributeRegistry.register(SUPER_SPACE_SLIME, SuperSpaceSlimeEntity.createMobAttributes());
	}
}
