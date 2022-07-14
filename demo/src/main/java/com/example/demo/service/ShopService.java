package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Shop;
import com.example.demo.repository.ShopRepository;

@Service
public class ShopService {
	
	@Autowired
	private ShopRepository repository;
	
	// Get the List of Shops
	public List<Shop> getAllShops() {
		List<Shop> list = (List<Shop>) repository.findAll();
		return list;
	}
	
	//Get Shop by keyword
	public List<Shop> getByKeyword(String keyword) {
		return repository.findByKeyword(keyword);
	}

}
