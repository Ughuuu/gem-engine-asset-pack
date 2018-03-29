package com.gemengine.system.common;

import java.util.ArrayList;
import java.util.Collection;

import com.google.inject.AbstractModule;

public class SystemInjectionModule extends AbstractModule {
	private final Collection<Class<? extends SystemBase>> systemList;

	public SystemInjectionModule() {
		this(new ArrayList<>());
	}

	public SystemInjectionModule(Collection<Class<? extends SystemBase>> systemList) {
		this.systemList = systemList;
	}

	@Override
	protected void configure() {
		for (Class<? extends Object> systemClass : systemList) {
			bind(systemClass).asEagerSingleton();
		}
	}
}
