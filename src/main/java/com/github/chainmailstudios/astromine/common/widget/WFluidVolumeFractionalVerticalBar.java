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
package com.github.chainmailstudios.astromine.common.widget;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.client.BaseRenderer;
import com.github.chainmailstudios.astromine.client.render.SpriteRenderer;
import com.github.chainmailstudios.astromine.common.utilities.FluidUtilities;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluids;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import spinnery.client.render.layer.SpinneryLayers;

import java.util.List;
import java.util.function.Supplier;

public class WFluidVolumeFractionalVerticalBar extends WFractionalVerticalBar {
	private Supplier<FluidVolume> volume;

	private final Identifier FLUID_BACKGROUND = AstromineCommon.identifier("textures/widget/fluid_volume_fractional_vertical_bar_background.png");

	public WFluidVolumeFractionalVerticalBar() {
		setUnit(new TranslatableText("text.astromine.fluid"));
	}

	@Override
	public Identifier getBackgroundTexture() {
		return FLUID_BACKGROUND;
	}

	public FluidVolume getFluidVolume() {
		return volume.get();
	}

	public <W extends WFractionalVerticalBar> W setFluidVolume(Supplier<FluidVolume> volume) {
		setProgressFraction(volume.get()::getFraction);
		setLimitFraction(volume.get()::getSize);

		this.volume = volume;
		return (W) this;
	}

	@Override
	public List<Text> getTooltip() {
		return Lists.newArrayList(FluidUtilities.rawFraction(getProgressFraction().get(), getLimitFraction().get(), getUnit()), new TranslatableText("text.astromine.tooltip.fractional_value", getProgressFraction().get().toDecimalString(), getLimitFraction().get().toDecimalString()));
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		float x = getX();
		float y = getY();
		float z = getZ();

		float sX = getWidth();
		float sY = getHeight();

		float sBGY = (((sY / getLimitFraction().get().floatValue()) * getProgressFraction().get().floatValue()));

		RenderLayer layer = SpinneryLayers.get(getBackgroundTexture());

		BaseRenderer.drawTexturedQuad(matrices, provider, layer, x, y, z, getWidth(), getHeight(), getBackgroundTexture());

		if (getFluidVolume().getFluid() != Fluids.EMPTY) {
			SpriteRenderer.beginPass()
					.setup(provider, RenderLayer.getSolid())
					.sprite(FluidUtilities.texture(getFluidVolume().getFluid())[0])
					.color(FluidUtilities.color(MinecraftClient.getInstance().player, getFluidVolume().getFluid()))
					.light(0x00f000f0)
					.overlay(OverlayTexture.DEFAULT_UV)
					.alpha(0xff)
					.normal(matrices.peek().getNormal(), 0, 0, 0)
					.position(matrices.peek().getModel(), x + 1, y + 1 + Math.max(0, sY - ((int) (sBGY) + 1)), x + sX - 1, y + sY - 1, z)
					.next(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
		}
	}
}
