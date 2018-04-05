package com.gemengine.system.backend.loader;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonRegionLoader;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.UBJsonReader;
import com.gemengine.system.backend.AssetSystem;
import com.gemengine.system.backend.loaders.LoaderData;
import com.gemengine.system.common.SystemBase;
import com.google.inject.Inject;

public class MeshLoaderSystem extends SystemBase {
	@Inject
	private MeshLoaderSystem(AssetSystem assetSystem) {
		final FileHandleResolver resolver = assetSystem.getFileHandleResolver();
		assetSystem.addLoaderDefault(new LoaderData<PolygonRegion>(PolygonRegion.class),
				new PolygonRegionLoader(resolver), ".psh");
		assetSystem.addLoaderOverride(new LoaderData<Model>(Model.class),
				new G3dModelLoader(new JsonReader(), resolver), ".g3dj");
		assetSystem.addLoaderOverride(new LoaderData<Model>(Model.class),
				new G3dModelLoader(new UBJsonReader(), resolver), ".g3db");
		assetSystem.addLoaderOverride(new LoaderData<Model>(Model.class), new ObjLoader(resolver), ".obj");
	}
}
