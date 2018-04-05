package com.gemengine.system.backend.loaders;

import java.io.IOException;
import java.util.jar.JarFile;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

@SuppressWarnings("rawtypes")
/**
 * Code loader class. This generates an instance of
 * {@link org.jsync.sync.ClassSync} from a .class file
 * 
 * @author Dragos
 *
 * @param <T>
 */
public class JarLoader extends AsynchronousAssetLoader<JarFile, JarLoader.JarParameter> {
	public static class JarParameter extends AssetLoaderParameters<JarFile> {
	}

	public JarLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, JarParameter parameter) {
		return null;
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, JarParameter parameter) {
		// Do all the load synchronously
	}

	@Override
	public JarFile loadSync(AssetManager manager, String fileName, FileHandle file, JarParameter parameter) {
		try {
			return new JarFile(fileName);
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}
}
