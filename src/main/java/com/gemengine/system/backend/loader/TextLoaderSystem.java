package com.gemengine.system.backend.loader;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gemengine.system.backend.AssetSystem;
import com.gemengine.system.backend.loaders.LoaderData;
import com.gemengine.system.backend.loaders.TextLoader;
import com.gemengine.system.common.SystemBase;
import com.google.inject.Inject;

public class TextLoaderSystem extends SystemBase {
	@Inject
	private TextLoaderSystem(AssetSystem assetSystem) {
		final FileHandleResolver resolver = assetSystem.getFileHandleResolver();
		assetSystem.addLoaderDefault(new LoaderData<String>(String.class), new TextLoader(resolver), ".txt", ".json");
	}
}
