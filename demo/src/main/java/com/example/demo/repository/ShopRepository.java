package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Shop;

public interface ShopRepository extends JpaRepository<Shop, Integer> {
	
	// Custom query
	@Query(value = " select * from shop s where s.owner_name like %:keyword% or"
			+ "s.shop_type like %:keyword%", nativeQuery = true)
	List<Shop> findByKeyword(@Param("keyword") String keyword);

}
