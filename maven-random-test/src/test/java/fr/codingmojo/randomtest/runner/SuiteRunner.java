package fr.codingmojo.randomtest.runner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

public class SuiteRunner extends Suite {

	public SuiteRunner(Class<?> klass)
			throws InitializationError {
		super(klass, getClasses());
	}

	private static class FileBean implements Comparable<FileBean>{
		private Class<?> klass;
		private Long timestamp;
		
		private FileBean(Class<?> klass, Long time){
			this.klass = klass;
			this.timestamp =time;
		}
		
		public int compareTo(FileBean o) {
			return this.timestamp.compareTo(o.timestamp);
		}
	}
	
	private static final Class<?>[] getClasses(){
		try {
//			String relativePath = System.getProperty("where.are.my.surefire.reports");
			ClassLoader loader = SuiteRunner.class.getClassLoader();
			URL url = loader.getResource("test.properties");
			
			Properties props = new Properties();
			props.load(new FileInputStream(new File(url.toURI())));
			File file = new File(props.getProperty("surefire.reports.directory"));
			assert file.isDirectory();
			
			String[] xmlResults = file.list(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if(name.endsWith(".txt")){
						return true;
					}
					return false;
				}
			});
			
			List<FileBean> klasses = new ArrayList<FileBean>();
			for (String filename : xmlResults) {
				klasses.add(new FileBean(Class.forName(filename.replace(".txt", "")), new File(file, filename).lastModified()));
			}
			Collections.sort(klasses);
			
			Class<?>[] array = new Class<?>[klasses.size()];
			for (int j = 0; j < array.length; j++) {
				array[j] = klasses.get(j).klass;
			}
			return array;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Class<?>[0];
	}

}
