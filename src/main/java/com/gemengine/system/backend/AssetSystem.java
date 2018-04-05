package com.gemengine.system.backend;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.jsync.sync.Commiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.gemengine.system.backend.loaders.LoaderData;
import com.gemengine.system.common.TimedSystem;
import com.gemengine.system.helper.backend.AssetSystemHelper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Asset System, used by the engine to collect assets.
 * 
 * @author Dragos
 *
 */
public class AssetSystem extends TimedSystem {
	private static final String FILE_SEPARATOR = "/";
	private final AssetManager assetManager;
	public final String assetsFolder;
	private final Map<String, String> assetToFolder = new HashMap<>();
	private final Commiter commiter;
	private final Multimap<String, LoaderData<?>> extensionToLoaderMap = ArrayListMultimap.create();
	private final Multimap<String, String> folderToAsset = ArrayListMultimap.create();
	private final List<String> loadFolders = new ArrayList<>();
	private final Logger logger = LoggerFactory.getLogger(AssetSystem.class);
	private final boolean useBlockingLoad;
	private final boolean useExternalFiles;

	@Inject
	private AssetSystem(@Named("useExternalFiles") boolean useExternalFiles,
			@Named("useBlockingLoad") boolean useBlockingLoad, @Named("assetsFolder") String assetsFolder,
			@Named("gitBranch") String gitBranch, @Named("startFolder") String startFolder) {
		super(300, true, 0);
		this.assetsFolder = assetsFolder;
		this.useExternalFiles = useExternalFiles;
		if (useExternalFiles) {
			assetManager = new AssetManager(new InternalFileHandleResolver(), false);
		} else {
			assetManager = new AssetManager(new LocalFileHandleResolver(), false);
		}
		this.useBlockingLoad = useBlockingLoad;
		Commiter commiterHolder = null;
		try {
			commiterHolder = new Commiter(assetsFolder, gitBranch);
		} catch (Exception exception) {
			logger.error("Cannot create git client", exception);
		}
		this.commiter = commiterHolder;
		loadFolder(startFolder);
	}

	/**
	 * Add a loader for all the given extensions. This is when you want one type(ex
	 * Texture) to be loaded by one loader ONLY(ex TextureLoader)
	 * 
	 * @param loaderData
	 *            The loader data.
	 * @param folder
	 *            The folder that the loader loads from. This is the firstmost
	 *            folder, or null if none should be checked for.
	 * @param assetLoader
	 *            The asset loader
	 * @param extensions
	 *            The extensions that this loader should be used for. They must have
	 *            . in their name. ex ".png"
	 */
	public <T, P extends AssetLoaderParameters<T>> void addLoaderDefault(LoaderData<T> loaderData,
			AssetLoader<T, P> assetLoader, String... extensions) {
		assetManager.setLoader(loaderData.getType(), assetLoader);
		for (String extension : extensions) {
			extensionToLoaderMap.put(extension, loaderData);
		}
	}

	/**
	 * Add a loader for all the given extensions. This is when you want one type(ex
	 * Texture) to be loaded by one loader or MORE(ex TextureLoader)
	 * 
	 * @param loaderData
	 *            The loader data.
	 * @param folder
	 *            The folder that the loader loads from. This is the firstmost
	 *            folder, or null if none should be checked for.
	 * @param assetLoader
	 *            The asset loader
	 * @param extensions
	 *            The extensions that this loader should be used for. They must have
	 *            . in their name. ex ".png"
	 */
	public <T, P extends AssetLoaderParameters<T>> void addLoaderOverride(LoaderData<T> loaderData,
			AssetLoader<T, P> assetLoader, String... extensions) {
		for (String extension : extensions) {
			assetManager.setLoader(loaderData.getType(), extension, assetLoader);
			extensionToLoaderMap.put(extension, loaderData);
		}
	}

