package org.cf.broker.repo;

import org.cf.broker.model.jpa.JpaServiceInstnModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaServiceInstanceModelRepository extends JpaRepository<JpaServiceInstnModel, String> {

}
