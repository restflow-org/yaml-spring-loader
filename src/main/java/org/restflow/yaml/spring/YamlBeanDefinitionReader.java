package org.restflow.yaml.spring;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.parser.ParserException;
import org.yaml.snakeyaml.scanner.ScannerException;

public class YamlBeanDefinitionReader extends AbstractBeanDefinitionReader {

	private String defaultParentBean;
//	private String resourceDescription;
	private List<String> importedFiles;
	static String NAMESPACE_DELIMETER= "."; 
	private ApplicationContext _applicationContext;
	private Map<String,String> importMappings;
	final Constructor _constructor;
	
	public YamlBeanDefinitionReader(ApplicationContext applicationContext) throws Exception {
		
		super((BeanDefinitionRegistry)applicationContext);
		
		_applicationContext=applicationContext;
		
		importedFiles = new Vector<String>();
		
		_constructor = new CustomConstructor("org.restflow.yaml.spring.YamlBeans");		
		addBasicConstructorTypes();		
	}

	public YamlBeanDefinitionReader(ApplicationContext applicationContext, Constructor constructor) {
		
		super((BeanDefinitionRegistry)applicationContext);
		
		_applicationContext=applicationContext;
		
		importedFiles = new Vector<String>();

		_constructor = constructor;
		addBasicConstructorTypes();
	}

	private void addBasicConstructorTypes () {
		_constructor.addTypeDescription(new TypeDescription(
			RuntimeBeanReference.class, "!ref"));
		_constructor.addTypeDescription(new TypeDescription(
			RunTimeBeanNamespaceReference.class, "!lref"));
		_constructor.addTypeDescription(new TypeDescription(
			InnerSpringBean.class, "!bean"));
		_constructor.addTypeDescription(new TypeDescription(
			InnerSpringBean.class, "!inline"));
		_constructor.addTypeDescription(new TypeDescription(
			InnerStringResourceFactoryBean.class, "!resource"));
	}

	private void registerSingleYamlBean(String resourceName, SpringBean bean) {
		String qualifiedId = bean.getId();
		if ( bean.getNamespace() != null && bean.getNamespace().length() != 0) {
			qualifiedId = bean.getNamespace() + NAMESPACE_DELIMETER + bean.getId();
			resolveQualifiedReferences(bean.getNamespace(),bean);
		}
		
		// Just use default parent if we're not dealing with the parent itself,
		// and if there's no class name specified. The latter has to happen for
		// backwards compatibility reasons.

		String parent = null;
		if ( bean.getParent() != null && bean.getType() != null) {
			throw new BeanCreationException( resourceName, bean.getId(), bean.getClassName(),new Exception("cannot set parent and type simultaneously."));
		}
		if ( bean.getParent() != null) {
			parent = bean.getParent();
		} else if (bean.getType() != null) {
			parent = bean.getType();
		}
		
		if ( parent == null && bean.getClassName() == null && !bean.getId().equals(this.defaultParentBean)) {
			parent = this.defaultParentBean;
		}

		try {
			AbstractBeanDefinition bd = BeanDefinitionReaderUtils.createBeanDefinition(
					parent, bean.getClassName(), getBeanClassLoader());

			if ( bean.getSingleton() != null) {
				String scope = bean.getSingleton() ? GenericBeanDefinition.SCOPE_SINGLETON :
					GenericBeanDefinition.SCOPE_PROTOTYPE;
				bd.setScope( scope );
			}
			
			
			bd.setAbstract(bean.getAbztract());
			bd.setLazyInit( bean.getLazy() );
			
			
			bd.setConstructorArgumentValues(generateConstructorArgs(bean.getConstructor()));
			bd.setPropertyValues(new MutablePropertyValues (bean.getProperties()) );
			
			getRegistry().registerBeanDefinition( qualifiedId, bd);					

		}
		catch (ClassNotFoundException ex) {
			throw new CannotLoadBeanClassException( resourceName, bean.getId(), bean.getClassName(), ex);
		}
		catch (LinkageError err) {
			throw new CannotLoadBeanClassException( resourceName , bean.getId(), bean.getClassName(), err);
		}
	}
	
	private ConstructorArgumentValues generateConstructorArgs(List<ConstructorArg> constructor) {
		ConstructorArgumentValues cas = new ConstructorArgumentValues();
		
		int cnt =0;
		int index =0;
		
		for (ConstructorArg ca : constructor ) {
		
		    if (ca.getIndex() != null) {
		    	index = ca.getIndex();
		    } else {
		    	index = cnt;
		    }
		
			if (ca.getType() != null ) {
				cas.addIndexedArgumentValue(index, ca.getValue(), ca.getType());
			} else {
				cas.addIndexedArgumentValue( index, ca.getValue());
			}
			cnt++;
		}
		return cas;
	}
	
