package com.sintaks.mushandi.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class NotDeletedException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5455843470630454045L;

	public NotDeletedException(String message) {
        super(message);
    }
}
