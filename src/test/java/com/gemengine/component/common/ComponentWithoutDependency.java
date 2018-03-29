package com.gemengine.component.common;

public class ComponentWithoutDependency extends ComponentBase {

	@Override
	public ComponentBase getRoot() {
		return ComponentRootTest.getInstance();
	}
}
