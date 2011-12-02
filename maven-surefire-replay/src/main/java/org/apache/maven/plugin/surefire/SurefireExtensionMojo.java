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

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.surefire.booterclient.ForkConfiguration;
import org.apache.maven.plugin.surefire.booterclient.ForkStarter;
import org.apache.maven.surefire.booter.ClassLoaderConfiguration;
import org.apache.maven.surefire.booter.Classpath;
import org.apache.maven.surefire.booter.ProviderConfiguration;
import org.apache.maven.surefire.booter.StartupConfiguration;
import org.apache.maven.surefire.booter.StartupReportConfiguration;
import org.apache.maven.surefire.booter.SurefireExecutionException;
import org.apache.maven.surefire.booter.SurefireStarter;
import org.apache.maven.surefire.suite.RunResult;

/**
 * Run tests using Surefire.
 */
public class SurefireExtensionMojo extends SurefirePlugin {

	public void execute() throws MojoExecutionException {
		try {
			super.execute();
		} catch (MojoFailureException e) {
			throw new MojoExecutionException("Error running tests ", e);
		}
	}

	@Override
	protected List createProviders() throws MojoFailureException {
		final Artifact junitDepArtifact = (Artifact) getProjectArtifactMap()
				.get("junit:junit-dep");
		final Artifact junitArtefact = (Artifact) getProjectArtifactMap().get(
				getJunitArtifactName());
		// final Artifact sureFireApi = (Artifact) getProjectArtifactMap()
		// .get("org.apache.maven.surefire:surefire-api");

		final ProviderInfo surefireReplayProviderInfo = new JUnit4ProviderInfo(
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
				SurefireDependencyResolver resolver = new SurefireDependencyResolver( 
						getArtifactResolver(), getArtifactFactory(), getLog(), getLocalRepository(),
                        getRemoteRepositories(), getMetadataSource(), getPluginName());
				
				return resolver.addProviderToClasspath(pluginArtifactMap, null);
			}

		};

		List providers = new ArrayList();
		providers.add(surefireReplayProviderInfo);
		return providers;
	}

	protected SurefireStarter createInprocessStarter(ProviderInfo provider,
			ForkConfiguration forkConfiguration,
			ClassLoaderConfiguration classLoaderConfiguration)
			throws MojoExecutionException, MojoFailureException {
		final StartupConfiguration startupConfiguration = createStartupConfiguration(
				forkConfiguration, provider, classLoaderConfiguration);
		final StartupReportConfiguration startupReportConfiguration = getStartupReportConfiguration();
		final ProviderConfiguration providerConfiguration = createProviderConfiguration();
		getLog().info("Creating surefire SurefireStarter");
		return new SurefireStarter(startupConfiguration, providerConfiguration,
				startupReportConfiguration);
	}

	protected ForkStarter createForkStarter(ProviderInfo provider,
			ForkConfiguration forkConfiguration,
			ClassLoaderConfiguration classLoaderConfiguration)
			throws MojoExecutionException, MojoFailureException {
		StartupConfiguration startupConfiguration = createStartupConfiguration(
				forkConfiguration, provider, classLoaderConfiguration);
		StartupReportConfiguration startupReportConfiguration = getStartupReportConfiguration();
		ProviderConfiguration providerConfiguration = createProviderConfiguration();
		getLog().info("Creating surefire ForkStarter");
		return new ForkStarter(providerConfiguration, startupConfiguration,
				forkConfiguration, getForkedProcessTimeoutInSeconds(),
				startupReportConfiguration);
	}

	private StartupReportConfiguration getStartupReportConfiguration() {
		return new StartupReportConfiguration(isUseFile(), isPrintSummary(),
				getReportFormat(), isRedirectTestOutputToFile(),
				isDisableXmlReport(), getReportsDirectory(),
				isTrimStackTrace(), getReportNameSuffix());
	}

}
