package com.andela.irrigation.repository;

import com.andela.irrigation.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, String>, JpaSpecificationExecutor<Sensor> {
}
