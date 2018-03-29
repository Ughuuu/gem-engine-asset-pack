package com.gemengine.system.common.dependency;

import com.gemengine.system.common.SystemBase;
import com.google.inject.Inject;

public class SystemWithUnresolvableDependency extends SystemBase {

	@Inject
	public SystemWithUnresolvableDependency(SystemWithObjectInConstructor systemWithObjectInConstructor) {
	}
}
