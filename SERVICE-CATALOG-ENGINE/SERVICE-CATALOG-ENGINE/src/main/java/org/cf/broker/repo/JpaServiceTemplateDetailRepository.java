package org.cf.broker.repo;

import org.cf.broker.model.jpa.JpaServiceTemplateDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaServiceTemplateDetailRepository extends JpaRepository<JpaServiceTemplateDetail, Integer> {
}
