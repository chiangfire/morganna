package com.firecode.morganna.framework.keygen;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Example HostName :pot-provider-001, workerId > 01
 **/
public class HostNameIdGenerator extends CommonIdGenerator implements IdGenerator{
	

    { initWorkerId(); }

    void initWorkerId() {
        InetAddress address;
        Long workerId;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Cannot get LocalHost InetAddress, please check your network!");
        }
        String hostName = address.getHostName();
        try {
            workerId = Long.valueOf(hostName.replace(hostName.replaceAll("\\d+$", ""), ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Wrong hostname:%s, hostname must be end with number!", hostName));
        }
        this.setWorkerId(workerId);
    }

    @Override
    public Serializable generate() {
    	
        return this.createId();
    }
}
