package com.gemengine.component.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gemengine.system.common.SystemManager;

public class ComponentTest {
	@Test
	public void checkComponentProperties() {
		SystemManager systemManager = new SystemManager();
		ComponentWithoutDependency componentWithDependency = systemManager
				.createComponent(ComponentWithoutDependency.class);
		assertTrue("Check enable state", componentWithDependency.isEnable());
		componentWithDependency.setEnable(false);
		assertFalse("Check enable state", componentWithDependency.isEnable());
		assertNull("Check getting an unexisting component gives null",
				ComponentRootTest.getInstance().getChild(ComponentWithDependency.class));
		componentWithDependency.remove();
		 assertEquals("The root component has no children",
				 0,
				 ComponentRootTest.getInstance().getChildren().size());
	}

	@Test
	public void rootComponentTest() {
		SystemManager systemManager = new SystemManager();
		ComponentWithoutDependency child = systemManager.createComponent(ComponentWithoutDependency.class);
		ComponentWithoutDependency parent = systemManager.createComponent(ComponentWithoutDependency.class);
		 assertEquals("Child is connected to root component",
		 ComponentRootTest.getInstance(), child.getParent());
		 assertEquals("Parent is connected to root component",
				 ComponentRootTest.getInstance(), parent.getParent());
		 assertTrue("The root component has as child the Parent component",
				 ComponentRootTest.getInstance().getChildren(ComponentWithoutDependency.class).contains(child));
		 assertTrue("The root component has as child the Parent component",
				 ComponentRootTest.getInstance().getChildren(ComponentWithoutDependency.class).contains(parent));
		 child.remove();
		 parent.remove();
		 assertEquals("The root component has no children",
				 0,
				 ComponentRootTest.getInstance().getChildren().size());
	}

	@Test
	public void componentRemoveChildrenTest() {
		SystemManager systemManager = new SystemManager();
		ComponentWithoutDependency child = systemManager.createComponent(ComponentWithoutDependency.class);
		ComponentWithoutDependency parent = systemManager.createComponent(ComponentWithoutDependency.class);
		 // parent
		 child.setParent(parent);
		 assertEquals("The root component has as child the Parent component",
				 parent,
				 ComponentRootTest.getInstance().getChild(ComponentWithoutDependency.class));
		 assertEquals("The Parent component has as child the Child component",
				 child,
				 parent.getChild(ComponentWithoutDependency.class));
		 child.setParent(null);
		 assertEquals("The parent is removed and the child is connected to root component",
				 ComponentRootTest.getInstance(),
				 child.getParent());
		 // parent
		 child.setParent(parent);
		 parent.removeChild(child);
		 assertEquals("The parent is removed",
				 0,
				 parent.getChildren().size());
		 // parent
		 child.setParent(parent);
		 parent.removeChildren(ComponentWithoutDependency.class);
		 assertEquals("The parent is removed",
				 0,
				 parent.getChildren().size());
		 // parent
		 child.setParent(parent);
		 child.removeParent();
		 assertEquals("The parent is removed",
				 0,
				 parent.getChildren().size());
		 child.setParent(parent);
		 parent.remove();
		 child.remove();
		 assertEquals("The root component has no children",
				 0,
				 ComponentRootTest.getInstance().getChildren().size());
	}
	
	@Test
	public void createComponentWithDependency() {
		SystemManager systemManager = new SystemManager();
		systemManager.putSystemType(SystemExample.class);
		systemManager.instantiateSystems();

		SystemExample systemExample = (SystemExample) systemManager.getSystems().get(0);

		ComponentWithDependency componentWithDependency = systemManager.createComponent(ComponentWithDependency.class);

		assertEquals("Check component injected correctly", systemExample, componentWithDependency.getSystemExample());
		
		componentWithDependency.remove();
		 assertEquals("The root component has no children",
				 0,
				 ComponentRootTest.getInstance().getChildren().size());
	}
}
