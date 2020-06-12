package com.github.chainmailstudios.astromine.mixin;

import com.github.chainmailstudios.astromine.common.weapon.WeaponElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	private static double lastFov = 0;

	private static boolean isTransitioning = false;

	@Inject(at = @At("RETURN"), method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D", cancellable = true)
	void onGetFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> callbackInformationReturnable) {
		double gameFov = callbackInformationReturnable.getReturnValueD();

		if (MinecraftClient.getInstance().options.keyUse.isPressed()) {
			Item heldItem = MinecraftClient.getInstance().player.getMainHandStack().getItem();


			if (heldItem instanceof WeaponElement) {
				double weaponFov = ((WeaponElement) heldItem).getZoom();

				if (gameFov > weaponFov) {
					gameFov = MathHelper.lerp(tickDelta / 10, lastFov, weaponFov);
				} else {
					gameFov = weaponFov;
				}

				isTransitioning = true;
			}
		} else {
			Item heldItem = MinecraftClient.getInstance().player.getMainHandStack().getItem();

			if (heldItem instanceof WeaponElement) {
				if (isTransitioning && lastFov < gameFov) {
					gameFov = MathHelper.lerp(tickDelta / 10, lastFov, gameFov);
				} else {
					isTransitioning = false;
				}
			}
		}

		callbackInformationReturnable.setReturnValue(gameFov);
		callbackInformationReturnable.cancel();

		lastFov = gameFov;
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;bobViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V"), method = "renderHand(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/Camera;F)V")
	void onRenderWorld(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo callbackInformation) {
		if (MinecraftClient.getInstance().options.keyUse.isPressed()) {
			Item heldItem = MinecraftClient.getInstance().player.getMainHandStack().getItem();

			if (heldItem instanceof WeaponElement) {
				WeaponElement weapon = (WeaponElement) heldItem;

				Vector3f translation = weapon.getTranslation();

				matrices.translate(translation.getX(), translation.getY(), translation.getZ());
			}
		}
	}
}
