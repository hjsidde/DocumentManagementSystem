package com.documents.mgmt.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.documents.mgmt.model.Comments;



public interface CommentRepo extends JpaRepository<Comments, Long>{ 
	
	@Query(value="select id from Comments where POSTID=:postId",nativeQuery=true) 
	List<Long> findByPost(@Param("postId") Long postId);

}