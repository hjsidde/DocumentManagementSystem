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
		String status = documentService.uploadFile(file);

		logger.info("uploadFile end");

		return new ResponseEntity<>(status, HttpStatus.OK);

	}

	@DeleteMapping("/documents/{documentId}")
	public ResponseEntity<String> deleteFile(@PathVariable Long documentId) {
		String message = "";

		
			boolean existed = documentService.delete(documentId);
			logger.info("existed : "+existed);

			if (existed) {
				message = "Delete the file successfully: " + documentId;
				return ResponseEntity.status(HttpStatus.OK).body(message);
			} else {
				message = "The file does not exist!";
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
			}

		

	}

	@GetMapping("/documents")
	public ResponseEntity<List<Document>> getAllDocuments() {
		logger.info("getListFiles begin");
		List<Document> docsList = documentService.loadAllDocs();

		return ResponseEntity.status(HttpStatus.OK).body(docsList);
	}

	@GetMapping("/posts/{documentId}")
	public ResponseEntity<Posts> readDocumentPosts(@PathVariable Long documentId) {
		Posts newPost = documentService.readDocumentPosts(documentId);
		return ResponseEntity.status(HttpStatus.OK).body(newPost);
	}

	@PutMapping("/{documentId}/posts/{postId}")
	public ResponseEntity<String> addPost(@PathVariable Long documentId, @PathVariable Long postId) {
		documentService.associateDocumentWithPost(documentId, postId);
		return ResponseEntity.ok("Document associated with Post successfully!");
	}

	@GetMapping("/comments/{documentId}/{postId}")
	public ResponseEntity<List<Comments>> readPostComments(@PathVariable Long documentId, @PathVariable Long postId) {
		List<Comments> commentsList = documentService.readPostComments(documentId, postId);
		return ResponseEntity.status(HttpStatus.OK).body(commentsList);
	}

	@PutMapping("/{documentId}/comments/{postId}/{commentId}")
	public ResponseEntity<String> addComment(@PathVariable Long documentId, @PathVariable Long postId,
			@PathVariable Long commentId) {
		documentService.addCommentsToPost(documentId, postId, commentId);
		return ResponseEntity.ok("Document associated with Post successfully!");
	}

}
