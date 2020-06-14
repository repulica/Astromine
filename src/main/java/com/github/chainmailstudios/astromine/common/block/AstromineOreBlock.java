package com.github.chainmailstudios.astromine.common.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;

import com.github.chainmailstudios.astromine.registry.AstromineBlocks;

import java.util.Random;

public class AstromineOreBlock extends OreBlock {
	public AstromineOreBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	protected int getExperienceWhenMined(Random random) {
		if(this == AstromineBlocks.ASTERITE_ORE) {
			return MathHelper.nextInt(random, 4, 8);
		} else if(this == AstromineBlocks.GALAXIUM_ORE) {
			return MathHelper.nextInt(random, 5, 9);
		} else if(this == AstromineBlocks.METITE_ORE) {
			return MathHelper.nextInt(random, 3, 7);
		} else return 0;
	}
}
