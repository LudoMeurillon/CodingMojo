package fr.codingmojo.randomtest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fr.codingmojo.CalcUtils;

public class Test2 {

	@Test
	public void test(){
		assertEquals(6, CalcUtils.add(2, 3));
	}
}
