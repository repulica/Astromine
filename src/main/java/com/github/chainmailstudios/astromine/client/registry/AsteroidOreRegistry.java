package com.github.chainmailstudios.astromine.client.registry;

import com.github.chainmailstudios.astromine.common.registry.BetaRegistry;
import com.github.chainmailstudios.astromine.common.registry.DeltaRegistry;
import com.github.chainmailstudios.astromine.common.utilities.data.Range;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;

import java.util.List;

public class AsteroidOreRegistry extends DeltaRegistry<Integer, Block> {
	public static final AsteroidOreRegistry INSTANCE = new AsteroidOreRegistry();

	private AsteroidOreRegistry() {
		// Locked.
	}

	public void register(Range<Integer> range, Block block) {
		if (range.getMinimum() > range.getMaximum()) {
			range = Range.of(range.getMaximum(), range.getMinimum());
		}

		for (int chance = range.getMinimum(); chance < range.getMaximum(); ++chance) {
			this.register(chance, block);
		}
	}
}