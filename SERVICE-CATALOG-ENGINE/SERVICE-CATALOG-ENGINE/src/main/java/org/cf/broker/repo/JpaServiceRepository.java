package org.cf.broker.repo;

import org.cf.broker.model.jpa.JpaService;
import org.cf.broker.model.jpa.JpaServiceBroker;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JpaServiceRepository extends JpaRepository<JpaService, String> {

    JpaService findByIdAndServiceBroker(String Id, JpaServiceBroker broker);

}
