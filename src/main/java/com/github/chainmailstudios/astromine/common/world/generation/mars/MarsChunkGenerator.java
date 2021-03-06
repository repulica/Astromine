package com.github.chainmailstudios.astromine.common.world.generation.mars;

import java.util.Arrays;
import java.util.Random;

import com.github.chainmailstudios.astromine.common.miscellaneous.BiomeGenCache;
import com.github.chainmailstudios.astromine.common.noise.OctaveNoiseSampler;
import com.github.chainmailstudios.astromine.common.noise.OpenSimplexNoise;
import com.github.chainmailstudios.astromine.common.world.generation.moon.MoonBiomeSource;
import com.github.chainmailstudios.astromine.registry.AstromineBlocks;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

public class MarsChunkGenerator extends ChunkGenerator {
	public static Codec<MarsChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(BiomeSource.field_24713.fieldOf("biome_source").forGetter(gen -> gen.biomeSource), Codec.LONG.fieldOf("seed").forGetter(gen -> gen.seed))
			.apply(instance, MarsChunkGenerator::new));

	private final BiomeSource biomeSource;
	private final long seed;
	private final OctaveNoiseSampler<OpenSimplexNoise> lowerInterpolatedNoise;
	private final OctaveNoiseSampler<OpenSimplexNoise> upperInterpolatedNoise;
	private final OctaveNoiseSampler<OpenSimplexNoise> interpolationNoise;
	private final ThreadLocal<BiomeGenCache> cache;
	public MarsChunkGenerator(BiomeSource biomeSource, long seed) {
		super(biomeSource, new StructuresConfig(false));
		this.biomeSource = biomeSource;
		this.seed = seed;
		Random random = new Random(seed);
		lowerInterpolatedNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, random, 5, 140.43, 45, 10);
		upperInterpolatedNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, random, 5, 140.43, 45, 10);
		interpolationNoise = new OctaveNoiseSampler<>(OpenSimplexNoise.class, random, 3, 80.32, 3, 3);
		this.cache = ThreadLocal.withInitial(() -> new BiomeGenCache(biomeSource));
	}

	@Override
	protected Codec<? extends ChunkGenerator> method_28506() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long seed) {
		return new MarsChunkGenerator(new MoonBiomeSource(seed), seed);
	}

	@Override
	public void buildSurface(ChunkRegion region, Chunk chunk) {
		// Unused.
	}

	@Override
	public void populateNoise(WorldAccess world, StructureAccessor accessor, Chunk chunk) {
		int x1 = chunk.getPos().getStartX();
		int z1 = chunk.getPos().getStartZ();

		int x2 = chunk.getPos().getEndX();
		int z2 = chunk.getPos().getEndZ();

		ChunkRandom chunkRandom = new ChunkRandom();
		chunkRandom.setTerrainSeed(chunk.getPos().x, chunk.getPos().z);

		for (int x = x1; x <= x2; ++x) {
			for (int z = z1; z <= z2; ++z) {
				float depth = 0;
				float scale = 0;
				int i = 0;

				// Biome lerp
				for (int x0 = -5; x0 <= 5; x0++) {
					for (int z0 = -5; z0 <= 5; z0++) {
						Biome biome = this.cache.get().getBiome((x + x0) >> 2, (z + z0) >> 2);

						i++;
						depth += biome.getDepth();
						scale += biome.getScale();
					}
				}

				depth /= i;
				scale /= i;

				// Noise calculation
				double noise = interpolationNoise.sample(x, z);
				if (noise >= 1) {
					noise = upperInterpolatedNoise.sample(x, z);
				} else if (noise <= -1) {
					noise = lowerInterpolatedNoise.sample(x, z);
				} else {
					noise = MathHelper.clampedLerp(lowerInterpolatedNoise.sample(x, z), upperInterpolatedNoise.sample(x, z), noise);
				}

				int height = (int) (depth + (noise * scale));
				for (int y = 0; y <= height; ++y) {
					chunk.setBlockState(new BlockPos(x, y, z), AstromineBlocks.MARS_SOIL.getDefaultState(), false);
					if (y <= 5) {
						if (chunkRandom.nextInt(y + 1) == 0) {
							chunk.setBlockState(new BlockPos(x, y, z), Blocks.BEDROCK.getDefaultState(), false);
						}
					}
				}
			}
		}
	}

	@Override
	public int getHeight(int x, int z, Heightmap.Type heightmapType) {
		return 0;
	}

	@Override
	public BlockView getColumnSample(int x, int z) {
		BlockState[] states = new BlockState[256];
		Arrays.fill(states, Blocks.AIR.getDefaultState());
		return new VerticalBlockSample(states);
	}
}
