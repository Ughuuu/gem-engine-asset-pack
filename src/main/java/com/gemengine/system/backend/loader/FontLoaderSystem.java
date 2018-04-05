package com.gemengine.system.backend.loader;

import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.gemengine.system.backend.AssetSystem;
import com.gemengine.system.backend.loaders.LoaderData;
import com.gemengine.system.common.SystemBase;
import com.google.inject.Inject;

public class FontLoaderSystem extends SystemBase {
	@Inject
	private FontLoaderSystem(AssetSystem assetSystem) {
		final FileHandleResolver resolver = assetSystem.getFileHandleResolver();
		assetSystem.addLoaderDefault(new LoaderData<BitmapFont>(BitmapFont.class), new BitmapFontLoader(resolver),
				".fnt");
	}
}
