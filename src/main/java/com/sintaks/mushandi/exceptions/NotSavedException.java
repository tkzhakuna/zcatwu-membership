package com.sintaks.mushandi.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class NotSavedException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5455843470630454045L;

	public NotSavedException(String message) {
        super(message);
    }
}
