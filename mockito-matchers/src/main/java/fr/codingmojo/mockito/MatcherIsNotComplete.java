package fr.codingmojo.mockito;

import static org.mockito.Matchers.argThat;

public class MatcherIsNotComplete<T> {

	private String attribute;
	
	public MatcherIsNotComplete<T> with(String attribute){
		this.attribute = attribute;
		return this;
	}
	
	public T eq(Object value){
		return argThat(new ReflectionMatcher<T>(attribute, value));
	}
	
	public T isNull(){
		return eq(null);
	}
}
