package com.portal.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RstController {
	
	@GetMapping("/employee/cao")
	public String cao() {
		return "cao";
	}
	
	@GetMapping("/admin/cao")
	public String caoadmine() {
		return "cao";
	}
}
