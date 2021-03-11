package com.infranics.demo.service;

import com.infranics.demo.cloudfoundry.model.JpaScreeningClinic;
import com.infranics.demo.cloudfoundry.repository.JpaScreeningClinicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoService {

    final
    JpaScreeningClinicRepository jpaScreeningClinicRepository;

    public DemoService(JpaScreeningClinicRepository jpaScreeningClinicRepository) {
        this.jpaScreeningClinicRepository = jpaScreeningClinicRepository;
    }

    public void SaveData(List<JpaScreeningClinic> screeningClinicList){
        jpaScreeningClinicRepository.saveAll(screeningClinicList);
    }

    public List<JpaScreeningClinic> ListData(){
        return jpaScreeningClinicRepository.findAll();
    }
}
