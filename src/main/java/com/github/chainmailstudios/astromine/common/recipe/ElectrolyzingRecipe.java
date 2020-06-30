package com.github.chainmailstudios.astromine.common.recipe;

import com.github.chainmailstudios.astromine.common.utilities.PacketUtilities;
import com.github.chainmailstudios.astromine.common.utilities.ParsingUtilities;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import com.github.chainmailstudios.astromine.AstromineCommon;
import com.github.chainmailstudios.astromine.common.block.entity.ElectrolyzerBlockEntity;
import com.github.chainmailstudios.astromine.common.component.inventory.EnergyInventoryComponent;
import com.github.chainmailstudios.astromine.common.component.inventory.FluidInventoryComponent;
import com.github.chainmailstudios.astromine.common.fraction.Fraction;
import com.github.chainmailstudios.astromine.common.utilities.FractionUtilities;
import com.github.chainmailstudios.astromine.common.volume.energy.EnergyVolume;
import com.github.chainmailstudios.astromine.common.volume.fluid.FluidVolume;
import com.github.chainmailstudios.astromine.registry.AstromineComponentTypes;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ElectrolyzingRecipe implements Recipe<Inventory> {
	final Identifier identifier;
	final RegistryKey<Fluid> inputFluidKey;
	final Lazy<Fluid> inputFluid;
	final Fraction inputAmount;
	final RegistryKey<Fluid> outputFluidKey;
	final Lazy<Fluid> outputFluid;
	final Fraction outputAmount;
	final Fraction energyConsumed;
	final int time;

	public ElectrolyzingRecipe(Identifier identifier, RegistryKey<Fluid> inputFluidKey, Fraction inputAmount, RegistryKey<Fluid> outputFluidKey, Fraction outputAmount, Fraction energyConsumed, int time) {
		this.identifier = identifier;
		this.inputFluidKey = inputFluidKey;
		this.inputFluid = new Lazy<>(() -> Registry.FLUID.get(this.inputFluidKey));
		this.inputAmount = inputAmount;
		this.outputFluidKey = outputFluidKey;
		this.outputFluid = new Lazy<>(() -> Registry.FLUID.get(this.outputFluidKey));
		this.outputAmount = outputAmount;
		this.energyConsumed = energyConsumed;
		this.time = time;
	}

	public boolean tryCrafting(ElectrolyzerBlockEntity electrolyzer, boolean isActuallyDoing) {
		FluidInventoryComponent fluidComponent = electrolyzer.getComponent(AstromineComponentTypes.FLUID_INVENTORY_COMPONENT);

		FluidVolume inputVolume = fluidComponent.getVolume(0);
		FluidVolume outputVolume = fluidComponent.getVolume(1);

		if (!inputVolume.getFluid().matchesType(inputFluid.get())) return false;
		if (!inputVolume.hasStored(inputAmount)) return false;
		if (!outputVolume.getFluid().matchesType(outputFluid.get()) && !outputVolume.isEmpty()) return false;
		if (!outputVolume.hasAvailable(outputAmount)) return false;

		if (isActuallyDoing) {
			EnergyInventoryComponent energyComponent = electrolyzer.getComponent(AstromineComponentTypes.ENERGY_INVENTORY_COMPONENT);

			EnergyVolume energyVolume = energyComponent.getVolume(0);

			if (energyVolume.hasStored(energyConsumed)) {
				inputVolume.extractVolume(inputAmount);
				energyVolume.extractVolume(energyConsumed);
				outputVolume.insertVolume(new FluidVolume(outputFluid.get(), outputAmount));
			}
		}

		return true;
	}
	
	@Override
	public boolean matches(Inventory inventory, World world) {
		return false; // we are not dealing with items
	}
	
	@Override
	public ItemStack craft(Inventory inventory) {
		return ItemStack.EMPTY; // we are not dealing with items
	}
	
	@Override
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	public ItemStack getOutput() {
		return ItemStack.EMPTY; // we are not dealing with items
	}
	
	@Override
	public Identifier getId() {
		return identifier;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}
	
	@Override
	public RecipeType<?> getType() {
		return Type.INSTANCE;
	}
	
	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		return DefaultedList.of(); // we are not dealing with items
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public Fluid getInputFluid() {
		return inputFluid.get();
	}

	public Fraction getInputAmount() {
		return inputAmount;
	}

	public Fluid getOutputFluid() {
		return outputFluid.get();
	}

	public Fraction getOutputAmount() {
		return outputAmount;
	}

	public Fraction getEnergyConsumed() {
		return energyConsumed;
	}

	public int getTime() {
		return time;
	}

	public static final class Serializer implements RecipeSerializer<ElectrolyzingRecipe> {
		public static final Identifier ID = AstromineCommon.identifier("electrolyzing");
		
		public static final Serializer INSTANCE = new Serializer();
		
		private Serializer() {
			// Locked.
		}
		
		@Override
		public ElectrolyzingRecipe read(Identifier identifier, JsonObject object) {
			ElectrolyzingRecipe.Format format = new Gson().fromJson(object, ElectrolyzingRecipe.Format.class);
			
			return new ElectrolyzingRecipe(identifier,
					RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.input)),
					FractionUtilities.fromJson(format.inputAmount),
					RegistryKey.of(Registry.FLUID_KEY, new Identifier(format.output)),
					FractionUtilities.fromJson(format.outputAmount),
					FractionUtilities.fromJson(format.energyGenerated),
					ParsingUtilities.fromJson(format.time, Integer.class));
		}
		
		@Override
		public ElectrolyzingRecipe read(Identifier identifier, PacketByteBuf buffer) {
			return new ElectrolyzingRecipe(identifier,
					RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()),
					FractionUtilities.fromPacket(buffer),
					RegistryKey.of(Registry.FLUID_KEY, buffer.readIdentifier()),
					FractionUtilities.fromPacket(buffer),
					FractionUtilities.fromPacket(buffer),
					PacketUtilities.fromPacket(buffer, Integer.class));
		}
		
		@Override
		public void write(PacketByteBuf buffer, ElectrolyzingRecipe recipe) {
			buffer.writeIdentifier(recipe.inputFluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.inputAmount);
			buffer.writeIdentifier(recipe.outputFluidKey.getValue());
			FractionUtilities.toPacket(buffer, recipe.outputAmount);
			FractionUtilities.toPacket(buffer, recipe.energyConsumed);
			buffer.writeInt(recipe.getTime());
		}
	}
	
	public static final class Type implements RecipeType<ElectrolyzingRecipe> {
		public static final Type INSTANCE = new Type();
		
		private Type() {
			// Locked.
		}
	}
	
	public static final class Format {
		String input;
		@SerializedName("input_amount")
		JsonElement inputAmount;

		String output;
		@SerializedName("output_amount")
		JsonElement outputAmount;

		@SerializedName("energy_consumed")
		JsonElement energyGenerated;

		JsonElement time;

		@Override
		public String toString() {
			return "Format{" +
					"input='" + input + '\'' +
					", inputAmount=" + inputAmount +
					", output='" + output + '\'' +
					", outputAmount=" + outputAmount +
					", energyGenerated=" + energyGenerated +
					", time=" + time +
					'}';
		}
	}
}
