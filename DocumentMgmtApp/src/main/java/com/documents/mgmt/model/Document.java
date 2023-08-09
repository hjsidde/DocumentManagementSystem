package com.documents.mgmt.model;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;

@Entity(name = "Document")
public class Document {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	public Document() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public List<Long> getCommentId() {
		return commentIds;
	}

	public void setCommentId(List<Long> commentId) {
		this.commentIds = commentId;
	}

	@Column(name = "FILE_NAME")
	private String fileName;

	@Column(name = "FILE_TYPE")
	private String fileType;
	@Column(name = "SIZE")
	private long size;

	@Column(name = "POST")
	private Long postId;

	@Lob
	@Column(name = "CONTENTS")
	private byte[] data;

	@ElementCollection
	@CollectionTable(name = "Comments", joinColumns = @JoinColumn(name = "id"))
	@Column(name = "COMMENTS")
	private List<Long> commentIds;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Document(Long id, String fileName, String fileType, long size, Long postId, byte[] data,
			List<Long> commentIds) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.fileType = fileType;
		this.size = size;
		this.postId = postId;
		this.data = data;
		this.commentIds = commentIds;
	}

	public Document(String fileName, String fileType, long size, byte[] data) {
		super();
		
		this.fileName = fileName;
		this.fileType = fileType;
		this.size = size;
		this.data = data;
	}

	public Document(Long id, String fileName, String fileType, long size, Long postId, byte[] data) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.fileType = fileType;
		this.size = size;
		this.postId = postId;
		this.data = data;
	}
	
	
	
	

}
