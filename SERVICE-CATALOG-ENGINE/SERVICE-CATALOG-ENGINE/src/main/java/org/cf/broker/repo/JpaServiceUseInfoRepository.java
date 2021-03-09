package org.cf.broker.repo;

import org.cf.broker.model.jpa.JpaServiceUseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaServiceUseInfoRepository extends JpaRepository<JpaServiceUseInfo, Integer> {


}
