package fr.codingmojo.mockito;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.mockito.ArgumentMatcher;

public class ReflectionMatcher<T> extends ArgumentMatcher<T>{

	private String name;
	private Object value;
	
	public ReflectionMatcher(String attribute, Object value){
		this.name = attribute;
		this.value = value;
	}
	
	@Override
	public boolean matches(Object argument) {
		if(argument == null){
			return false;
		}
		
		Field field = findField(argument.getClass(), name);

		Object realValue = null;
		try{
			if(field != null){
				if(!field.isAccessible()){
					field.setAccessible(true);
				}
				realValue = field.get(argument);
			}else{
				//checking for getter : try to find  name() method
				Method method = findMethod(argument.getClass(), name);
				if(method != null){
					if(!method.isAccessible()){
						method.setAccessible(true);
					}
					realValue = method.invoke(argument, new Object[0]);
				}else{
					throw new IllegalArgumentException("no attribute or method ["+name+"] for class "+argument.getClass());
				}
			}
		
		}catch(IllegalAccessException e){
			e.printStackTrace();
		}catch(InvocationTargetException e){
			e.printStackTrace();
		}
		if(value != null){
			return value.equals(realValue);
		}else{
			return realValue == null;
		}
	}
	
	public static Field findField(Class<?> clazz, String name) {
		Class<?> searchType = clazz;
		while (!Object.class.equals(searchType) && searchType != null) {
			Field[] fields = searchType.getDeclaredFields();
			for (Field field : fields) {
				if (name.equals(field.getName())) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}
	
	public static Method findMethod(Class<?> clazz, String name) {
		Class<?> searchType = clazz;
		while (searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
			for (Method method : methods) {
				if (name.equals(method.getName())) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}
}
