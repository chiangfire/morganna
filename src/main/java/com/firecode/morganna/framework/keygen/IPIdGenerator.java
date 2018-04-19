package com.firecode.morganna.framework.keygen;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Example IP 192.168.1.108 :11000000 10101000 00000001 01101100，
 * Intercepting the last 10 bits 01 01101100,Decimal system：364, workerId > 364.
 */
public class IPIdGenerator extends CommonIdGenerator implements IdGenerator{
	
    { initWorkerId(); }
    
    
    private IPIdGenerator(){}

    void initWorkerId() {
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Cannot get LocalHost InetAddress, please check your network!");
        }
        byte[] ipAddressByteArray = address.getAddress();
        this.setWorkerId((long) (((ipAddressByteArray[ipAddressByteArray.length - 2] & 0B11) << Byte.SIZE) + (ipAddressByteArray[ipAddressByteArray.length - 1] & 0xFF)));
    }

    @Override
    public Serializable generate() {
    	
        return this.createId();
    }
    
    public static final IdGenerator getInstance(){
    	
    	return IdGeneratorHolder.IDGENERATOR_INSTANCE;
    }
    
	private static final class IdGeneratorHolder{
		
		private static final IdGenerator IDGENERATOR_INSTANCE = new IPIdGenerator();
	}
    
}
