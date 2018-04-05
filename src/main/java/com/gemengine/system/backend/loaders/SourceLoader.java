package com.gemengine.system.backend.loaders;

import org.jsync.sync.SourceSync;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * Source loader class. This generates an instance of
 * {@link org.jsync.sync.SourceSync} from a .java file
 * 
 * @author Dragos
 *
 * @param <T>
 */
public class SourceLoader extends AsynchronousAssetLoader<SourceSync, SourceLoader.SourceParameter> {
	public static class SourceParameter extends AssetLoaderParameters<SourceSync> {
	}

	private final String assetsFolder;
	private final String completeCodeFolder;

	public SourceLoader(FileHandleResolver resolver, String assetsFolder, String codeFolder) {
		super(resolver);
		this.assetsFolder = assetsFolder;
		this.completeCodeFolder = assetsFolder + codeFolder;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SourceParameter parameter) {
		return null;
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, SourceParameter parameter) {
		// Do all the load synchronously
	}

	@Override
	public SourceSync loadSync(AssetManager manager, String fileName, FileHandle file, SourceParameter parameter) {
		String completePath = file.pathWithoutExtension();
		String folder = assetsFolder;
		if (completePath.contains(completeCodeFolder)) {
			folder = completeCodeFolder;
		}
		String path = file.pathWithoutExtension().substring(folder.length()).replace('/', '.');
		return new SourceSync(path, folder, folder);
	}
}
