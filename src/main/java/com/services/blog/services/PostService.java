package com.services.blog.services;

import java.util.List;

import com.services.blog.payloads.PostDto;
import com.services.blog.payloads.PostResponse;

public interface PostService {
	
	PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);
	
	PostDto updatePost(PostDto postDto, Integer postId);
	
	void deletePost(Integer postId);
	
	PostDto getSinglePost(Integer postId);
	
	PostResponse getAllPosts(Integer pageNumber, Integer pageSize,String sortBy,String sortDir);
	
	List<PostDto> getPostsByCategory(Integer categoryId);
	
	List<PostDto> getPostsByUser(Integer userId);
	
	List<PostDto> searchPosts(String keyword);

}
