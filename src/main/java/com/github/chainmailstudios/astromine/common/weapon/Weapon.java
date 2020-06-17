package com.github.chainmailstudios.astromine.common.weapon;

import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;

public interface Weapon {
	float getZoom();

	float getDamage();

	float getDistance();

	int getPunch();

	float getRecoil();

	long getShotInterval();

	long getReloadInterval();

	long getLastShot();

	void setLastShot(long lastShot);

	long getLastReload();

	void setLastReload(long lastReload);

	default boolean isReloading(long currentTime) {
		return currentTime - getLastReload() < getReloadInterval();
	}

	default boolean isWaiting(long currentTime) {
		return currentTime - getLastShot() < getShotInterval();
	}

	SoundEvent getShotSound();

	Vector3f getTranslation();

	Item getAmmo();
}