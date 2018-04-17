package com.firecode.morganna.framework.keygen;

import java.io.Serializable;

/**
 * @author jiang
 */
public interface IdGenerator {
	
    /**
     * Id
     */
	Serializable generate();

}
