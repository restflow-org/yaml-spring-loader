package org.restflow.yaml.spring;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.ManagedMap;
/**
 * Inner bean wrapping the ResourceStringFactoryBean, (which loads a resource and returns a String).
 * This class is meant to be used with Yamls tag capabilities (i.e. !resource) allowing resources to
 * be loaded and injected directly into the String properties of a bean.  The BeanClassName is a hardcoded
 * with the name of the factory for doing the loading and conversion to String.
 * 
 * @author scottm
 *
 */
public class InnerStringResourceFactoryBean implements BeanDefinition {

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
	private ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
	
	public InnerStringResourceFactoryBean (String resourceName) {
		properties = new ManagedMap<String, Object>();
		properties.put("resourcePath", resourceName);
	}
	
	public String getClassName() {
		//anonymous inner bean. named unused
		return "ResourceStringFactoryBean";
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

	
	@Override
	public String getBeanClassName() {
		return "org.restflow.yaml.spring.ResourceStringFactoryBean";
	}
	@Override
	public String getParentName() {
		if (type != null) return type;
		if (parent != null) return parent;
		return null;
	}
	@Override
	public MutablePropertyValues getPropertyValues() {
		return new MutablePropertyValues(properties);
	}
	@Override
	public boolean isAbstract() {
		return false;
	}
	@Override
	public boolean isPrototype() {
		return true;
	}
	@Override
	public boolean isSingleton() {
		return false;
	}
	@Override
	public void setParentName(String parent) {
		setParent(parent);
	}
	@Override
	public boolean isLazyInit() {
		return false;
	}

	@Override
	public ConstructorArgumentValues getConstructorArgumentValues() {
		return constructorArgumentValues;
	}
	@Override
	public String[] getDependsOn() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getFactoryBeanName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getFactoryMethodName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public BeanDefinition getOriginatingBeanDefinition() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getResourceDescription() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getRole() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean isAutowireCandidate() {
		return false;
	}
	@Override
	public boolean isPrimary() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void setAutowireCandidate(boolean arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setBeanClassName(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setDependsOn(String[] arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setFactoryBeanName(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setFactoryMethodName(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setLazyInit(boolean arg0) {
		
	}
	
	@Override
	public void setPrimary(boolean arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String[] attributeNames() {
		// TODO Auto-generated method stub
		if (properties == null ) return new String[0];
		Set<String> propNames = properties.keySet();
		return propNames.toArray(new String[propNames.size()]);
	}
	@Override
	public Object getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean hasAttribute(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Object removeAttribute(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setAttribute(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Object getSource() {
		// TODO Auto-generated method stub
		return null;
	}

		
	
	
}
