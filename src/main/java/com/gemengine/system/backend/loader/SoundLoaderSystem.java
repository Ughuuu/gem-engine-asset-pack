package com.gemengine.system.backend.loader;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.gemengine.system.backend.AssetSystem;
import com.gemengine.system.backend.loaders.LoaderData;
import com.gemengine.system.common.SystemBase;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class SoundLoaderSystem extends SystemBase {
	@Inject
	private SoundLoaderSystem(AssetSystem assetSystem, @Named("musicFolder") String musicFolder,
			@Named("soundFolder") String soundFolder) {
		final FileHandleResolver resolver = assetSystem.getFileHandleResolver();
		assetSystem.addLoaderDefault(new LoaderData<Music>(Music.class, musicFolder), new MusicLoader(resolver), ".wav",
				".mp3", ".ogg");
		assetSystem.addLoaderDefault(new LoaderData<Sound>(Sound.class, soundFolder), new SoundLoader(resolver), ".wav",
				".mp3", ".ogg");
	}
}
