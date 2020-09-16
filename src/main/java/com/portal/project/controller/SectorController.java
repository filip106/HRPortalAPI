package com.portal.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.portal.project.model.Sector;
import com.portal.project.service.SectorService;

@RestController
public class SectorController {

	@Autowired
	private SectorService sectorService;
	
	@PostMapping("/admin/sector")
	public ResponseEntity<?> saveSector(@RequestBody Sector sector) {
		return new ResponseEntity<Sector>(sectorService.saveSector(sector), HttpStatus.OK);
	}
	
}
