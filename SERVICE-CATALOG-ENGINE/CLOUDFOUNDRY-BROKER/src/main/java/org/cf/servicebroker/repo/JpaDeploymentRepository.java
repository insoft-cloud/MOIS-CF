package org.cf.servicebroker.repo;

import org.cf.servicebroker.model.deployment.JpaDeployment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDeploymentRepository extends JpaRepository<JpaDeployment, String> {

    JpaDeployment findByName(String name);
}
