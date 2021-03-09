package org.cf.broker.repo;

import org.cf.broker.model.jpa.JpaServiceImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaServiceImageRepository extends JpaRepository<JpaServiceImage, Integer> {


}
