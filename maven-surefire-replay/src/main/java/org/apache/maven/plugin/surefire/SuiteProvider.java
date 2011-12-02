package org.apache.maven.plugin.surefire;

import java.util.Properties;

import org.apache.maven.surefire.junit4.JUnit4Provider;
import org.apache.maven.surefire.providerapi.ProviderParameters;
import org.apache.maven.surefire.report.ConsoleLogger;
import org.apache.maven.surefire.report.ReporterConfiguration;
import org.apache.maven.surefire.report.ReporterFactory;
import org.apache.maven.surefire.testset.DirectoryScannerParameters;
import org.apache.maven.surefire.testset.TestArtifactInfo;
import org.apache.maven.surefire.testset.TestRequest;
import org.apache.maven.surefire.util.CodingMojoDirectoryScanner;
import org.apache.maven.surefire.util.DirectoryScanner;

public class SuiteProvider extends JUnit4Provider{
	
	
	public SuiteProvider(ProviderParameters booterParameters ){
		super(proxifyProviderParameters(booterParameters));
	}
	
	private static ProviderParameters proxifyProviderParameters(final ProviderParameters booterParameters){
		return new ProviderParameters(){

			public DirectoryScanner getDirectoryScanner() {
				if(getDirectoryScannerParameters() == null){
					return null;
				}
				return new CodingMojoDirectoryScanner(
						getDirectoryScannerParameters().getTestClassesDirectory(),
						getDirectoryScannerParameters().getIncludes(),
						getDirectoryScannerParameters().getExcludes(),
						getDirectoryScannerParameters().getRunOrder());
			}

			public ReporterFactory getReporterFactory() {
				return booterParameters.getReporterFactory();
			}

			public ConsoleLogger getConsoleLogger() {
				return booterParameters.getConsoleLogger();
			}

			public DirectoryScannerParameters getDirectoryScannerParameters() {
				return booterParameters.getDirectoryScannerParameters();
			}

			public ReporterConfiguration getReporterConfiguration() {
				return booterParameters.getReporterConfiguration();
			}

			public TestRequest getTestRequest() {
				return booterParameters.getTestRequest();
			}

			public ClassLoader getTestClassLoader() {
				return booterParameters.getTestClassLoader();
			}

			public Properties getProviderProperties() {
				return booterParameters.getProviderProperties();
			}

			public TestArtifactInfo getTestArtifactInfo() {
				return booterParameters.getTestArtifactInfo();
			}
		};
	}


}
