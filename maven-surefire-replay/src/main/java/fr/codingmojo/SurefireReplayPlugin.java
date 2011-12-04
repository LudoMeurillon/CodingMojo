package fr.codingmojo;

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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.surefire.AbstractSurefireMojo;
import org.apache.maven.plugin.surefire.SuiteProvider;
import org.apache.maven.plugin.surefire.SurefirePluginAdapter;

/**
 * Run last tests run using Surefire.
 * 
 * @author Ludovic Meurillon
 * @version $Id: MyMojo.java $
 * 
 * @extendsPlugin surefire
 * @extendsGoal test
 * @goal replay
 * @phase test
 * 
 * @threadSafe
 * @noinspection JavaDoc
 */
public class SurefireReplayPlugin extends SurefirePluginAdapter {
	
	private static String reportDirectoryProperty = "surefire.replay.last.reports";
	
	/**
     * Base directory where all reports are read from.
     */
    private File lastReportsDirectory;
    
	
	/**
     * Base directory where all reports are written to.
     *
     * @parameter default-value="${project.build.directory}/surefire-replay-reports"
     */
    private File reportsDirectory;
    
    /**
     * Base directory where all reports are written to.
     *
     * @parameter default-value="${project.build.directory}"
     * @required
     */
    private File targetDirectory;
    
    /**
     * Execute the plugin as Surefire could do but start with extracting properties from System or plugin configuration
     */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		Map props = getSystemPropertyVariables();
		//If there is no SystemPropertyVariables just create the Map
		if(props == null){
			props = new HashMap();
		}
		//Check if "surefire.replay.last.reports" is defined
		if(props.get(reportDirectoryProperty) == null){
			//If not try to build it from plugin configuration <lastReportDirectory>...</lastReportDirectory>
			if(lastReportsDirectory != null){
				props.put(reportDirectoryProperty, lastReportsDirectory.getAbsolutePath());
			}else{
				//At last just create a default one into .../target/surefire-rports
				props.put(reportDirectoryProperty, new File(targetDirectory, "surefire-reports").getAbsolutePath());
			}
		}
		
		//refresh system variables used by the plugin
		setSystemPropertyVariables(props);
		super.execute();
	}
	
	/**
	 * Surefire extension to provider our own TestSuiteProvider
	 *  
	 * @see SuiteProvider
	 * @see SurefirePluginAdapter 
	 */
	@Override
	protected List createProviders() throws MojoFailureException {
		List providers = new ArrayList();
		providers.add(createProviderInfo(SuiteProvider.class));
		return providers;
	}
}
