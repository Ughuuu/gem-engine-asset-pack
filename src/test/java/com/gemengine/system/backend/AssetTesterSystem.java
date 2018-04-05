package com.gemengine.system.backend;

import static org.junit.Assert.assertEquals;

import com.gemengine.system.common.SystemBase;
import com.google.inject.Inject;

public class AssetTesterSystem extends SystemBase{
	private final AssetSystem assetSystem;
	
	@Inject
	private AssetTesterSystem(AssetSystem assetSystem) {
		this.assetSystem = assetSystem;
	}
	
	public void doTestSequence() {
		String contents = assetSystem.getAsset("test.txt");
		assertEquals("test", contents);		
	}
}
