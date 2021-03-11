package com.infranics.demo.cloudfoundry.repository;

import com.infranics.demo.cloudfoundry.model.JpaScreeningClinic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaScreeningClinicRepository extends JpaRepository<JpaScreeningClinic, Integer > {
}
