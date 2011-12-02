package org.apache.maven.surefire.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CodingMojoDirectoryScanner extends DefaultDirectoryScanner {

	private File basedir;

	public CodingMojoDirectoryScanner(File basedir, List includes,
			List excludes, RunOrder runOrder) {
		super(basedir, includes, excludes, runOrder);
		this.basedir = basedir;
	}
	
	public TestsToRun locateTestClasses( ClassLoader classLoader, ScannerFilter scannerFilter )
    {
        String[] testClassNames = collectTests();
        List result = new ArrayList();
        for ( int i = 0; i < testClassNames.length; i++ )
        {
            String className = testClassNames[i];
            Class testClass;
			try {
				testClass = classLoader.loadClass(className);
	            if ( scannerFilter == null || scannerFilter.accept( testClass ) ){
	                result.add( testClass );
	            }else{
	                getClassesSkippedByValidation().add( testClass );
	            }
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
        }
        return new TestsToRun( result );
    }
	

	@Override
	String[] collectTests() {
		try {
			assert basedir.isDirectory();
			File file = new File(basedir.getParent(), "surefire-reports");
			assert file.isDirectory();

			String[] xmlResults = file.list(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (name.endsWith(".txt")) {
						return true;
					}
					return false;
				}
			});

			List<FileBean> klasses = new ArrayList<FileBean>();
			for (String filename : xmlResults) {
				klasses.add(new FileBean(Class.forName(filename.replace(".txt",
						"") /* , false, ClassLoader.getSystemClassLoader() */),
						new File(file, filename).lastModified()));
			}
			Collections.sort(klasses);

			String[] array = new String[klasses.size()];
			for (int j = 0; j < array.length; j++) {
				array[j] = klasses.get(j).klass.getName();
			}
			return array;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return new String[0];
	}

	private static class FileBean implements Comparable<FileBean> {
		private Class<?> klass;
		private Long timestamp;

		private FileBean(Class<?> klass, Long time) {
			this.klass = klass;
			this.timestamp = time;
		}

		public int compareTo(FileBean o) {
			return this.timestamp.compareTo(o.timestamp);
		}
	}
}
