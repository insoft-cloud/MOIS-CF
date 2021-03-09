package org.cf.broker.repo;

import org.cf.broker.model.jpa.JpaServiceInstn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaServiceInstanceRepository extends JpaRepository<JpaServiceInstn, String> {
    List<JpaServiceInstn> findAllByProjectId(String projectId);

    JpaServiceInstn findByClientId(@Param("client_id") String clientId);
}
