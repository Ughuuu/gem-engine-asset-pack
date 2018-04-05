package com.gemengine.system.backend.loader;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.PixmapLoader;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.gemengine.system.backend.AssetSystem;
import com.gemengine.system.backend.loaders.LoaderData;
import com.gemengine.system.common.SystemBase;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class GraphicsLoaderSystem extends SystemBase {
	@Inject
	private GraphicsLoaderSystem(AssetSystem assetSystem, @Named("pixmapFolder") String pixmapFolder,
			@Named("atlasFolder") String atlasFolder, @Named("imageFolder") String imageFolder) {
		final FileHandleResolver resolver = assetSystem.getFileHandleResolver();
		assetSystem.addLoaderDefault(new LoaderData<Pixmap>(Pixmap.class, pixmapFolder), new PixmapLoader(resolver),
				".png", ".jpg", ".jpeg", ".bmp");
		assetSystem.addLoaderDefault(new LoaderData<TextureAtlas>(TextureAtlas.class, atlasFolder),
				new TextureAtlasLoader(resolver), ".atlas");

		assetSystem.addLoaderDefault(new LoaderData<Texture>(Texture.class, imageFolder), new TextureLoader(resolver),
				".png", ".jpg", ".jpeg", ".bmp");
	}
}
