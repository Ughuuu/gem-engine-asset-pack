package com.gemengine.system.backend.loader;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gemengine.system.backend.AssetSystem;
import com.gemengine.system.backend.loaders.LoaderData;
import com.gemengine.system.common.SystemBase;
import com.google.inject.Inject;

public class ShaderLoaderSystem extends SystemBase {
	@Inject
	private ShaderLoaderSystem(AssetSystem assetSystem) {
		final FileHandleResolver resolver = assetSystem.getFileHandleResolver();
		assetSystem.addLoaderDefault(new LoaderData<ShaderProgram>(ShaderProgram.class),
				new ShaderProgramLoader(resolver), ".vert");
	}
}
