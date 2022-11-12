package com.andela.irrigation.repository;

import com.andela.irrigation.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SlotRepository extends JpaRepository<Slot, String>, JpaSpecificationExecutor<Slot> {

}
