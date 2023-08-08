package com.documents.mgmt.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDeleteAction;

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

	public Document( String fileName, String fileDownloadUri, String fileType, long size) {
		super();
		
		this.fileName = fileName;
		this.fileDownloadUri = fileDownloadUri;
		this.fileType = fileType;
		this.size = size;
	}
	
	public Document(String fileName, String fileDownloadUri, String fileType, long size, Long postId,List<Long> commentId) {
		super();
		
		this.fileName = fileName;
		this.fileDownloadUri = fileDownloadUri;
		this.fileType = fileType;
		this.size = size;
		this.postId = postId;
		this.commentIds=commentId;
	}
	

	public Document(String fileName, String fileDownloadUri, String fileType, long size, Long postId) {
		super();
		
		this.fileName = fileName;
		this.fileDownloadUri = fileDownloadUri;
		this.fileType = fileType;
		this.size = size;
		this.postId = postId;
	}



	@Column(name = "FILE_NAME")
	private String fileName;
	@Column(name = "DOWNLOAD_URL")

	private String fileDownloadUri;
	@Column(name = "FILE_TYPE")
	private String fileType;
	@Column(name = "SIZE")
	private long size;

	@Column(name = "POST")
	private Long postId;
	
	 @ElementCollection
	 @CollectionTable(name="Comments", joinColumns=@JoinColumn(name="id"))
	 @Column(name="COMMENTS")
	private List<Long> commentIds;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileDownloadUri() {
		return fileDownloadUri;
	}

	public void setFileDownloadUri(String fileDownloadUri) {
		this.fileDownloadUri = fileDownloadUri;
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

	
}
