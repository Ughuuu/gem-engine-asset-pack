package com.gemengine.system.backend.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * Loads a file text into memory, as a {@link String}
 * 
 * @author Dragos
 *
 */
public class TextLoader extends AsynchronousAssetLoader<String, TextLoader.TextParameter> {
	public static class TextParameter extends AssetLoaderParameters<String> {
	}

	public TextLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TextParameter parameter) {
		return null;
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, TextParameter parameter) {
		// Do all the load synchronously
	}

	@Override
	public String loadSync(AssetManager manager, String fileName, FileHandle file, TextParameter parameter) {
		return file.readString();
	}
}
