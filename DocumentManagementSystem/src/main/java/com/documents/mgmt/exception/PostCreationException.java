package com.documents.mgmt.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class PostCreationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public PostCreationException(String message) {
        super(message);
    }

    public PostCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
