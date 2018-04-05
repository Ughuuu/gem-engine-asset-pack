package com.gemengine.system.backend.loaders;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;

/**
 * This is used to hold data about loaders.
 * 
 * @author Dragos
 *
 */
public class LoaderData<T> {
	private AssetLoaderParameters<T> assetLoaderParameters;

	private final String folder;

	private final Class<T> type;

	public LoaderData(Class<T> type) {
		this(type, null, "");
	}

	public LoaderData(Class<T> type, AssetLoaderParameters<T> assetLoaderParameters, String folder) {
		this.type = type;
		this.assetLoaderParameters = assetLoaderParameters;
		this.folder = folder;
	}

	public LoaderData(Class<T> type, String folder) {
		this(type, null, folder);
	}

	public String getFolder() {
		return folder;
	}

	public Class<T> getType() {
		return type;
	}

	/**
	 * load a new asset using this loader data. Don't call this, this is called from
	 * Asset System.
	 * 
	 * @param assetManager
	 *            The asset manager
	 * @param path
	 *            The path.
	 */
	public void load(AssetManager assetManager, String path) {
		if (assetLoaderParameters != null) {
			assetManager.load(path, getType(), assetLoaderParameters);
		} else {
			assetManager.load(path, type);
		}
	}
}