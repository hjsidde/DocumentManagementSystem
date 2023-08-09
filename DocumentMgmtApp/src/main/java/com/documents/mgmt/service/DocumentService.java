package com.documents.mgmt.service;

import java.io.IOException;
import java.lang.annotation.Repeatable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.documents.mgmt.exception.DocumentNotFoundExcepxtion;
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

	public DocumentService() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Autowired
	DocumentRepo repo;

	@Autowired
	CommentRepo commentRepo;

	@Value("${document.thirdparty-url}")
	String thirdpartyurl;

	
	

	@Autowired
	RestTemplate restTemplate;

	public Long uploadFile(MultipartFile file) {
		logger.info("uploadFile begin");

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		Document uploadResponse = new Document();

		try {
			uploadResponse = repo.save(new Document(fileName, file.getContentType(), file.getSize(), file.getBytes()));
		} catch (IOException e) {

			logger.error("IOException : " + e.getLocalizedMessage());
		}

		Long id = uploadResponse.getId();

		logger.info("uploadFile end");

		return id;

	}

	@Transactional
	public boolean delete(Long documentId) {
		logger.info("delete begin");
		repo.deleteById(documentId);
		logger.info("delete begin");
		return true;

	}

	public List<Document> loadAllDocs() {
		logger.info("loadAllDocs begin");
		List<Document> filesList = repo.findAll();
		logger.info("loadAllDocs end");
		return filesList;

	}

	// associate document with post

	public Long associateDocumentWithPost(Long documentId, Long postId) {
		logger.info("associateDocumentWithPost begin");
		Optional<Document> documentOptional = getDocument(documentId);
		documentOptional.orElseThrow(DocumentNotFoundExcepxtion::new);
		Document document = documentOptional.get();
		document.setPostId(postId);
		Document updatedDocument = repo.save(document);
		logger.info("associateDocumentWithPost end");
		return updatedDocument.getId();

	}

	public Long addCommentsToPost(Long documentId, Long postId, Long commentId) {

		logger.info("addCommentsToPost begin");

		Optional<Document> documentOptional = getDocument(documentId);
		documentOptional.orElseThrow(DocumentNotFoundExcepxtion::new);
		Document document = documentOptional.get();
		Comments updatedCOmment;

		if (document.getPostId() != null && document.getPostId() == postId) {

			Comments comments = new Comments();
			comments.setPostId(postId);
			comments.setId(commentId);

			updatedCOmment = commentRepo.save(comments);
			List<Long> commentIdList =new ArrayList<>();
			commentIdList.add(updatedCOmment.getId());
			document.setCommentId(commentIdList);
			repo.save(document);

		} else {
			logger.error("post not found for postId : DocumentId:  "+postId +" : "+documentId);
			throw new PostNotFoundException();
		}

		logger.info("addCommentsToPost end");
		return updatedCOmment.getId();

	}

	public Optional<Document> getDocument(Long documentId) {
		return repo.findById(documentId);

	}

	public Posts readDocumentPosts(Long documentId) {

		Optional<Document> document = getDocument(documentId);
		document.orElseThrow(DocumentNotFoundExcepxtion::new);
		Long postId = document.get().getPostId();
		String url = thirdpartyurl + "/posts/" + postId;
		logger.info("url is " + url);
		return restTemplate.getForObject(url, Posts.class);

	}

	public List<Comments> readPostComments(Long documentId, Long postId) {
		logger.info("readPostComments begin");
		List<Long> commentIds = commentRepo.findByPost(postId);
		List<Comments> commentsList = new ArrayList<>();

		for (int i = 0; i < commentIds.size(); i++) {
			String url = thirdpartyurl + "/comments/" + commentIds.get(i);
			Comments comment = restTemplate.getForObject(url, Comments.class);
			if(comment==null) {
				logger.error("unable to get comment from comments service");
			}
			commentsList.add(comment);
		}
		logger.info("readPostComments end");
		return commentsList;

	}

}
