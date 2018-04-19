package com.firecode.morganna.framework;

import java.io.Serializable;

import com.firecode.morganna.framework.keygen.IPIdGenerator;
import com.firecode.morganna.framework.keygen.IdGenerator;
/**
 * @author JIANG
 */
public enum GenerationType implements IdGenerator{
	/**
	 * Twitter Snowflake 算法
	 */
	SNOWFLAKE(){
		@Override
		public Serializable generate() {
			
			return IPIdGenerator.getInstance().generate();
		}
	};
}
