package com.firecode.morganna.framework;

import java.io.Serializable;
import org.junit.Test;

import com.firecode.morganna.BaseTest;

public class GeneratedValueTest {

	@Test
	public void test() {
		BaseTest.timeTest((args)->{
			for(int i=0;i<100000;i++){
				Serializable generate = GenerationType.SNOWFLAKE.generate();
				System.err.println(generate);
			}
		});
	}

}
