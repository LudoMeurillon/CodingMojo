package fr.codingmojo.mockito;


public class CodingMojoMatchers {
	
	public static <T> MatcherIsNotComplete<T> some(Class<T> clazz) {
		 return new MatcherIsNotComplete<T>();
	}
}
