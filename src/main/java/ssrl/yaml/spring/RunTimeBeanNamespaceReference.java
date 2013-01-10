package ssrl.yaml.spring;

import org.springframework.beans.factory.config.RuntimeBeanReference;;

/**
 * @author scottm
 * 
 * Place for the yaml file to reference a local bean automatically qualified by Namespace.
 * 
 * Probably doesn't need to extend the RuntimeBeanReference.
 */
public class RunTimeBeanNamespaceReference extends RuntimeBeanReference {

	String namespace;

	public RunTimeBeanNamespaceReference(String arg0) {
		super(arg0);
	}

	@Override
	public String getBeanName() {
		if ( getNamespace() == null || getNamespace().length()==0) return super.getBeanName();
		
		return getNamespace()+"::" + super.getBeanName();
	}


	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	

	
}
