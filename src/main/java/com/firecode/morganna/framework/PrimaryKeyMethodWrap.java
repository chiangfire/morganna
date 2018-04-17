package com.firecode.morganna.framework;

import java.lang.reflect.Method;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PrimaryKeyMethodWrap {
	
	private Method readMethod;
	
	private Method writeMethod;
	
	private GenerationType strategy;

}
