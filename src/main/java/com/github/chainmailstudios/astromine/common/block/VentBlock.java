package com.github.chainmailstudios.astromine.common.block;

import com.github.chainmailstudios.astromine.common.block.entity.VentBlockEntity;
import com.github.chainmailstudios.astromine.common.network.NetworkMember;
import com.github.chainmailstudios.astromine.common.network.NetworkType;
import com.github.chainmailstudios.astromine.registry.AstromineNetworkTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.world.BlockView;

public class VentBlock extends FacingBlock implements BlockEntityProvider, NetworkMember {
	public VentBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new VentBlockEntity();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
		super.appendProperties(builder);
	}

	public BlockState getPlacementState(ItemPlacementContext context) {
		return this.getDefaultState().with(FACING, context.getPlayerLookDirection().getOpposite());
	}

	@Override
	public <T extends NetworkType> boolean acceptsType(T type) {
		return type == AstromineNetworkTypes.ENERGY || type == AstromineNetworkTypes.FLUID;
	}

	@Override
	public boolean isRequester() {
		return true;
	}
}
