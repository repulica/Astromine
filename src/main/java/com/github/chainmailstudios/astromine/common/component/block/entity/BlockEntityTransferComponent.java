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
package com.github.chainmailstudios.astromine.common.component.block.entity;

import com.github.chainmailstudios.astromine.common.block.transfer.TransferType;
import com.github.chainmailstudios.astromine.common.utilities.DirectionUtilities;
import com.google.common.collect.Maps;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Arrays;
import java.util.Map;

public class BlockEntityTransferComponent implements Component {
	private final Map<ComponentType<?>, TransferEntry> components = Maps.newHashMap();

	public BlockEntityTransferComponent(ComponentType<?>... types) {
		Arrays.asList(types).forEach(this::add);
	}

	public TransferEntry get(ComponentType<?> type) {
		return components.get(type);
	}

	public Map<ComponentType<?>, TransferEntry> get() {
		return components;
	}

	public void add(ComponentType<?> type) {
		components.put(type, new TransferEntry());
	}

	@Override
	public void fromTag(CompoundTag tag) {
		CompoundTag dataTag = tag.getCompound("data");

		for (String key : dataTag.getKeys()) {
			Identifier keyId = new Identifier(key);
			TransferEntry entry = new TransferEntry();
			entry.fromTag(dataTag.getCompound(key));
			components.put(ComponentRegistry.INSTANCE.get(keyId), entry);
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag dataTag = new CompoundTag();

		for (Map.Entry<ComponentType<?>, TransferEntry> entry : components.entrySet()) {
			dataTag.put(entry.getKey().getId().toString(), entry.getValue().toTag(new CompoundTag()));
		}

		tag.put("data", dataTag);

		return tag;
	}

	public static final class TransferEntry {
		private final Map<Direction, TransferType> types = Maps.newHashMap();

		public TransferEntry() {
			Arrays.asList(Direction.values()).forEach(direction -> set(direction, TransferType.NONE));
		}

		public void set(Direction direction, TransferType type) {
			types.put(direction, type);
		}

		public TransferType get(Direction origin) {
			return types.get(origin);
		}

		public void fromTag(CompoundTag tag) {
			for (String directionKey : tag.getKeys()) {
				types.put(DirectionUtilities.byNameOrId(directionKey), TransferType.valueOf(tag.getString(directionKey)));
			}
		}

		public CompoundTag toTag(CompoundTag tag) {
			for (Map.Entry<Direction, TransferType> entry : types.entrySet()) {
				tag.putString(String.valueOf(entry.getKey().getName()), entry.getValue().toString());
			}

			return tag;
		}

		public boolean areAllNone() {
			for (TransferType value : types.values()) {
				if (value != TransferType.NONE)
					return false;
			}
			return true;
		}
	}
}
