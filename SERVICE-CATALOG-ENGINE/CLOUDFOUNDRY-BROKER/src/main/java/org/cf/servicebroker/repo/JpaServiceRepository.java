package org.cf.servicebroker.repo;

import org.cf.servicebroker.model.service.JpaService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaServiceRepository extends JpaRepository<JpaService, String> {

}
