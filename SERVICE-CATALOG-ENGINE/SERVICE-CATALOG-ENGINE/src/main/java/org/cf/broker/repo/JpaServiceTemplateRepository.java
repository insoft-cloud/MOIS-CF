package org.cf.broker.repo;

import org.cf.broker.model.jpa.JpaServiceTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaServiceTemplateRepository extends JpaRepository<JpaServiceTemplate, Integer> {
}
