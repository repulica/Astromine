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

import com.github.chainmailstudios.astromine.common.component.block.entity.BlockEntityTransferComponent;
import com.github.chainmailstudios.astromine.common.utilities.MirrorUtilities;
import com.google.common.collect.ImmutableMap;
import nerdhub.cardinal.components.api.ComponentType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import spinnery.widget.WInterface;
import spinnery.widget.WTabHolder;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;
import spinnery.widget.api.WPositioned;

@Environment(EnvType.CLIENT)
public class WTransferTypeSelectorPanel {
	public static void createTab(WTabHolder.WTab tab, WPositioned anchor, Direction rotation, BlockEntityTransferComponent component, BlockPos blockPos, ComponentType<?> type, WInterface wInterface) {
		final Position finalNorth = Position.of(anchor, 7 + 22, 31 + 22, 0);
		final Position finalSouth = Position.of(anchor, 7 + 0, 31 + 44, 0);
		final Position finalUp = Position.of(anchor, 7 + 22, 31 + 0, 0);
		final Position finalDown = Position.of(anchor, 7 + 22, 31 + 44, 0);
		final Position finalWest = Position.of(anchor, 7 + 44, 31 + 22, 0);
		final Position finalEast = Position.of(anchor, 7 + 0, 31 + 22, 0);

		final ImmutableMap<Direction, Position> positons = ImmutableMap.<Direction, Position>builder()
				.put(Direction.NORTH, finalNorth)
				.put(Direction.SOUTH, finalSouth)
				.put(Direction.WEST, finalWest)
				.put(Direction.EAST, finalEast)
				.put(Direction.UP, finalUp)
				.put(Direction.DOWN, finalDown).build();

		tab.add(tab.getBody().createChild(WTransferTypeSelectorButton::new, positons.get(MirrorUtilities.rotate(Direction.NORTH, rotation)), Size.of(18, 18)).setComponent(component).setType(type).setRotation(rotation).setDirection(Direction.NORTH).setBlockPos(blockPos).setInterface(wInterface));
		tab.add(tab.getBody().createChild(WTransferTypeSelectorButton::new, positons.get(MirrorUtilities.rotate(Direction.SOUTH, rotation)), Size.of(18, 18)).setComponent(component).setType(type).setRotation(rotation).setDirection(Direction.SOUTH).setBlockPos(blockPos).setInterface(wInterface));
		tab.add(tab.getBody().createChild(WTransferTypeSelectorButton::new, positons.get(MirrorUtilities.rotate(Direction.UP, rotation)), Size.of(18, 18)).setComponent(component).setType(type).setRotation(rotation).setDirection(Direction.UP).setBlockPos(blockPos).setInterface(wInterface));
		tab.add(tab.getBody().createChild(WTransferTypeSelectorButton::new, positons.get(MirrorUtilities.rotate(Direction.DOWN, rotation)), Size.of(18, 18)).setComponent(component).setType(type).setRotation(rotation).setDirection(Direction.DOWN).setBlockPos(blockPos).setInterface(wInterface));
		tab.add(tab.getBody().createChild(WTransferTypeSelectorButton::new, positons.get(MirrorUtilities.rotate(Direction.WEST, rotation)), Size.of(18, 18)).setComponent(component).setType(type).setRotation(rotation).setDirection(Direction.WEST).setBlockPos(blockPos).setInterface(wInterface));
		tab.add(tab.getBody().createChild(WTransferTypeSelectorButton::new, positons.get(MirrorUtilities.rotate(Direction.EAST, rotation)), Size.of(18, 18)).setComponent(component).setType(type).setRotation(rotation).setDirection(Direction.EAST).setBlockPos(blockPos).setInterface(wInterface));
	}
}
