package com.gemengine.system.common.dependency;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.gemengine.system.common.SystemManager;

public class SystemManagingTest {
	@Test
	public void addSystemWithDependencies() {
		SystemManager systemManager = new SystemManager();
		systemManager.putSystemType(SystemWithDependency.class);
		systemManager.putSystemType(SystemWithoutDependency.class);
		systemManager.instantiateSystems();
		assertEquals("Detect the systems are created", 2, systemManager.getSystems().size());
		SystemWithDependency systemWithDependency = (SystemWithDependency) systemManager.getSystems().stream()
				.filter((system) -> system.getClass() == SystemWithDependency.class).findFirst().get();
		SystemWithoutDependency systemWithoutDependency = (SystemWithoutDependency) systemManager.getSystems().stream()
				.filter((system) -> system.getClass() == SystemWithoutDependency.class).findFirst().get();
		assertEquals("Check that the given dependency is a singleton", systemWithoutDependency,
				systemWithDependency.getSystemDependency());
	}

	@Test
	public void addSystemWithoutDependencies() {
		SystemManager systemManager = new SystemManager();
		systemManager.putSystemType(SystemWithoutDependency.class);
		systemManager.instantiateSystems();
		assertEquals("Detect the systems are created", 1, systemManager.getSystems().size());
		assertEquals("Detect the systems are the expected ones", SystemWithoutDependency.class,
				systemManager.getSystems().get(0).getClass());
	}

	@Test
	public void addSystemWithUnresolvableDependency() {
		SystemManager systemManager = new SystemManager();
		systemManager.putSystemType(SystemWithUnresolvableDependency.class);
		systemManager.instantiateSystems();
		assertEquals("Detect no system is created", 0, systemManager.getSystems().size());
	}

	@Test
	public void removeSystem() {
		SystemManager systemManager = new SystemManager();
		systemManager.putSystemType(SystemWithoutDependency.class);
		systemManager.instantiateSystems();
		assertEquals("Detect the systems are created", 1, systemManager.getSystems().size());
		systemManager.removeSystemType(SystemWithoutDependency.class);
		systemManager.instantiateSystems();
		assertEquals("Detect there are no systems", 0, systemManager.getSystems().size());
	}
}