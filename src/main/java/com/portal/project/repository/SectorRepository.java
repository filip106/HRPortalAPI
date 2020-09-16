package com.portal.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal.project.model.Sector;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long>{

}
