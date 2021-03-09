package org.cf.broker.repo;

import org.cf.broker.model.jpa.JpaServiceBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaServiceBatchRepository extends JpaRepository<JpaServiceBatch, Integer> {
}
