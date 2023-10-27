package com.victoroliveira.dscommerce.dto;

import com.victoroliveira.dscommerce.entities.Category;

public class CategoryDTO {

	private Long id;
	private String name;

	public CategoryDTO(Long id, String name) {

		this.id = id;
		this.name = name;
	}

	public CategoryDTO(Category entity) {

		this.id = entity.getId();
		this.name = entity.getName();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}