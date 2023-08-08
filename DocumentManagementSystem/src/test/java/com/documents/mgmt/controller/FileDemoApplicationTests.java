package com.documents.mgmt.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.documents.mgmt.controller.DocumentController;
import com.documents.mgmt.model.Comments;
import com.documents.mgmt.model.Document;
import com.documents.mgmt.model.Posts;
import com.documents.mgmt.service.DocumentService;

@RunWith(SpringRunner.class)
@WebMvcTest(DocumentController.class)
public class FileDemoApplicationTests {

	@Test
	public void contextLoads() {
	}

	@MockBean
	private DocumentController documentController;

	@MockBean
	private DocumentService documentService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(documentController).build();
	}

	@Test
	@WithMockUser(username = "user", password = "password")
	public void testGetAllDocuments() throws Exception {
		List<Document> mockDocsList = new ArrayList<>();

		Mockito.when(documentService.loadAllDocs()).thenReturn(mockDocsList);

		mockMvc.perform(get("/documents").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		
	}
	
	@Test
	@WithMockUser(username = "user", password = "password")
    public void testUploadPdfFile() throws Exception {
        // Mock the behavior of documentService.uploadFile
        when(documentService.uploadFile(Mockito.any()))
            .thenReturn("File uploaded successfully");

        mockMvc = MockMvcBuilders.standaloneSetup(documentController).build();

        // Load a sample PDF file from resources
        InputStream inputStream = getClass().getResourceAsStream("C:\\Jyothi\\test-files\\test.pdf");
        MockMultipartFile multipartFile = new MockMultipartFile(
            "file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, inputStream);

        // Perform the POST request to /documents
        mockMvc.perform(MockMvcRequestBuilders.multipart("/documents")
            .file(multipartFile))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
	

    @Test
    public void testDeleteFile() throws Exception {
        Long documentId = 123L;

        // Mock the behavior of documentService.delete
        when(documentService.delete(documentId))
            .thenReturn(true);

        mockMvc = MockMvcBuilders.standaloneSetup(documentController).build();

        // Perform the DELETE request to /documents/{documentId}
        mockMvc.perform(MockMvcRequestBuilders.delete("/documents/{documentId}", documentId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    public void testAddPost() throws Exception {
        Long documentId = 1L;
        Long postId = 1L;

        // Mock the behavior of documentService.associateDocumentWithPost
        doNothing().when(documentService).associateDocumentWithPost(documentId, postId);

        mockMvc = MockMvcBuilders.standaloneSetup(documentController).build();

        // Perform the PUT request to /{documentId}/posts/{postId}
        mockMvc.perform(MockMvcRequestBuilders.put("/{documentId}/posts/{postId}", documentId, postId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    
    
    @Test
    public void testReadDocumentPosts() throws Exception {
        Long documentId = 1L;
        Posts mockPost = new Posts(); // Create a mock Posts object

        // Mock the behavior of documentService.readDocumentPosts
        when(documentService.readDocumentPosts(documentId))
            .thenReturn(mockPost);

        mockMvc = MockMvcBuilders.standaloneSetup(documentController).build();

        // Perform the GET request to /posts/{documentId}
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{documentId}", documentId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk()); // You can adjust the expected status
    }

   

    @Test
    public void testAddComments() throws Exception {
        Long documentId = 1L;
        Long postId = 1L;
        Long commentId=1L;

        // Mock the behavior of documentService.associateDocumentWithPost
        doNothing().when(documentService).addCommentsToPost(documentId, postId,commentId);

        mockMvc = MockMvcBuilders.standaloneSetup(documentController).build();

        // Perform the PUT request to /{documentId}/posts/{postId}
        mockMvc.perform(MockMvcRequestBuilders.put("/{documentId}/comments/{postId}/{commentId}", documentId, postId,commentId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    public void testReadPostComments() throws Exception {
        Long documentId = 1L;
        Long postId = 1L;

        List<Comments> mockCommentsList = new ArrayList<>(); // Create a mock list of Comments
        // Add mock Comments objects to the list

        // Mock the behavior of documentService.readPostComments
        when(documentService.readPostComments(documentId, postId))
            .thenReturn(mockCommentsList);

        mockMvc = MockMvcBuilders.standaloneSetup(documentController).build();

        // Perform the GET request to /comments/{documentId}/{postId}
        mockMvc.perform(MockMvcRequestBuilders.get("/comments/{documentId}/{postId}", documentId, postId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk()); // Use isCreated() for status 201
             // Assuming Comments is an array
    }
    
    
	
	

}
