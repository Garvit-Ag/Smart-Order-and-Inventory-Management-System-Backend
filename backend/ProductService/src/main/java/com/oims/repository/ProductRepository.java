package com.oims.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oims.Model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	boolean existsById(Integer id);
}
