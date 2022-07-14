package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Shop;
import com.example.demo.service.ShopService;

@Controller
public class ShopController {
	
	@Autowired
	private ShopService service;
	
	@RequestMapping(path = {"/","/search"})
	public String home(Shop shop, Model model, String keyword) {
		if (keyword != null) {
			List<Shop> list = service.getByKeyword(keyword);
			model.addAttribute("list", list);
		} else {
			List<Shop> list = service.getAllShops();
			model.addAttribute("list", list);
			
		}
		
		return "index";
	}

}
