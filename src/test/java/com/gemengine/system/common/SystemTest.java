package com.gemengine.system.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SystemTest {
	@Test
	public void checkSystemEnableState() {
		SystemManager systemManager = new SystemManager();
		systemManager.putSystemType(SystemWithPriorityZero.class);
		systemManager.instantiateSystems();

		SystemBase system = systemManager.getSystems().get(0);

		assertTrue("The system should be enabled", system.isEnable());

		system.setEnable(false);

		assertFalse("The system should be enabled", system.isEnable());
	}

	@Test
	public void checkSystemsAreOrdered() {
		SystemBase systemWithPriorityZero = new SystemWithPriorityZero();
		SystemBase systemWithPriorityOne = new SystemWithPriorityOne();
		SystemBase systemWithPriorityOneAlso = new SystemWithPriorityOneAlso();

		assertEquals("The system with priority one is first", 1,
				Integer.signum(systemWithPriorityOne.compareTo(systemWithPriorityZero)));

		assertEquals("The system with priority zero is last", -1,
				Integer.signum(systemWithPriorityZero.compareTo(systemWithPriorityOne)));

		assertEquals("The system with same priority but lexicographically greater name is first", 1,
				Integer.signum(systemWithPriorityOneAlso.compareTo(systemWithPriorityOne)));
	}
}