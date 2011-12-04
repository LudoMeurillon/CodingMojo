package org.apache.maven.plugin.surefire;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Map;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.surefire.booter.Classpath;
import org.apache.maven.surefire.providerapi.SurefireProvider;

/**
 * Run tests using Surefire.
 */
public class SurefirePluginAdapter extends SurefirePlugin {
	

	protected SurefireDependencyResolver createDependencyResolver()
    {
        return new SurefireDependencyResolver( getArtifactResolver(), getArtifactFactory(), getLog(), getLocalRepository(), getRemoteRepositories(), getMetadataSource(), getPluginName() );
    }
	
	protected ProviderInfo createProviderInfo(Class<? extends SurefireProvider> providerClass){
		final Artifact junitDepArtifact = (Artifact) getProjectArtifactMap()
				.get("junit:junit-dep");
		final Artifact junitArtefact = (Artifact) getProjectArtifactMap().get(
				getJunitArtifactName());
		
		return new JUnit4ProviderInfo(
				junitArtefact, junitDepArtifact) {

			public String getProviderName() {
				String name = SuiteProvider.class.getName();
				getLog().info("Getting name of SuiteProvider : " + name);
				return name;
			};

			public Classpath getProviderClasspath()
					throws ArtifactResolutionException,
					ArtifactNotFoundException {
				final Map pluginArtifactMap = getPluginArtifactMap();
				SurefireDependencyResolver resolver = createDependencyResolver();
				return resolver.addProviderToClasspath(pluginArtifactMap, null);
			}
		};
	}
	

//	protected SurefireStarter createInprocessStarter(ProviderInfo provider,
//			ForkConfiguration forkConfiguration,
//			ClassLoaderConfiguration classLoaderConfiguration)
//			throws MojoExecutionException, MojoFailureException {
//		final StartupConfiguration startupConfiguration = createStartupConfiguration(
//				forkConfiguration, provider, classLoaderConfiguration);
//		final StartupReportConfiguration startupReportConfiguration = getStartupReportConfiguration();
//		final ProviderConfiguration providerConfiguration = createProviderConfiguration();
//		getLog().info("Creating surefire SurefireStarter");
//		return new SurefireStarter(startupConfiguration, providerConfiguration,
//				startupReportConfiguration);
//	}
//
//	protected ForkStarter createForkStarter(ProviderInfo provider,
//			ForkConfiguration forkConfiguration,
//			ClassLoaderConfiguration classLoaderConfiguration)
//			throws MojoExecutionException, MojoFailureException {
//		StartupConfiguration startupConfiguration = createStartupConfiguration(
//				forkConfiguration, provider, classLoaderConfiguration);
//		StartupReportConfiguration startupReportConfiguration = getStartupReportConfiguration();
//		ProviderConfiguration providerConfiguration = createProviderConfiguration();
//		getLog().info("Creating surefire ForkStarter");
//		return new ForkStarter(providerConfiguration, startupConfiguration,
//				forkConfiguration, getForkedProcessTimeoutInSeconds(),
//				startupReportConfiguration);
//	}

//	private StartupReportConfiguration getStartupReportConfiguration() {
//		return new StartupReportConfiguration(isUseFile(), isPrintSummary(),
//				getReportFormat(), isRedirectTestOutputToFile(),
//				isDisableXmlReport(), getReportsDirectory(),
//				isTrimStackTrace(), getReportNameSuffix());
//	}

}
