package com.gemengine.system.backend.loader;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gemengine.system.backend.AssetSystem;
import com.gemengine.system.backend.loaders.LoaderData;
import com.gemengine.system.common.SystemBase;
import com.google.inject.Inject;

public class UISkinLoaderSystem extends SystemBase {
	@Inject
	private UISkinLoaderSystem(AssetSystem assetSystem) {
		final FileHandleResolver resolver = assetSystem.getFileHandleResolver();
		assetSystem.addLoaderDefault(new LoaderData<Skin>(Skin.class, "uiskin/"), new SkinLoader(resolver), ".json");
	}
}
