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
package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class AstromineOreBlock extends OreBlock {
	public AstromineOreBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	protected int getExperienceWhenMined(Random random) {
		if (this == AstromineBlocks.ASTEROID_ASTERITE_ORE) {
			return MathHelper.nextInt(random, 5, 8);
		} else if (this == AstromineBlocks.ASTEROID_GALAXIUM_ORE || this == AstromineBlocks.ASTEROID_STELLUM_ORE) {
			return MathHelper.nextInt(random, 6, 9);
		} else if (this == AstromineBlocks.ASTEROID_METITE_ORE || this == AstromineBlocks.METEOR_METITE_ORE) {
			return MathHelper.nextInt(random, 4, 7);
		} else if (this == AstromineBlocks.ASTEROID_COAL_ORE) {
			return MathHelper.nextInt(random, 0, 2);
		} else if (this == AstromineBlocks.ASTEROID_TIN_ORE || this == AstromineBlocks.ASTEROID_COPPER_ORE) {
			return MathHelper.nextInt(random, 1, 2);
		} else if (this == AstromineBlocks.ASTEROID_IRON_ORE) {
			return MathHelper.nextInt(random, 1, 3);
		} else if (this == AstromineBlocks.ASTEROID_GOLD_ORE) {
			return MathHelper.nextInt(random, 2, 3);
		} else if (this == AstromineBlocks.ASTEROID_DIAMOND_ORE || this == AstromineBlocks.ASTEROID_EMERALD_ORE) {
			return MathHelper.nextInt(random, 3, 7);
		} else if (this == AstromineBlocks.ASTEROID_LAPIS_ORE || this == AstromineBlocks.ASTEROID_REDSTONE_ORE) {
			return MathHelper.nextInt(random, 2, 5);
		} else {
			return 0;
		}
	}
}
