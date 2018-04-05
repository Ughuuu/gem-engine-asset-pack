package com.gemengine.system.backend.loader;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.gemengine.system.backend.AssetSystem;
import com.gemengine.system.backend.loaders.LoaderData;
import com.gemengine.system.common.SystemBase;
import com.google.inject.Inject;

public class ParticleLoaderSystem extends SystemBase {
	@Inject
	private ParticleLoaderSystem(AssetSystem assetSystem) {
		final FileHandleResolver resolver = assetSystem.getFileHandleResolver();
		assetSystem.addLoaderDefault(new LoaderData<ParticleEffect>(ParticleEffect.class),
				new ParticleEffectLoader(resolver), ".2dparticle");
		assetSystem.addLoaderDefault(
				new LoaderData<com.badlogic.gdx.graphics.g3d.particles.ParticleEffect>(
						com.badlogic.gdx.graphics.g3d.particles.ParticleEffect.class),
				new com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader(resolver), ".3dparticle");
	}
}
