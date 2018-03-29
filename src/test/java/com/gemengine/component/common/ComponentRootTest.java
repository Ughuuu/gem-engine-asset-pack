package com.gemengine.component.common;

public class ComponentRootTest extends ComponentBase {
	private static ComponentRootTest instance = null;
	
	public static ComponentRootTest getInstance() {
		if (instance == null) {
			instance = new ComponentRootTest();
		}
		return instance;
	}

	private ComponentRootTest() {
	}

	@Override
	public ComponentBase getRoot() {
		return this;
	}
}