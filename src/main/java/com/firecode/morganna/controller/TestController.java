package com.firecode.morganna.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class TestController {
	
	@GetMapping("/test")
	public Mono<String> get(){
		
		return Mono.just("测试数据");
	}
	

}
