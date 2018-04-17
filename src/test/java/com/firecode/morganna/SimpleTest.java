package com.firecode.morganna;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;


public class SimpleTest extends BaseTest{
	
	@Autowired
	private WebTestClient webClient;
	
	@Test
	public void query(){
		webClient.get().uri("/save").accept(MediaType.APPLICATION_JSON_UTF8).exchange()
		.expectStatus().isOk()
		.expectBody(String.class).consumeWith(response->{
			p(response);
		});
	}
}