	private void findExternalChanges() throws GitAPIException, IOException {
		List<DiffEntry> entries = commiter.update();
		for (DiffEntry entry : entries) {
			String oldPath = assetsFolder + entry.getOldPath();
			String newPath = assetsFolder + entry.getNewPath();
			switch (entry.getChangeType()) {
			case DELETE:
				unplaceAsset(oldPath);
				break;
			case MODIFY:
			case RENAME:
				unplaceAsset(oldPath);
				break;
			default:
				break;
			}
			for (String loadFolder : loadFolders) {
				switch (entry.getChangeType()) {
				case ADD:
				case COPY:
					placeAsset(newPath, loadFolder);
					break;
				case MODIFY:
				case RENAME:
					placeAsset(newPath, loadFolder);
					break;
				default:
					break;
				}
			}
		}
	}

	private void findExternalFilesAtStart() {
		try {
			for (String path : commiter.getFiles()) {
				for (String loadFolder : loadFolders) {
					placeAsset(assetsFolder + path, loadFolder);
				}
			}
		} catch (Exception exception) {
			logger.error("Error finding external files at start", exception);
		}
	}

	private void findInternalFiles() {
		Queue<FileHandle> fileQueue = internalFilesHandleFrom(assetsFolder);
		FileHandle file;
		while ((file = fileQueue.poll()) != null) {
			if (file.isDirectory()) {
				fileQueue.addAll(internalFilesHandleFrom(file.path()));
			} else {
				String path = file.path();
				Collection<LoaderData<?>> type = extensionToLoaderMap.get(file.extension());
				if (type != null) {
					for (String loadFolder : loadFolders) {
						placeAsset(path, loadFolder);
					}
				}
			}
		}
	}

	/**
	 * Get all the assets of the given type.
	 * 
	 * @param type
	 *            The type of the asset
	 * @return The assets requested or null
	 */
	public <T> T[] getAll(Class<T> type) {
		Array<T> elements = new Array<>();
		assetManager.getAll(type, elements);
		return elements.toArray(type);
	}

	/**
	 * Get the asset from the given path.
	 * 
	 * @param path
	 *            The path of the asset
	 * @return The asset or null
	 */
	public <T> T getAsset(String path) {
		if (!isAssetLoaded(assetsFolder + path)) {
			return null;
		}
		return assetManager.get(assetsFolder + path);
	}

	/**
	 * Get the filename of a given asset
	 * 
	 * @param asset
	 *            The asset
	 * @return The filename or null
	 */
	public <T> String getAssetFileName(T asset) {
		return assetManager.getAssetFileName(asset);
	}

	/**
	 * Get all the names of the loaded assets.
	 * 
	 * @return Asset names
	 */
	public String[] getAssetNames() {
		return assetManager.getAssetNames().toArray(String.class);
	}

	/**
	 * Returns the {@link FileHandleResolver} for which this AssetManager was loaded
	 * with. This is used when creating loaders
	 * 
	 * @return the file handle resolver which this AssetManager uses
	 */
	public FileHandleResolver getFileHandleResolver() {
		return assetManager.getFileHandleResolver();
	}

	/**
	 * Get the loading progress.
	 * 
	 * @return The progress in percent of completion.
	 */
	public float getProgress() {
		return assetManager.getProgress();
	}

	private Deque<FileHandle> internalFilesHandleFrom(String path) {
		return new ArrayDeque<>(Arrays.asList(Gdx.files.internal(path).list()));
	}

	/**
	 * @param fileName
	 *            the file name of the asset
	 * @return whether the asset is loaded
	 */
	public boolean isAssetLoaded(String path) {
		return assetManager.isLoaded(path);
	}

	/**
	 * Get the loading progress.
	 * 
	 * @return Wether all assets are loaded or not.
	 */
	public boolean isLoaded() {
		return assetManager.update();
	}

	private void loadAllFirstTime() {
		if (useExternalFiles) {
			try {
				findExternalChanges();
				findExternalFilesAtStart();
			} catch (Exception exception) {
				logger.error("Cannot load assets", exception);
			}
		} else {
			findInternalFiles();
		}
		if (useBlockingLoad) {
			assetManager.finishLoading();
			if (!useExternalFiles) {
				setEnable(false);
			}
		}
	}

