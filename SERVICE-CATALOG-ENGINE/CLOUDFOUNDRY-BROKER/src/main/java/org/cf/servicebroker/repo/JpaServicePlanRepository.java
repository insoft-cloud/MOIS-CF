package org.cf.servicebroker.repo;


import org.cf.servicebroker.model.plan.JpaServicePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaServicePlanRepository extends JpaRepository<JpaServicePlan, String> {
}
