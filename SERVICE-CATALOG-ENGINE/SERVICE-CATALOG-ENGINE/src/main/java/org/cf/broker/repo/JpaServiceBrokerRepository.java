package org.cf.broker.repo;

import org.cf.broker.model.jpa.JpaServiceBroker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaServiceBrokerRepository extends JpaRepository<JpaServiceBroker, String> {
    List<JpaServiceBroker> findAllByUseAt(int i);
}