	private boolean loadAsset(String path) {
		String extension = AssetSystemHelper.getExtension(path);
		Collection<LoaderData<?>> types = extensionToLoaderMap.get(extension);
		if (types == null) {
			return false;
		}
		final String folder = AssetSystemHelper.getLastFolder(path, assetsFolder);
		LoaderData<?> defaultLoaderData = null;
		for (LoaderData<?> loaderData : types) {
			String loaderFolder = loaderData.getFolder();
			if (loaderFolder == null) {
				defaultLoaderData = loaderData;
				continue;
			}
			if (loaderData.getFolder().equals(folder)) {
				loaderData.load(assetManager, path);
				return true;
			}
		}
		if (defaultLoaderData != null) {
			defaultLoaderData.load(assetManager, path);
			return true;
		}
		return false;
	}

	/**
	 * Set a folder to be loaded with all the assets it contains.
	 * 
	 * @param folder
	 *            The folder path, ending with /
	 */
	public void loadFolder(String folder) {
		if (loadFolders.contains(folder)) {
			return;
		}
		setEnable(true);
		loadFolders.add(folder);
		if (folderToAsset.get(folder) == null) {
			return;
		}
		for (String path : folderToAsset.get(folder)) {
			loadAsset(path);
		}
		if (useBlockingLoad) {
			assetManager.finishLoading();
		}
	}

	@Override
	public void onUpdate(float delta) {
		if (useExternalFiles) {
			try {
				// always check file changes
				findExternalChanges();
				if (useBlockingLoad) {
					assetManager.finishLoading();
				}
			} catch (Exception exception) {
				logger.error("Error while checking for file changes", exception);
			}
		} else {
			if (assetManager.update()) {
				setEnable(false);
			}
		}
		if (!useBlockingLoad) {
			assetManager.update();
		}
	}

	private boolean placeAsset(String path, String loadFolder) {
		String folder = assetToFolder.get(path);
		if (folder == null) {
			int folderPos = path.lastIndexOf(FILE_SEPARATOR);
			folder = path.substring(0, folderPos + 1);
			assetToFolder.put(path, folder);
			StringBuilder sb = new StringBuilder();
			for (String subFolder : folder.split(FILE_SEPARATOR)) {
				sb.append(subFolder);
				sb.append(FILE_SEPARATOR);
				String subfolderFullPath = sb.toString();
				Collection<String> filesInFolder = folderToAsset.get(subfolderFullPath);
				filesInFolder.add(path);
			}
		}
		if (loadFolder == null || folder.indexOf(loadFolder) == -1) {
			return false;
		}
		return loadAsset(path);
	}

	private void unloadAsset(String path) {
		if (isAssetLoaded(path)) {
			assetManager.unload(path);
		}
	}

	/**
	 * Unload all the assets
	 */
	public void unloadAssets() {
		assetManager.dispose();
	}

	/**
	 * Unload a folder, with all folders and assets it has.
	 * 
	 * @param folder
	 *            The folder path, ending with /
	 */
	public void unloadFolder(String folder) {
		if (!loadFolders.contains(folder)) {
			return;
		}
		loadFolders.remove(folder);
		if (folderToAsset.get(folder) == null) {
			return;
		}
		for (String path : folderToAsset.get(folder)) {
			unloadAsset(path);
		}
		if (useBlockingLoad) {
			assetManager.finishLoading();
		}
	}

	private void unplaceAsset(String path) {
		String folder = assetToFolder.remove(path);
		if (folder == null) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (String subFolder : folder.split(FILE_SEPARATOR)) {
			sb.append(subFolder);
			sb.append(FILE_SEPARATOR);
			String subfolderFullPath = sb.toString();
			Collection<String> filesInFolder = folderToAsset.get(subfolderFullPath);
			filesInFolder.remove(path);
		}
		unloadAsset(path);
	}
}
