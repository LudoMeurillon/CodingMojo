package fr.codingmojo;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

public class SuiteRunner extends Suite {

	public SuiteRunner(Class<?> klass, File targetDirectory)
			throws InitializationError {
		super(klass, getClasses(targetDirectory));
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
	
	private static final Class<?>[] getClasses(File targetDirectory){
		try {
			File file = new File(targetDirectory, "surefire-reports");
			assert targetDirectory.isDirectory();
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
				klasses.add(new FileBean(Class.forName(filename.replace(".txt", "") /*, false, ClassLoader.getSystemClassLoader()*/), new File(file, filename).lastModified()));
			}
			Collections.sort(klasses);
			
			Class<?>[] array = new Class<?>[klasses.size()];
			for (int j = 0; j < array.length; j++) {
				array[j] = klasses.get(j).klass;
			}
			return array;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return new Class<?>[0];
	}

}
