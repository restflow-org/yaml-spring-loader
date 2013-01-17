package org.restflow.yaml.spring;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

/**
 * Factory bean to load a resource and return a String.  This class was written to enable the
 * InnerStringResourceFactoryBean to load resources during Yaml load time and directly inject
 * the contents of the resource into the bean as a String.
 * 
 * @author scottm
 *
 */
public class ResourceStringFactoryBean  implements FactoryBean<String>, ResourceLoaderAware {

	private String _resourcePath;
	private ResourceLoader _resourceLoader;
	
	public ResourceStringFactoryBean() {
		super();
	}

	public String getObject() throws Exception {
		String val = IOUtils.toString(_resourceLoader.getResource(_resourcePath).getInputStream(),"UTF-8");
		return val;
	}

	public String getResourcePath() {
		return _resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		_resourcePath = resourcePath;
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setResourceLoader( ResourceLoader resourceLoader)
			throws BeansException {
		
		_resourceLoader = resourceLoader;
	}




	

	
	
	
	
}
