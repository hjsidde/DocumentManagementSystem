package com.documents.mgmt.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "Comments")
public class Comments {
	public Comments() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Id
	@Column(name = "ID")
	private Long id;
	@Column(name="POSTID")
	private Long postId;
	public Comments(Long id, Long postId, String name, String body, String email) {
		super();
		this.id = id;
		this.postId = postId;
		this.name = name;
		this.body = body;
		this.email = email;
	}
	public Long getPostId() {
		return postId;
	}
	public void setPostId(Long postId) {
		this.postId = postId;
	}
	@Column(name = "NAME")
	private String name;
	@Column(name = "BODY")
	private String body;
	@Column(name = "EMAIL")
	private String email;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "Comments [id=" + id + ", name=" + name + ", body=" + body + ", email=" + email + "]";
	}
	
	
	
	
	

}

