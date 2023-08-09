package com.documents.mgmt.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.documents.mgmt.exception.CommentAddException;
import com.documents.mgmt.exception.CommentNotFoundException;
import com.documents.mgmt.exception.DatabaseSaveException;
import com.documents.mgmt.exception.DocumentNotFoundExcepxtion;
import com.documents.mgmt.exception.PostCreationException;
import com.documents.mgmt.exception.PostNotFoundException;
import com.documents.mgmt.model.Comments;
import com.documents.mgmt.model.Document;
import com.documents.mgmt.model.Posts;
import com.documents.mgmt.service.DocumentService;

@RestController
public class DocumentController {

	private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

	@Autowired
	private DocumentService documentService;

	@PostMapping("/documents")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

		logger.info("uploadFile begin");
		Long id = documentService.uploadFile(file);

		if (id == null) {
			logger.error("uploadFile failed !! check logs for more details.");
			throw new DatabaseSaveException();
		} else {
			logger.info("uploadFile end");
			return new ResponseEntity<>("Upload successful for id :" + id, HttpStatus.CREATED);
		}

	}

	@DeleteMapping("/documents/{documentId}")
	public ResponseEntity<String> deleteFile(@PathVariable(required = true, name = "documentId") Long documentId) {
		String message = "";
		logger.info("deleteFile begin");

		boolean existed = documentService.delete(documentId);

		if (existed) {
			message = "Delete the file successfully: " + documentId;
			logger.info("deleteFile end");
			return ResponseEntity.status(HttpStatus.OK).body(message);
		} else {
			logger.error("deleteFile failed !! check logs for more details.");
			throw new DocumentNotFoundExcepxtion();
		}

	}

	@GetMapping("/documents")
	public ResponseEntity<List<Document>> getAllDocuments() {
		logger.info("getListFiles begin");
		List<Document> docsList = documentService.loadAllDocs();
		if (docsList.size() > 0) {
			logger.info("getListFiles end");
			return ResponseEntity.status(HttpStatus.OK).body(docsList);
		} else {
			logger.error("getListFiles is empty !! check logs for more details.");
			throw new DocumentNotFoundExcepxtion();
		}

	}

	@GetMapping("/posts/{documentId}")
	public ResponseEntity<Posts> readDocumentPosts(
			@PathVariable(required = true, name = "documentId") Long documentId) {
		logger.info("readDocumentPosts begin");
		Posts newPost = documentService.readDocumentPosts(documentId);
		
		if(newPost!=null) {
			logger.info("readDocumentPosts end");
			return ResponseEntity.status(HttpStatus.OK).body(newPost);
		}else {
			logger.info("readDocumentPosts error !! check logs for more details.");
			throw new PostNotFoundException();
		}
		
	}

	@PutMapping("/{documentId}/posts/{postId}")
	public ResponseEntity<String> addPost(@PathVariable(required = true, name = "documentId") Long documentId,
			@PathVariable(required = true, name = "postId") Long postId) {
		logger.info("addPost begin");
		Long id =documentService.associateDocumentWithPost(documentId, postId);
		
		if(id!=null) {
			logger.info("addPost end");
			return ResponseEntity.ok("Document associated with Post successfully!");
		}else {
			logger.error("post creation error !! check logs for more details.");
			throw new PostCreationException();
		}
	}

	@GetMapping("/comments/{documentId}/{postId}")
	public ResponseEntity<List<Comments>> readPostComments(
			@PathVariable(required = true, name = "documentId") Long documentId,
			@PathVariable(required = true, name = "postId") Long postId) {
		logger.info("readPostComments begin");
		List<Comments> commentsList = documentService.readPostComments(documentId, postId);
		
		if (commentsList.size() > 0) {
			logger.info("readPostComments end");
			return ResponseEntity.status(HttpStatus.OK).body(commentsList);
		} else {
			logger.error("readPostComments error!! check logs for more details.");
			throw new CommentNotFoundException();
		}
		
	}

	@PutMapping("/{documentId}/comments/{postId}/{commentId}")
	public ResponseEntity<String> addComment(@PathVariable(required = true, name = "documentId") Long documentId,
			@PathVariable(required = true, name = "postId") Long postId,
			@PathVariable(required = true, name = "commentId") Long commentId) {
		logger.info("addComment begin");
		Long id=documentService.addCommentsToPost(documentId, postId, commentId);
		if(id!=null) {
			logger.info("addComment end");
			return ResponseEntity.ok("Document associated with Post successfully!");
		}else {
			logger.error("comment creation error!! check logs for more details.");
			throw new CommentAddException();
		}
		
		
	}

}
