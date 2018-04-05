package com.gemengine.system.backend.loaders;

import org.jsync.sync.ClassSync;

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
public class CodeLoader<T> extends AsynchronousAssetLoader<ClassSync, CodeLoader.CodeParameter> {
	public static class CodeParameter extends AssetLoaderParameters<ClassSync> {
		private final ClassLoader classLoader;

		public CodeParameter(ClassLoader classLoader) {
			this.classLoader = classLoader;
		}
	}

	private final String assetsFolder;
	private final String completeCodeFolder;

	public CodeLoader(FileHandleResolver resolver, String assetsFolder, String codeFolder) {
		super(resolver);
		this.assetsFolder = assetsFolder;
		this.completeCodeFolder = assetsFolder + codeFolder;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, CodeParameter parameter) {
		return null;
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, CodeParameter parameter) {
		// Do all the load synchronously
	}

	@Override
	public ClassSync<T> loadSync(AssetManager manager, String fileName, FileHandle file, CodeParameter parameter) {
		String completePath = file.pathWithoutExtension();
		String folder = assetsFolder;
		if (completePath.contains(completeCodeFolder)) {
			folder = completeCodeFolder;
		}
		String path = completePath.substring(folder.length()).replace('/', '.');
		return new ClassSync<>(parameter.classLoader, path, folder);
	}
}
