package com.services.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.services.blog.entities.Category;


public interface CategoryRepo extends JpaRepository<Category, Integer> {

}
