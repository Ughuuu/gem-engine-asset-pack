package com.gemengine.system.common.dependency;

import com.gemengine.system.common.SystemBase;
import com.google.inject.Inject;

public class SystemWithDependency extends SystemBase {
	private final SystemWithoutDependency systemDependency;

	@Inject
	public SystemWithDependency(SystemWithoutDependency systemDependency) {
		this.systemDependency = systemDependency;
	}

	public SystemWithoutDependency getSystemDependency() {
		return systemDependency;
	}
}
