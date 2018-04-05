package com.gemengine.system.backend;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.gemengine.system.backend.AssetSystem;
import com.gemengine.system.backend.loader.TextLoaderSystem;
import com.gemengine.system.common.SystemManager;

public class AssetSystemTest {
	public String assetsFolder;
	
	public boolean deleteFolder(String file) {
		File index = new File(file);
		String[]entries = index.list();
		for(String s: entries){
		    File currentFile = new File(index.getPath(),s);
		    if(!currentFile.delete()) {
		    	return false;
		    }
		}
		return true;
	}
	
	public void createDummyFile(String name) throws FileNotFoundException, UnsupportedEncodingException {
		File dir = new File(assetsFolder);
		dir.mkdir();
		PrintWriter writer = new PrintWriter(assetsFolder + "/"+name + ".txt", "UTF-8");
		writer.println("test");
		writer.close();
	}
	
	@Test
	public void checkAssetSystemStarts() throws FileNotFoundException, UnsupportedEncodingException {
		assetsFolder= "bin/" + UUID.randomUUID().toString();
		createDummyFile("test");
		SystemManager systemManager = new SystemManager();
		systemManager.putSystemType(AssetTesterSystem.class);
		systemManager.putSystemType(AssetSystem.class);
		systemManager.putSystemType(TextLoaderSystem.class);
		systemManager.putNamedProperty("useBlockingLoad", true);
		systemManager.putNamedProperty("useExternalFiles", true);
		systemManager.putNamedProperty("assetsFolder", assetsFolder);
		systemManager.putNamedProperty("gitBranch", "master");
		systemManager.putNamedProperty("startFolder", assetsFolder);
		systemManager.instantiateSystems();

		AssetTesterSystem assetTesterSystem = (AssetTesterSystem) systemManager.getSystems().stream()
				.filter((system) -> system.getClass() == AssetTesterSystem.class).findFirst().get();
		assetTesterSystem.doTestSequence();
	}

}
