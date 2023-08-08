package com.documents.mgmt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "Posts")
public class Posts {
	@Id
	@Column(name = "ID")
	private Long id;
	@Column(name = "TITLE")
	private String title;
	@Column(name = "BODY")
	private String body;
	
	public Long getId() {
		return id;
	}
	public Posts() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Posts(Long id, String title, String body) {
		super();
		this.id = id;
		this.title = title;
		this.body = body;
	}
	
	
	
	
	
	

}
