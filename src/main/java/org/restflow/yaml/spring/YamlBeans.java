package org.restflow.yaml.spring;

import java.util.LinkedList;
import java.util.List;

public class YamlBeans {

	private List<SpringBean> beans;
	private List<SpringBean> types;
	private List<String> imports = new LinkedList<String>();

	private Namespace namespace;
	
	public List<SpringBean> getBeans() {
		return beans;
	}
	public void setBeans(List<SpringBean> beans) {
		this.beans = beans;
	}

	public List<SpringBean> getComponents() {
		return beans;
	}
	public void setComponents(List<SpringBean> beans) {
		this.beans = beans;
	}

	public List<String> getImports() {
		return imports;
	}
	public void setImports(List<String> imports) {
		this.imports = imports;
	}
	public void setNamespace(Namespace namespace) {
		this.namespace = namespace;
	}
	
	public Namespace getNamespace() {
		return namespace;
	}
	
	public List<SpringBean> getTypes() {
		return types;
	}
	public void setTypes(List<SpringBean> types) {
		this.types = types;
	}

	public String calcNamespaceText() {
		if (namespace == null) return "";
		return namespace.calcNamespaceText();
	}
	
	
	
}
