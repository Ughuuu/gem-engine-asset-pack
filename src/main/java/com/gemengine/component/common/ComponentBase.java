package com.gemengine.component.common;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

/**
 * The basic ComponentBase type. This needs to be extended by components that
 * will be used in the engine. Components hold data only, and the logic is
 * performed by {@link com.gemengine.system.base.SystemBase} or others that
 * extend that. If you need to do logic in the component,
 * {@link com.google.inject.Inject} the component with a system.
 *
 */
public abstract class ComponentBase {
	private final SetMultimap<Class<? extends ComponentBase>, ComponentBase> children = HashMultimap.create();
	private boolean enable;
	private ComponentBase parent;

	public ComponentBase() {
		this(true);
	}

	public ComponentBase(boolean enable) {
		this.enable = enable;
		parent = getRoot();
		if(getRoot() != this) {
			getRoot().children.put(this.getClass(), this);
		}
	}

	public void addChild(ComponentBase child) {
		child.parent.children.remove(child.getClass(), child);
		children.put(child.getClass(), child);
		child.parent = this;
	}

	public ComponentBase getChild(Class<? extends ComponentBase> childType) {
		Iterator<ComponentBase> componentIterator = children.get(childType).iterator();
		if (componentIterator.hasNext()) {
			return componentIterator.next();
		}
		return null;
	}

	public Collection<ComponentBase> getChildren() {
		return children.values();
	}

	public Set<ComponentBase> getChildren(Class<? extends ComponentBase> childType) {
		return children.get(childType);
	}

	public ComponentBase getParent() {
		return parent;
	}

	public abstract ComponentBase getRoot();

	public boolean isEnable() {
		return enable;
	}

	public void removeChild(ComponentBase child) {
		children.remove(child.getClass(), child);
		child.setParent(getRoot());
	}

	public void removeChildren(Class<? extends ComponentBase> childType) {
		Set<ComponentBase> typeChildren = children.get(childType);
		children.remove(childType, children);
		typeChildren.stream().forEach((child) -> child.setParent(getRoot()));
	}

	public void removeParent() {
		parent.removeChild(this);
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setParent(ComponentBase parent) {
		if (parent == null) {
			getRoot().addChild(this);
		} else {
			parent.addChild(this);
		}
	}
	
	public void remove() {
		parent.children.remove(this.getClass(), this);
		getChildren().stream().forEach(component -> component.parent = getRoot());
	}
}
