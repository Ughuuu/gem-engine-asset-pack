package com.gemengine.system.helper.backend;

/**
 * Helper class used in asset system.
 * 
 * @author Dragos
 *
 */
public class AssetSystemHelper {
	/**
	 * Get the extension of a path.
	 * 
	 * @param path
	 *            Path to get extension from
	 * @return The extension of the path, or empty string.
	 */
	public static String getExtension(String path) {
		int extensionStart = path.lastIndexOf('.');
		if (extensionStart == -1)
			return "";
		return path.substring(extensionStart);
	}

	/**
	 * Get the folder of this asset without the first folder.
	 * 
	 * @param path
	 *            The path of the file
	 * @param fileSeparator
	 *            The file separator
	 * @return The path
	 */
	public static String getLastFolder(String path, String fileSeparator) {
		final int folderPosLast = path.lastIndexOf(fileSeparator);
		final int folderPosFirst = path.indexOf(fileSeparator);
		return path.substring(folderPosFirst + 1, folderPosLast + 1);
	}

	/**
	 * Get the path without the extension
	 * 
	 * @param path
	 *            Path to get path without extension from
	 * @return The path without extension or same path if no extension is found.
	 */
	public static String getWithoutExtension(String path) {
		int extensionStart = path.lastIndexOf('.');
		if (extensionStart == -1)
			return "";
		return path.substring(0, extensionStart);
	}

	private AssetSystemHelper() {
	}
}
