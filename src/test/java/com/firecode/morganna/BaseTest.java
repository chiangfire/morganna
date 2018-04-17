package com.firecode.morganna;

import java.util.function.Consumer;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/**
 * @author JIANG
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BaseTest{
	
	
	public static final void p(Object o){
		
		System.err.println(o);
	}
	
	public static final void timeTest(Consumer<Object> consumer){
		long time = System.currentTimeMillis();
		consumer.accept(null);
		System.err.println(String.join("", "执行完毕耗时：",String.valueOf(System.currentTimeMillis() - time)));
	}
	

}
