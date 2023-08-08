package com.documents.mgmt.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.documents.mgmt.model.Document;



public interface DocumentRepo extends JpaRepository<Document, Long>{ 
	
	
	  
	 

}
