package com.ankaraKarate.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ankaraKarate.entity.Category;
import com.ankaraKarate.repo.CategoryRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
	
	private final CategoryRepo categoryRepo;
	
	public Map<Integer, String> kategoriMap(){
		
		List<Category> categories = categoryRepo.findAll();
		
		Map<Integer, String> categoryMap = new HashMap<>();
		
		for(Category c : categories) {
			categoryMap.put(c.getId(), c.getName());
		}
		
		return categoryMap;
	}

}
