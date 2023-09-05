package com.services.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.asm.Advice.OffsetMapping.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.services.blog.entities.Category;
import com.services.blog.entities.Post;
import com.services.blog.entities.User;
import com.services.blog.exceptions.ResourceNotFoundException;
import com.services.blog.payloads.PostDto;
import com.services.blog.payloads.PostResponse;
import com.services.blog.repositories.CategoryRepo;
import com.services.blog.repositories.PostRepo;
import com.services.blog.repositories.UserRepo;
import com.services.blog.services.PostService;


@Service
public class PostServiceImpl implements PostService{
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;

	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
		
		User user = this.userRepo.findById(userId)
				.orElseThrow(()-> new ResourceNotFoundException("User","Id",userId));
		Category cat = this.categoryRepo.findById(categoryId)
	    		.orElseThrow(()->new ResourceNotFoundException("Category", "CategoryId", categoryId));
		Post post = this.modelMapper.map(postDto,Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(cat);
		Post updatedPost = this.postRepo.save(post);
		return this.modelMapper.map(updatedPost,PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(()->new ResourceNotFoundException("Post", "PostId", postId));
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		Post updatedPost = this.postRepo.save(post);
		return this.modelMapper.map(updatedPost,PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(()->new ResourceNotFoundException("Post", "PostId", postId));
		this.postRepo.delete(post);
	}

	@Override
	public PostDto getSinglePost(Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(()->new ResourceNotFoundException("Post", "PostId", postId));
		return this.modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostResponse getAllPosts(Integer pageNumber,Integer pageSize,String sortBy,String sortDir) {
	
		org.springframework.data.domain.Sort sort=null;
		if(sortDir.equalsIgnoreCase("asc"))
			sort=org.springframework.data.domain.Sort.by(sortBy).ascending();
		else {
			sort=org.springframework.data.domain.Sort.by(sortBy).descending();
		}
		
		Pageable p = PageRequest.of(pageNumber, pageSize,sort);
		
	    Page<Post> pagePost =	this.postRepo.findAll(p);
	    
	    List<Post> allPosts = pagePost.getContent();
	    
	    List<PostDto> list2 = allPosts.stream().map((post)-> this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
		
	    PostResponse postResponse=new PostResponse();
	    postResponse.setContent(list2);
	    postResponse.setPageNumber(pagePost.getNumber());
	    postResponse.setPageSize(pagePost.getSize());
	    postResponse.setTotalElements(pagePost.getTotalElements());
	    postResponse.setTotalPages(pagePost.getTotalPages());
	    postResponse.setLastPages(pagePost.isLast());
	    
	    return postResponse;
	}

	@Override
	public List<PostDto> getPostsByCategory(Integer categoryId) {
		Category cat = this.categoryRepo.findById(categoryId)
				.orElseThrow(()->new ResourceNotFoundException("Category", "CategoryId", categoryId));
		List<Post> posts = this.postRepo.findByCategory(cat);
		List<PostDto> list2 = posts.stream().map((post)-> this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
		return list2;
	}

	@Override
	public List<PostDto> getPostsByUser(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(()->new ResourceNotFoundException("User", "Id", userId));
		List<Post> posts = this.postRepo.findByUser(user);		
		List<PostDto> list2 = posts.stream().map((post)-> this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
		return list2;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
	    List<Post> list = this.postRepo.findByTitleContaining(keyword);
	    List<PostDto> list2 = list.stream().map((post)-> this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
		return list2;
	}
	

}
