package org.restflow.yaml.spring;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.restflow.yaml.spring.ConstructorArg;
import org.springframework.beans.factory.support.GenericBeanDefinition;


public class SpringBean {

	public String id;
	public String className;
	public String type = null;
	public String namespace;
	public Boolean singleton = null;
	public String parent = null;
	public Map<String, Object> properties;
	public Boolean abztract = false;
	public Boolean lazy =false;
	public String scope = GenericBeanDefinition.SCOPE_SINGLETON;
	private List<ConstructorArg> constructor = new Vector<ConstructorArg>();
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Map<String, Object> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public Boolean getSingleton() {
		return singleton;
	}
	public void setSingleton(Boolean singleton) {
		this.singleton = singleton;
	}
	public Boolean getAbztract() {
		return abztract;
	}
	public void setAbztract(Boolean abztract) {
		this.abztract = abztract;
	}
	public Boolean getLazy() {
		return lazy;
	}
	public void setLazy(Boolean lazy) {
		this.lazy = lazy;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public List<ConstructorArg> getConstructor() {
		return constructor;
	}
	public void setConstructor(List<ConstructorArg> constructor) {
		this.constructor = constructor;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	

	
	
	
}
