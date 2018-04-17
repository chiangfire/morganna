package com.firecode.morganna.framework.annotation;

import static java.lang.annotation.ElementType.FIELD;
//import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.firecode.morganna.framework.GenerationType;
/**
 * 自动生成主键
 * @author JIANG
 * 
 */
@Target({/*METHOD, */FIELD})
@Retention(RUNTIME)
public @interface GeneratedValue {
	
	GenerationType strategy() default GenerationType.SNOWFLAKE;
	
}
