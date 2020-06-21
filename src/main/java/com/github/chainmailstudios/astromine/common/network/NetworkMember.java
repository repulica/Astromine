package com.github.chainmailstudios.astromine.common.network;

public interface NetworkMember {
	default NetworkType getNetworkType() {
		return NetworkType.EMPTY;
	}

	default boolean isNode() {
		return true;
	}

	default boolean isRequester() {
		return false;
	}

	default boolean isProvider() {
		return false;
	}

	default boolean isBuffer() {
		return false;
	}

	default boolean isVisual() {
		return false;
	}
}
