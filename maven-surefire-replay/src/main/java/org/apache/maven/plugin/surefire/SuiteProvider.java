package org.apache.maven.plugin.surefire;

import org.apache.maven.surefire.junit4.JUnit4Provider;
import org.apache.maven.surefire.providerapi.CodingMojoProviderParameter;
import org.apache.maven.surefire.providerapi.ProviderParameters;

public class SuiteProvider extends JUnit4Provider{
	
	public SuiteProvider(ProviderParameters booterParameters ){
		super(new CodingMojoProviderParameter(booterParameters));
	}
	
}
