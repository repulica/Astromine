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

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public class AstromineToolMaterials {
	public static final AstromineToolMaterial COPPER = new AstromineToolMaterial(1, 200, 4f, 1.5f, 10, () -> Ingredient.ofItems(AstromineItems.COPPER_INGOT));
	public static final AstromineToolMaterial TIN = new AstromineToolMaterial(1, 200, 5f, 1.0f, 10, () -> Ingredient.ofItems(AstromineItems.TIN_INGOT));
	public static final AstromineToolMaterial BRONZE = new AstromineToolMaterial(2, 539, 7f, 2.5f, 20, () -> Ingredient.ofItems(AstromineItems.BRONZE_INGOT));
	public static final AstromineToolMaterial STEEL = new AstromineToolMaterial(3, 1043, 7.5f, 3f, 24, () -> Ingredient.ofItems(AstromineItems.STEEL_INGOT));

	public static final AstromineToolMaterial METITE = new AstromineToolMaterial(2, 981, 14f, 5.0f, 5, () -> Ingredient.ofItems(AstromineItems.METITE_INGOT));
	public static final AstromineToolMaterial ASTERITE = new AstromineToolMaterial(5, 2015, 10f, 5.0f, 20, () -> Ingredient.ofItems(AstromineItems.ASTERITE));
	public static final AstromineToolMaterial STELLUM = new AstromineToolMaterial(5, 2643, 8f, 6.0f, 15, () -> Ingredient.ofItems(AstromineItems.STELLUM_INGOT));
	public static final AstromineToolMaterial GALAXIUM = new AstromineToolMaterial(6, 3072, 11f, 5.0f, 18, () -> Ingredient.ofItems(AstromineItems.GALAXIUM));
	public static final AstromineToolMaterial UNIVITE = new AstromineToolMaterial(7, 3918, 12f, 6.0f, 22, () -> Ingredient.ofItems(AstromineItems.UNIVITE_INGOT));

	public static class AstromineToolMaterial implements ToolMaterial {
		private final int miningLevel;
		private final int itemDurability;
		private final float miningSpeed;
		private final float attackDamage;
		private final int enchantability;
		private final Lazy<Ingredient> repairIngredient;

		AstromineToolMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantibility, Supplier<Ingredient> repairIngredient) {
			this.miningLevel = miningLevel;
			this.itemDurability = itemDurability;
			this.miningSpeed = miningSpeed;
			this.attackDamage = attackDamage;
			this.enchantability = enchantibility;
			this.repairIngredient = new Lazy(repairIngredient);
		}

		@Override
		public int getDurability() {
			return this.itemDurability;
		}

		@Override
		public float getMiningSpeedMultiplier() {
			return this.miningSpeed;
		}

		@Override
		public float getAttackDamage() {
			return this.attackDamage;
		}

		@Override
		public int getMiningLevel() {
			return this.miningLevel;
		}

		@Override
		public int getEnchantability() {
			return this.enchantability;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return this.repairIngredient.get();
		}
	}
}
