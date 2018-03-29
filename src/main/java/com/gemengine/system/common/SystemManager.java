package com.gemengine.system.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * 
 * 
 * @author Dragos
 *
 */
public class SystemManager {
	private Injector injector = Guice.createInjector(new SystemInjectionModule());
	private final Logger logger = LoggerFactory.getLogger(SystemManager.class);
	private final List<SystemBase> systemArray = new ArrayList<>();
	private final Set<Class<? extends SystemBase>> systemTypesSet = new HashSet<>();

	public <T> T createComponent(Class<T> componentType) {
		return injector.getInstance(componentType);
	}

	public List<SystemBase> getSystems() {
		return Collections.unmodifiableList(systemArray);
	}

	public void instantiateSystems() {
		try {
			injector = Guice.createInjector(new SystemInjectionModule(systemTypesSet));
		} catch (Exception exception) {
			logger.error("The injector failed to create", exception);
			return;
		}
		systemArray.clear();
		final Map<Class<?>, SystemBase> systemMap = new HashMap<>();
		for (Class<? extends SystemBase> systemType : systemTypesSet) {
			systemArray.add(injector.getInstance(systemType));
		}
		systemArray.addAll(systemMap.values());
		Collections.sort(systemArray);
	}

	public <T extends SystemBase> void putSystemType(Class<T> systemType) {
		systemTypesSet.add(systemType);
	}

	public <T extends SystemBase> void removeSystemType(Class<T> systemType) {
		systemTypesSet.remove(systemType);
	}
}
