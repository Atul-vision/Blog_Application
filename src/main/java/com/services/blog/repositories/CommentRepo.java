package com.services.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.services.blog.entities.Comment;

public interface CommentRepo extends JpaRepository<Comment, Integer> {
	
	

}
