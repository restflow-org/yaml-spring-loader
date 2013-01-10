package ssrl.yaml.spring;
import junit.framework.TestCase;


public class TestNamespaceBasic extends TestCase {

	public void testOrganization() throws Exception {
		Namespace ns = new Namespace();
		ns.setOrganization("SSRL");
		
		assertEquals("SSRL", ns.calcNamespaceText());
	}

	public void testModule() throws Exception {
		Namespace ns = new Namespace();
		ns.setModule("DataProcessing");
		
		assertEquals("DataProcessing", ns.calcNamespaceText());
	}
	
	public void testRevision() throws Exception {
		Namespace ns = new Namespace();
		ns.setRevision("1.0");
		assertEquals("1.0", ns.calcNamespaceText());
	}

	public void testOrganizationModule () throws Exception {
		Namespace ns = new Namespace();
		ns.setOrganization("SSRL");
		ns.setModule("DataProcessing");		
		
		assertEquals("SSRL.DataProcessing", ns.calcNamespaceText());
	}

	public void testOrganizationRevision () throws Exception {
		Namespace ns = new Namespace();
		ns.setOrganization("SSRL");
		ns.setRevision("2.3");		
		
		assertEquals("SSRL.2.3", ns.calcNamespaceText());
	}

	
	public void testOrganziationModuleRevision() throws Exception {
		Namespace ns = new Namespace();
		ns.setOrganization("SSRL");
		ns.setModule("DataProcessing");
		ns.setRevision("1.5");
		
		assertEquals("SSRL.DataProcessing.1.5", ns.calcNamespaceText());
		
	}

	public void testNull() throws Exception {
		Namespace ns = new Namespace();
		assertEquals("", ns.calcNamespaceText());
	}
		

	public void testModuleRevision() throws Exception {
		Namespace ns = new Namespace();
		ns.setModule("DataProcessing");
		ns.setRevision("1.5");
		
		assertEquals("DataProcessing.1.5", ns.calcNamespaceText());
	}
	
}

