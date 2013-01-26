package ssrl.yaml.spring;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.restflow.yaml.spring.YamlBeanDefinitionReader;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.support.GenericApplicationContext;
import org.yaml.snakeyaml.parser.ParserException;
import org.yaml.snakeyaml.scanner.ScannerException;


import ssrl.beans.Address;
import ssrl.beans.People;
import ssrl.beans.Person;
import junit.framework.TestCase;


public class TestLoader extends TestCase {
	
	public void testLoadTest1() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);
		rdr.registerBeanDefinitions("file:src/test/resources/baseComponents.yml");

		factory.refresh();
	}
	
	public void testLoadTest2() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);
		rdr.registerBeanDefinitions("file:src/test/resources/components.yml");

		factory.refresh();
		
		Address address = (Address)factory.getBean("SsrlAddress");
		assertEquals("2575 Sand Hill", address.getStreet());
		
	}

	public void testInnerBean() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);
		rdr.registerBeanDefinitions("file:src/test/resources/innerBean.yml");

		factory.refresh();
		
		Person person = (Person)factory.getBean("Zelazny");
		assertEquals("Roger", person.getFirst());
		assertEquals("2575 Sand Hill", person.getAddress().getStreet());
		
	}
	
	public void testInnerBeanInList() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);
		rdr.registerBeanDefinitions("file:src/test/resources/innerBean.yml");

		factory.refresh();
		
		People people = (People)factory.getBean("Authors");
		assertNotNull( people.getPeople().get(0));
		
	}	
	
	public void testErrors() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);

		try {
			rdr.registerBeanDefinitions("file:src/test/resources/tabErrorTest.yml");
		} catch (BeanCreationException e) {
			assertEquals("Syntax error in yaml resource: tabErrorTest.yml\nTab in indentation at line 10, column 0\n    	  last: Zelazny\n    ^", e.getMessage());
		}

	}	

	public void testUnalignedColumnErrors() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);

		try {
			rdr.registerBeanDefinitions("file:src/test/resources/unalignedColumnErrorTest.yml");
		} catch (BeanCreationException e) {
			assertEquals("Syntax error in yaml resource: unalignedColumnErrorTest.yml\nIndentation unaligned near line 10, column 5\n         last: Zelazny\n         ^", e.getMessage() );
		}

	}	

	public void testEmptyNamespace() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);

		rdr.registerBeanDefinitions("file:src/test/resources/emptyNamespace.yml");


	}		
	
	public void testEmptyImports() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);

		rdr.registerBeanDefinitions("file:src/test/resources/emptyImports.yml");

	}			
	
	public void testEmptyTypes() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);

		rdr.registerBeanDefinitions("file:src/test/resources/emptyTypes.yml");

	}			
	public void testEmptyComponents() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);

		rdr.registerBeanDefinitions("file:src/test/resources/emptyComponents.yml");

	}			
	
	public void testInvalidComponentProperty() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);

		try {
			rdr
					.registerBeanDefinitions("file:src/test/resources/invalidComponentProperty.yml");
		} catch (BeanCreationException e) {
			assertEquals(
					e.getMessage(),
					""
							+ " Unable to find property 'a' on class: org.restflow.yaml.spring.SpringBean in yaml resource: invalidComponentProperty.yml\n"
							+ "Error found while traversing yaml structure:\n"
							+ "  components:\n    a:\n"
							+ " Property should be one of properties,parent,id,scope,singleton,namespace,abztract,type,className,lazy,constructor");

			// assertTrue(e.getMessage().startsWith("Component is defined with invalid token.\n Cannot create property=a"));
		}

	}

	public void testNoImportList() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);

		try {
			rdr
					.registerBeanDefinitions("file:src/test/resources/noImportList.yml");
		} catch (BeanCreationException e) {

			assertEquals(
					"Problem in imports declaration.  List resources with '- '",
					e.getMessage());
		}

	}

	public void testBadTopLevelKey() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);

		try {
			rdr
					.registerBeanDefinitions("file:src/test/resources/badTopLevelKey.yml");
		} catch (BeanCreationException e) {
			assertEquals(
					e.getMessage(),
					" Unable to find property 'badKey' on class: org.restflow.yaml.spring.YamlBeans in yaml resource: badTopLevelKey.yml\n"
							+ "Error found while traversing yaml structure:\n"
							+ "  badKey:\n"
							+ " Property should be one of namespace,beans,components,imports,types");

			// assertTrue(e.getMessage().startsWith("Component is defined with invalid token.\n Unable to find property 'badKey' on class: org.restflow.yaml.spring.YamlBeans"));
		}

	}

	public void testInvalidTag() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);

		try {
			rdr
					.registerBeanDefinitions("file:src/test/resources/invalidTag.yml");
		} catch (BeanCreationException e) {
			assertEquals(e.getMessage(),
					" Invalid tag: !local in yaml resource: invalidTag.yml\n"
							+ "Error found while traversing yaml structure:\n"
							+ "  components:\n" + "    properties:\n");
		}

	}
	
	
	public void testResourceTag() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);

		//rdr.registerBeanDefinitions("file:src/test/resources/resourceTag.yml");

		String text = "components:\n"
				+ "\n"
				+ "  - id: SsrlAddress\n"
				+ "    className: ssrl.beans.Address\n"
				+ "    properties:\n"
				+ "      street: !resource file:src/test/resources/street.txt\n"
				+ "      city: Menlo Park\n"
				+ "      zip: 94025  \n";
		InputStream beans = new ByteArrayInputStream(text.getBytes("UTF-8"));
		
		rdr.loadBeanDefinitions(beans,"junit Test");
		factory.refresh();
		
		Address address = (Address)factory.getBean("SsrlAddress");
		assertEquals("2575 Sand Hill Road", address.getStreet());
	}
	
	
	
}
