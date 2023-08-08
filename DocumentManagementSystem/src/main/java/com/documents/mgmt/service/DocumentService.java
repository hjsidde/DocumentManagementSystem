package com.documents.mgmt.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.documents.mgmt.exception.DocumentNotFoundExcepxtion;
import com.documents.mgmt.exception.FileStorageException;
import com.documents.mgmt.exception.PostNotFoundException;
import com.documents.mgmt.model.Comments;
import com.documents.mgmt.model.Document;
import com.documents.mgmt.model.Posts;
import com.documents.mgmt.repos.CommentRepo;
import com.documents.mgmt.repos.DocumentRepo;

import ch.qos.logback.classic.Logger;

@Service
public class DocumentService {
	private static final Logger logger = (Logger) LoggerFactory.getLogger(DocumentService.class);

	private final Path fileStorageLocation;

	public DocumentService() {
		this.fileStorageLocation = Paths.get(uploadDirectory).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
					ex);
		}
	}

	@Autowired
	DocumentRepo repo;
	
	@Autowired
	CommentRepo commentRepo;

	@Value("${document.thirdparty-url}")
	String thirdpartyurl;

	@Value("${document.upload-dir}")
	String uploadDirectory;
	
	@Autowired
	RestTemplate restTemplate;

	public String uploadFile(MultipartFile file) {
		logger.info("storeFile begin");

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		Document uploadResponse;
		String status = "";

		logger.info("fileName is : " + fileName);

		try {

			if (fileName.contains("..")) {

				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);

			}

			if (fileName.contains(".pdf")) {
				Path targetLocation = this.fileStorageLocation.resolve(fileName);
				long size = Files.size(targetLocation);
				Long bytesCopied = Files.copy(file.getInputStream(), targetLocation,
						StandardCopyOption.REPLACE_EXISTING);
				if (bytesCopied == size) {
					String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
							.path(fileName).toUriString();
					uploadResponse = repo
							.save(new Document(fileName, fileDownloadUri, file.getContentType(), file.getSize()));
					Long id = uploadResponse.getId();
					status = "upload succesful for Id : " + id;
				}

			} else {

				throw new FileStorageException("Please upload only pdf files!!! " + fileName);

			}

			logger.info("storeFile end");

			return status;
		} catch (IOException ex) {

			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}

	}

	@Transactional
	public boolean delete(Long documentId) {
	    Document document = getDocument(documentId);

	    try {
	        Path file = fileStorageLocation.resolve(document.getFileName());

	        if (Files.exists(file)) {
	            boolean deletionStatus = Files.deleteIfExists(file);
	            if (deletionStatus) {
	                repo.deleteById(documentId);
	                return true;  // File and record deleted successfully
	            } else {
	                
	                return false;  // File deletion failed
	            }
	        } else {
	            
	            return false;  // File does not exist
	        }
	    } catch (IOException e) {
	        logger.error(e.getLocalizedMessage());
	        return false;  // Error occurred during deletion
	    }
	}

	public List<Document> loadAllDocs() {

		List<Document> filesList = repo.findAll();
		return filesList;

	}

	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new DocumentNotFoundExcepxtion("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new DocumentNotFoundExcepxtion("File not found " + fileName, ex);
		}
	}

	public Resource load(String filename) {
		try {
			Path file = fileStorageLocation.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	// associate document with post

	public void associateDocumentWithPost(Long documentId, Long postId) {
		Optional<Document> documentOptional = repo.findById(documentId);
		if (documentOptional.isPresent()) {
			Document document = documentOptional.get();
			document.setPostId(postId);
			repo.save(document);
		} else {

			throw new DocumentNotFoundExcepxtion("Document not found.");
		}
	}

	public void addCommentsToPost(Long documentId, Long postId, Long commentId) {
		
		Document document = getDocument(documentId);
		
		if (document.getPostId() != null && document.getPostId() == postId) {
			
			Comments comments=new Comments();
			comments.setPostId(postId);
			comments.setId(commentId);
			
			
			commentRepo.save(comments);

		} else {
			throw new PostNotFoundException("post not found.");
		}

	}

	public Document getDocument(Long documentId) {
		Optional<Document> documentOptional = repo.findById(documentId);

		if (documentOptional.isPresent()) {
			Document document = documentOptional.get();
			return document;
		} else {
			throw new DocumentNotFoundExcepxtion("Document not found.");
		}

	}

	public Posts readDocumentPosts(Long documentId) {

		Document document = getDocument(documentId);
		Long postId = document.getPostId();
		String url = thirdpartyurl + "/posts/" + postId;
		logger.info("url is " + url);
		return restTemplate.getForObject(url, Posts.class);

	}

	public List<Comments> readPostComments(Long documentId, Long postId) {
		
		

		List<Long> commentIds = commentRepo.findByPost(postId);
		List<Comments> commentsList = new ArrayList<>();

		for (int i = 0; i < commentIds.size(); i++) {
			String url = thirdpartyurl + "/comments/" + commentIds.get(i);
			Comments comment = restTemplate.getForObject(url, Comments.class);
			commentsList.add(comment);
		}

		return commentsList;

	}

	/*
	 * public Posts createPosts(Posts posts,Long documentId) throws
	 * PostCreationException { logger.info(" createPosts begin"); String
	 * url=postsurl+"/posts/"; HttpHeaders headers =new HttpHeaders();
	 * HttpEntity<Posts> requestEntity =new HttpEntity<>(posts,headers);
	 * headers.setContentType(MediaType.APPLICATION_JSON); ResponseEntity<Posts>
	 * responseEntity=restTemplate.postForEntity(url,requestEntity,Posts.class);
	 * if(responseEntity.getStatusCode() ==HttpStatus.CREATED) { Posts
	 * newPost=responseEntity.getBody(); Optional<Document> documentOptional =
	 * repo.findById(documentId); if (documentOptional.isPresent()) { Document
	 * document = documentOptional.get(); document.setPostId(newPost.getId());
	 * repo.save(document); } else {
	 * 
	 * throw new DocumentNotFoundExcepxtion("Document not found."); }
	 * logger.info(" createPosts begin"); return responseEntity.getBody(); }else {
	 * throw new PostCreationException("Failed to create new post"); }
	 * 
	 * 
	 * 
	 * }
	 */

}
