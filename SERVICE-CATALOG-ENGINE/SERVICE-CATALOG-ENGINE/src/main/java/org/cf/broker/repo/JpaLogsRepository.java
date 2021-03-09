package org.cf.broker.repo;

import org.cf.broker.model.jpa.JpaLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLogsRepository extends JpaRepository<JpaLog,Integer> {





}
