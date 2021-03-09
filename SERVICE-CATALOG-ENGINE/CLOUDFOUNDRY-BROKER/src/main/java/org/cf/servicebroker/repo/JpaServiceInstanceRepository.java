package org.cf.servicebroker.repo;

import org.cf.servicebroker.model.serviceinstance.JpaServiceInstn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaServiceInstanceRepository extends JpaRepository<JpaServiceInstn, String> {
    JpaServiceInstn findByVmInstnId(String vmInstanId);
}
