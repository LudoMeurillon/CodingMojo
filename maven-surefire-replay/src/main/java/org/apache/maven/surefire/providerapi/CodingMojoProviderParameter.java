package org.apache.maven.surefire.providerapi;

import java.util.Properties;

import org.apache.maven.surefire.report.ConsoleLogger;
import org.apache.maven.surefire.report.ReporterConfiguration;
import org.apache.maven.surefire.report.ReporterFactory;
import org.apache.maven.surefire.testset.DirectoryScannerParameters;
import org.apache.maven.surefire.testset.TestArtifactInfo;
import org.apache.maven.surefire.testset.TestRequest;
import org.apache.maven.surefire.util.CodingMojoDirectoryScanner;
import org.apache.maven.surefire.util.DirectoryScanner;

public class CodingMojoProviderParameter implements ProviderParameters{

	private ProviderParameters targetParams;
	
	public CodingMojoProviderParameter(ProviderParameters targetParams){
		this.targetParams = targetParams;
	}
	
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
		return targetParams.getReporterFactory();
	}

	public ConsoleLogger getConsoleLogger() {
		return targetParams.getConsoleLogger();
	}

	public DirectoryScannerParameters getDirectoryScannerParameters() {
		return targetParams.getDirectoryScannerParameters();
	}

	public ReporterConfiguration getReporterConfiguration() {
		return targetParams.getReporterConfiguration();
	}

	public TestRequest getTestRequest() {
		return targetParams.getTestRequest();
	}

	public ClassLoader getTestClassLoader() {
		return targetParams.getTestClassLoader();
	}

	public Properties getProviderProperties() {
		return targetParams.getProviderProperties();
	}

	public TestArtifactInfo getTestArtifactInfo() {
		return targetParams.getTestArtifactInfo();
	}
	
}
