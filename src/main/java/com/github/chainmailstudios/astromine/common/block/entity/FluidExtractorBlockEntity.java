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
package com.github.chainmailstudios.astromine.common.block.entity;

import com.github.chainmailstudios.astromine.common.block.base.DefaultedBlockWithEntity;
import com.github.chainmailstudios.astromine.common.block.entity.base.DefaultedEnergyFluidBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.SimpleFluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkMemberType;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineBlockEntityTypes;
import com.github.chainmailstudios.astromine.registry.AstromineConfig;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

public class FluidExtractorBlockEntity extends DefaultedEnergyFluidBlockEntity implements NetworkMember, Tickable {
	private final Fraction cooldown = Fraction.empty();

	public boolean isActive = false;

	public boolean[] activity = {false, false, false, false, false};

	public FluidExtractorBlockEntity() {
		super(AstromineBlockEntityTypes.FLUID_EXTRACTOR);

		fluidComponent.getVolume(0).setSize(Fraction.ofWhole(4));
	}

	@Override
	protected double getEnergySize() {
		return AstromineConfig.get().fluidExtractorEnergy;
	}

	@Override
	protected FluidInventoryComponent createFluidComponent() {
		return new SimpleFluidInventoryComponent(1);
	}

	@Override
	public void tick() {
		super.tick();

		start:
		if (this.world != null && !this.world.isClient()) {
			if (asEnergy().getEnergy() < AstromineConfig.get().fluidExtractorEnergyConsumed) {
				cooldown.resetToEmpty();
				isActive = false;
				break start;
			}

			isActive = true;

			cooldown.add(Fraction.of(1, AstromineConfig.get().fluidExtractorTimeConsumed));
			cooldown.simplify();
			if (cooldown.isBiggerOrEqualThan(Fraction.ofWhole(1))) {
				cooldown.resetToEmpty();

				FluidVolume fluidVolume = fluidComponent.getVolume(0);

				Direction direction = getCachedState().get(HorizontalFacingBlock.FACING);
				BlockPos targetPos = pos.offset(direction);
				FluidState targetFluidState = world.getFluidState(targetPos);

				if (targetFluidState.isStill()) {
					FluidVolume toInsert = new FluidVolume(targetFluidState.getFluid(), Fraction.bucket());
					if (fluidVolume.hasAvailable(Fraction.bucket())) {
						fluidVolume.pullVolume(toInsert, toInsert.getFraction());
						asEnergy().extract(AstromineConfig.get().fluidExtractorEnergyConsumed);

						world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
						world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1, 1);
					}
				}
			}
		}

		if (activity.length - 1 >= 0) System.arraycopy(activity, 1, activity, 0, activity.length - 1);

		activity[4] = isActive;

		if (isActive && !activity[0]) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(DefaultedBlockWithEntity.ACTIVE, true));
		} else if (!isActive && activity[0]) {
			world.setBlockState(getPos(), world.getBlockState(getPos()).with(DefaultedBlockWithEntity.ACTIVE, false));
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("cooldown", cooldown.toTag(new CompoundTag()));
		return super.toTag(tag);
	}

	@Override
	protected @NotNull Map<NetworkType, Collection<NetworkMemberType>> createMemberProperties() {
		return ofTypes(AstromineNetworkTypes.FLUID, PROVIDER, AstromineNetworkTypes.ENERGY, REQUESTER);
	}
}
