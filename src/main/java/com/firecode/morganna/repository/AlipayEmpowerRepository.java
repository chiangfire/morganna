package com.firecode.morganna.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.firecode.morganna.domain.alipay.AlipayEmpowerRecord;

@Repository
public interface AlipayEmpowerRepository extends ReactiveCrudRepository<AlipayEmpowerRecord, String>{

}
