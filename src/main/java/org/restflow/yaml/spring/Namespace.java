package org.restflow.yaml.spring;


public class Namespace {
	
	final public static String BasicNamespaceDelimeter = ".";
	
	private String organization = "";
	private String module = "";
	private String route = "";		
	private String revision = "";
	
	public Namespace() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	
	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String calcNamespaceText() {
		StringBuffer namespace = new StringBuffer("");

		String delimeter = "";
		if ( getOrganization().length() != 0 ) {
			namespace.append( getOrganization() );
			delimeter = BasicNamespaceDelimeter; 
		}

		if ( getModule().length() != 0 ) {
			namespace.append( delimeter );
			namespace.append( getModule() ) ;
			delimeter = BasicNamespaceDelimeter; 			
		}

		if ( getRoute().length() != 0 ) {
			namespace.append( delimeter );
			namespace.append( getRoute() ) ;
			delimeter = BasicNamespaceDelimeter; 			
		}
		
		if ( getRevision().length() != 0 ) {
			namespace.append( delimeter );
			namespace.append( getRevision() );
			delimeter = BasicNamespaceDelimeter; 
		}
		
		return namespace.toString();
	}
	
	
}
