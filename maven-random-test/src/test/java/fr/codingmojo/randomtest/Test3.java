package fr.codingmojo.randomtest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fr.codingmojo.CalcUtils;

public class Test3 {

	@Test
	public void test(){
		assertEquals(5, CalcUtils.add(2, 3));
	}
}
