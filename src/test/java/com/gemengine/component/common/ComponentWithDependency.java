package com.gemengine.component.common;

import com.google.inject.Inject;

public class ComponentWithDependency extends ComponentBase {
	private final SystemExample systemExample;

	@Inject
	public ComponentWithDependency(SystemExample systemExample) {
		this.systemExample = systemExample;
	}

	@Override
	public ComponentBase getRoot() {
		return ComponentRootTest.getInstance();
	}

	public SystemExample getSystemExample() {
		return systemExample;
	}
}
