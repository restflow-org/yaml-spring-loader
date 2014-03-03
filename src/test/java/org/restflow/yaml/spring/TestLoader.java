package org.restflow.yaml.spring;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.restflow.foo.FooAddress;
import org.restflow.foo.FooAuthors;
import org.restflow.foo.FooAuthor;
import org.restflow.yaml.spring.YamlBeanDefinitionReader;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.support.GenericApplicationContext;

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
		
		FooAddress address = (FooAddress)factory.getBean("SsrlAddress");
		assertEquals("2575 Sand Hill", address.getStreet());
	}

	public void testInnerBean() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);
		rdr.registerBeanDefinitions("file:src/test/resources/innerBean.yml");

		factory.refresh();
		
		FooAuthor person = (FooAuthor)factory.getBean("Zelazny");
		assertEquals("Roger", person.getFirst());
		assertEquals("2575 Sand Hill", person.getAddress().getStreet());
	}
	
	public void testInnerBeanInList() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);
		rdr.registerBeanDefinitions("file:src/test/resources/innerBean.yml");

		factory.refresh();
		
		FooAuthors people = (FooAuthors)factory.getBean("Authors");
		assertNotNull( people.getPeople().get(0));
	}	
	
	public void testErrors() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);

		try {
			rdr.registerBeanDefinitions("file:src/test/resources/tabErrorTest.yml");
		} catch (BeanCreationException e) {
			assertEquals(
					"Syntax error in yaml resource: tabErrorTest.yml\n"
				+	"Tab in indentation at line 10, column 0\n"
				+	"    	  last: Zelazny\n"
				+	"    ^", 
				e.getMessage());
		}
	}	

	public void testUnalignedColumnErrors() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);

		try {
			rdr.registerBeanDefinitions("file:src/test/resources/unalignedColumnErrorTest.yml");
		} catch (BeanCreationException e) {
			assertEquals(
					"Syntax error in yaml resource: unalignedColumnErrorTest.yml\n"
				+	"Indentation unaligned near line 10, column 5\n"
				+	"         last: Zelazny\n"
				+	"         ^", 
				e.getMessage() );
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
			rdr.registerBeanDefinitions("file:src/test/resources/invalidComponentProperty.yml");
		} catch (BeanCreationException e) {
			assertTrue(e.getMessage().startsWith(
					"  in 'reader', line 6, column 3:\n"
					+ "      - id: Zelazny\n"
					+ "      ^ in yaml resource: invalidComponentProperty.yml\n"
					+ "Error found while traversing yaml structure:\n"
					+ "  components:\n"
					+ "    a:\n"));
		}
	}

	public void testNoImportList() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);

		try {
			rdr.registerBeanDefinitions("file:src/test/resources/noImportList.yml");
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
			rdr.registerBeanDefinitions("file:src/test/resources/badTopLevelKey.yml");
		} catch (BeanCreationException e) {
			assertTrue(e.getMessage().startsWith(
					"  in 'reader', line 1, column 9:\n"
				+	"    badKey: hello\n"
				+	"            ^ in yaml resource: badTopLevelKey.yml\n"
				+	"Error found while traversing yaml structure:\n"
				+	"  badKey:"));
		}
	}

	public void testInvalidTag() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);

		try {
			rdr.registerBeanDefinitions("file:src/test/resources/invalidTag.yml");
		} catch (BeanCreationException e) {
				assertEquals(
					"  in 'reader', line 6, column 3:\n"
				+	"      - id: Ssrl\n"
				+	"      ^ in yaml resource: invalidTag.yml\n"
				+	"Error found while traversing yaml structure:\n"
				+	"  components:\n"
				+	"    properties:\n",
				e.getMessage()
				);
		}
	}
	
	public void testResourceTag() throws Exception {
		GenericApplicationContext factory = new GenericApplicationContext();
		YamlBeanDefinitionReader rdr = new YamlBeanDefinitionReader(factory);

		//rdr.registerBeanDefinitions("file:src/test/resources/resourceTag.yml");

		String text = "components:\n"
				+ "\n"
				+ "  - id: SsrlAddress\n"
				+ "    className: org.restflow.foo.FooAddress\n"
				+ "    properties:\n"
				+ "      street: !resource file:src/test/resources/street.txt\n"
				+ "      city: Menlo Park\n"
				+ "      zip: 94025  \n";
		InputStream beans = new ByteArrayInputStream(text.getBytes("UTF-8"));
		
		rdr.loadBeanDefinitions(beans,"junit Test");
		factory.refresh();
		
		FooAddress address = (FooAddress)factory.getBean("SsrlAddress");
		assertEquals("2575 Sand Hill Road", address.getStreet());
	}	
}
