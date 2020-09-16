package com.portal.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal.project.model.Sector;
import com.portal.project.repository.SectorRepository;

@Service
public class SectorService {

	@Autowired
	private SectorRepository sectorRepository;
	
	public Sector saveSector(Sector sector) {
		return sectorRepository.save(sector);
	}
	
	public Sector findSectorById(Long id) {
		return sectorRepository.findById(id).get();
	}
	
}