	public int loadBeanDefinitions(Resource resource)
			throws BeanDefinitionStoreException {
		// setResourceDescription( file.getName() );
		InputStream input;
		try {
			input = resource.getInputStream();

			return loadBeanDefinitions(input, resource.getFilename());
		} catch (ScannerException e) {
			throw new BeanCreationException(buildReadableError(e.getMessage(), e.getProblemMark(), resource))  ;
		} catch (ParserException e) {
			throw new BeanCreationException(buildReadableError(e.getMessage(), e.getProblemMark(), resource))  ;			
		} catch (YAMLException e) {
			if (e.getMessage().contains("Cannot create property=imports") && e.getMessage().contains("No single argument constructor found for interface java.util.List")) {
				throw new BeanCreationException ("Problem in imports declaration.  List resources with '- '");
			} else if (e.getMessage().contains("Cannot create property=") && e.getMessage().contains( "Unable to find property ") ) {
				String className = e.getMessage().substring(e.getMessage().lastIndexOf("class: ")+7);
				
				String err = traverseYamlException(e.getMessage(),resource);				
				throw new BeanCreationException (err + " Property should be one of " + listSetters(className)); 
			} else {
				String err = traverseYamlException(e.getMessage(),resource);
				throw new BeanCreationException (err.toString()); 
			}
		} catch (Exception e) {
			throw new BeanDefinitionStoreException(resource.getDescription() + "\n" + e.getMessage());
		}
	}
	
	static public String traverseYamlException(String message, Resource resource) {
		StringBuffer buf = new StringBuffer();

		String lastException = message.substring(message.lastIndexOf(";") + 1);
		String trimException = lastException.replaceFirst("exception=","");

		
		buf.append(trimException);
		buf.append( " in yaml resource: ");
		buf.append(resource.getFilename());
		buf.append("\n");

		String[] errors = message.split("Cannot create property=");
		
		buf.append("Error found while traversing yaml structure:\n");
		
		for (int index=1; index < errors.length ; index= index+1) {
			buf.append(StringUtils.repeat(" ",index * 2));				
			
			String property=errors[index];
			buf.append(property.split(" ")[0]);
			buf.append(":\n");
		}
		

		return buf.toString();
	}
	
	
	static public String buildReadableError( String message, Mark problemMark, Resource resource ) {
		StringBuffer err = yamlResourceError(resource);			
		
		if ( message.contains("\\t") ) {
			err.append("Tab in indentation at line ");
		} else if (message.contains("block mapping") || (message.contains("mapping values are not allowed here" )) ) {
			err.append("Indentation unaligned near line ");
		} else {
			err.append( message );
			err.append( " at " );
		}
		err.append(problemMark.getLine());
		err.append(", column ");
		err.append(problemMark.getColumn());
		err.append("\n");						
		err.append(problemMark.get_snippet());
		
	
		return err.toString();
	}


	private static StringBuffer yamlResourceError(Resource resource) {
		StringBuffer err = new StringBuffer("Syntax error in yaml resource: ");
		err.append(resource.getFilename());
		err.append("\n");
		return err;
	}
		
	public int loadBeanDefinitions(InputStream input, String resourceName) throws Exception {

		logger.info("loading bean definitions from: " + resourceName);

		Yaml yaml = new Yaml(_constructor);

		YamlBeans beans = (YamlBeans) yaml.load(input);

		if (beans.getImports() != null) {
			for (String importRes : beans.getImports()) {

				ExpressionParser parser = new SpelExpressionParser();

				StandardEvaluationContext context = new StandardEvaluationContext(
						getImportMappings());

				String importString = parser.parseExpression(importRes,
						new TemplateParserContext()).getValue(context,
						String.class);
				registerBeanDefinitions(importString);
			}
		}

		String namespaceStr = beans.calcNamespaceText();
		
		if (beans.getTypes() != null) {
			for (SpringBean bean : beans.getTypes()) {
				bean.setNamespace(namespaceStr);
				bean.setAbztract(true);
				registerSingleYamlBean(resourceName, bean);
			}
		}

		if (beans.getBeans() != null) {
			for (SpringBean bean : beans.getBeans()) {
				bean.setNamespace(namespaceStr);
				registerSingleYamlBean(resourceName, bean);
			}
		}

		return 0;
	}
	
