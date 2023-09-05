package com.services.blog.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.services.blog.entities.Comment;
import com.services.blog.entities.Post;
import com.services.blog.exceptions.ResourceNotFoundException;
import com.services.blog.payloads.CommentDto;
import com.services.blog.repositories.CommentRepo;
import com.services.blog.repositories.PostRepo;
import com.services.blog.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService{
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private CommentRepo commentRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(()->new ResourceNotFoundException("Post", "PostId", postId));
		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		comment.setPost(post);
	    Comment creteComment = this.commentRepo.save(comment);
		return this.modelMapper.map(creteComment, CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentId) {
		Comment comment = this.commentRepo.findById(commentId)
				.orElseThrow(()->new ResourceNotFoundException("Comment", "CommentId", commentId));
		
		this.commentRepo.delete(comment);
	}
	

}
