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
package com.github.chainmailstudios.astromine.client.rei.fluidmixing;

import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public abstract class AbstractFluidMixingDisplay implements RecipeDisplay {
	private final double energy;
	private final FluidVolume firstInput;
	private final FluidVolume secondInput;
	private final FluidVolume output;
	private final Identifier id;

	public AbstractFluidMixingDisplay(double energy, FluidVolume firstInput, FluidVolume secondInput, FluidVolume output, Identifier id) {
		this.energy = energy;
		this.firstInput = firstInput;
		this.secondInput = secondInput;
		this.output = output;
		this.id = id;
	}

	@Override
	public Optional<Identifier> getRecipeLocation() {
		return Optional.ofNullable(id);
	}

	@Override
	public List<List<EntryStack>> getInputEntries() {
		return Arrays.asList(
				Collections.singletonList(EntryStack.create(firstInput.getFluid())),
				Collections.singletonList(EntryStack.create(secondInput.getFluid()))
		);
	}

	@Override
	public List<List<EntryStack>> getRequiredEntries() {
		return getInputEntries();
	}

	@Override
	public List<EntryStack> getOutputEntries() {
		return Collections.singletonList(EntryStack.create(output.getFluid()));
	}

	public double getEnergy() {
		return energy;
	}

	public FluidVolume getFirstInput() {
		return firstInput;
	}

	public FluidVolume getSecondInput() {
		return secondInput;
	}

	public FluidVolume getOutput() {
		return output;
	}
}