	public void registerBeanDefinitions(String resourceName) throws Exception {

		//guard against duplicate imports
		if ( importedFiles.contains(resourceName)) return;
		importedFiles.add(resourceName);
		
		
		loadBeanDefinitions(_applicationContext.getResource(resourceName));

	}


	private void resolveQualifiedReferences(String namespace, SpringBean bean) {
		resolveQualifiedReferencesInMap(namespace,bean.getProperties());
	}
	
	private void resolveQualifiedReferencesInMap(String namespace, Map map) {
		
		if (map == null) return;
		
		for (Object key : map.keySet() ) {
			Object prop = map.get(key);
			
			if ( prop instanceof RunTimeBeanNamespaceReference ) {
				RunTimeBeanNamespaceReference qual =  (RunTimeBeanNamespaceReference)prop;
				map.put(key,new RuntimeBeanReference(namespace + NAMESPACE_DELIMETER + qual.getBeanName()));
			}
			
			if ( prop instanceof ManagedMap ) {
				resolveQualifiedReferencesInMap(namespace, (ManagedMap)prop);
			}

			if ( prop instanceof ManagedList ) {
				resolveQualifiedReferencesInList(namespace, (ManagedList)prop);
			}
		}
	}
		
	private void resolveQualifiedReferencesInList(String namespace, ManagedList list) {
		for (Object prop: list) {
			if ( prop instanceof RunTimeBeanNamespaceReference ) {
				RunTimeBeanNamespaceReference qual =  (RunTimeBeanNamespaceReference)prop;
				list.set(list.indexOf(prop), new RuntimeBeanReference(namespace + NAMESPACE_DELIMETER + qual.getBeanName()));
			}
			
			if ( prop instanceof ManagedMap ) {
				resolveQualifiedReferencesInMap(namespace, (ManagedMap)prop);
			}

			if ( prop instanceof ManagedList ) {
				resolveQualifiedReferencesInMap(namespace, (ManagedMap)prop);
			}
			
		}
	}
	

	
	public static class CustomConstructor extends Constructor {
		
		public CustomConstructor(String className) throws ClassNotFoundException {
			super(className);
			
/*            this.yamlConstructors.put(new Tag("!bean"), new ConstructBean());*/
			
		}

		@Override
		protected Map<Object, Object> createDefaultMap() {
			return new ManagedMap<Object, Object>();
		}

		@Override
		protected List<Object> createDefaultList(int arg0) {
			return new ManagedList<Object>();
		}
		
/*		private class ConstructBean extends AbstractConstruct {
            public Object construct(Node node) {
            	node.get
            	return new InnerSpringBean();
            }
		}*/
		
	}
	
	/**
	 * Set the default parent bean for this bean factory. If a child bean
	 * definition handled by this factory provides neither a parent nor a class
	 * attribute, this default value gets used.
	 * <p>
	 * Can be used e.g. for view definition files, to define a parent with a
	 * default view class and common attributes for all views. View definitions
	 * that define their own parent or carry their own class can still override
	 * this.
	 * <p>
	 * Strictly speaking, the rule that a default parent setting does not apply
	 * to a bean definition that carries a class is there for backwards
	 * compatiblity reasons. It still matches the typical use case.
	 */
	public void setDefaultParentBean(String defaultParentBean) {
		this.defaultParentBean = defaultParentBean;
	}

	/**
	 * Return the default parent bean for this bean factory.
	 */
	public String getDefaultParentBean() {
		return this.defaultParentBean;
	}



	public Map<String, String> getImportMappings() {
		return importMappings;
	}

	public void setImportMappings(Map<String, String> importMappings) {
		this.importMappings = importMappings;
	}
	
	public static String listSetters(String className) {

		Method[] methods;
		try {
			methods = Class.forName(className).getMethods();
		} catch (SecurityException e) {
			return "";
		} catch (ClassNotFoundException e) {
			return "";
		}

		StringBuffer buf = new StringBuffer();
		boolean first = true;
		for (Method method : methods) {
			if (isSetter(method)) {
				if (!first) buf.append(",");
				first = false;
				String property = method.getName().replaceFirst("set", "");
				property = property.substring(0,1).toLowerCase() + property.substring(1);
				buf.append(property );
			}
		}

		return buf.toString();
	}

	public static boolean isSetter(Method method) {
		if (!method.getName().startsWith("set"))
			return false;
		if (method.getParameterTypes().length != 1)
			return false;
		return true;
	}


	
}		
		
	
