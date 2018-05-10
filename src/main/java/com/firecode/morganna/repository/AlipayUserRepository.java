package com.firecode.morganna.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.firecode.morganna.domain.alipay.AlipayUser;

@Repository
public interface AlipayUserRepository extends ReactiveCrudRepository<AlipayUser, String>{
	
}
