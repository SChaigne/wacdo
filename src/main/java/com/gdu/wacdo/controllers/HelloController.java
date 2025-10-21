package com.gdu.wacdo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/check")
	public String helloWorld() {
		return "Hello World!";
	}
}