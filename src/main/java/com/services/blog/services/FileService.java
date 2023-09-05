package com.services.blog.services;

import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


public interface FileService {
	
	String uploadImage(String path,MultipartFile file) throws IOException;
	InputStream getResource(String path, String filename) throws FileNotFoundException;

}
