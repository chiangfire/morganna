package com.firecode.morganna.framework;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * 为ID为空的对象生成 Snowflake ID
 * @author JIANG
 */
@WritingConverter
public class EmptyPrimaryToSnowflakeConverter implements Converter<SnowflakePrimary,Object>{

	@Override
	public Object convert(SnowflakePrimary source) {
		
		return source;
	}
	
}
