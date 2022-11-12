package com.andela.irrigation.repository;

import com.andela.irrigation.model.Plot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PlotRepository extends JpaRepository<Plot, String>, JpaSpecificationExecutor<Plot> {
}
