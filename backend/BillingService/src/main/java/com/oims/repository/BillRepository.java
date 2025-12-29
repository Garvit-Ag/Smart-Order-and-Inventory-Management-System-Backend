package com.oims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oims.model.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
	
	boolean existsByOrderId(Integer ID);
	
}
