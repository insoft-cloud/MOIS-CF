package org.cf.broker.repo;

import org.cf.broker.model.jpa.JpaService;
import org.cf.broker.model.jpa.JpaServicePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaServicePlanRepository extends JpaRepository<JpaServicePlan, String> {

    JpaServicePlan findByIdAndJpaService(String Id, JpaService service);
}
