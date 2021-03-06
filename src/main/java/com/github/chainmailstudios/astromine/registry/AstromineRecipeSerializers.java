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

import com.github.chainmailstudios.astromine.common.recipe.*;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.registry.Registry;

public class AstromineRecipeSerializers {
	public static final RecipeSerializer<TrituratingRecipe> TRITURATING = Registry.register(
			Registry.RECIPE_SERIALIZER,
			TrituratingRecipe.Serializer.ID,
			TrituratingRecipe.Serializer.INSTANCE);

	public static final RecipeSerializer<PressingRecipe> PRESSING = Registry.register(
			Registry.RECIPE_SERIALIZER,
			PressingRecipe.Serializer.ID,
			PressingRecipe.Serializer.INSTANCE);

	public static final RecipeSerializer<LiquidGeneratingRecipe> LIQUID_GENERATING = Registry.register(
			Registry.RECIPE_SERIALIZER,
			LiquidGeneratingRecipe.Serializer.ID,
			LiquidGeneratingRecipe.Serializer.INSTANCE);

	public static final RecipeSerializer<ElectrolyzingRecipe> ELECTROLYZER = Registry.register(
			Registry.RECIPE_SERIALIZER,
			ElectrolyzingRecipe.Serializer.ID,
			ElectrolyzingRecipe.Serializer.INSTANCE);

	public static final RecipeSerializer<FluidMixingRecipe> FLUID_MIXER = Registry.register(
			Registry.RECIPE_SERIALIZER,
			FluidMixingRecipe.Serializer.ID,
			FluidMixingRecipe.Serializer.INSTANCE);

	public static final RecipeSerializer<SolidGeneratingRecipe> SOLID_GENERATING = Registry.register(
			Registry.RECIPE_SERIALIZER,
			SolidGeneratingRecipe.Serializer.ID,
			SolidGeneratingRecipe.Serializer.INSTANCE);

	public static final RecipeSerializer<AlloySmeltingRecipe> ALLOY_SMELTING = Registry.register(
			Registry.RECIPE_SERIALIZER,
			AlloySmeltingRecipe.Serializer.ID,
			AlloySmeltingRecipe.Serializer.INSTANCE);

	public static void initialize() {
		// Unused.
	}
}
