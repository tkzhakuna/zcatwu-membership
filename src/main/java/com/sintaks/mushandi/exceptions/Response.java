package com.sintaks.mushandi.exceptions;

import lombok.Data;

@Data
public class Response {

    private String message;
    private int status;

    public Response(int status,String message) {
        this.status=status;
        this.message = message;
    }


}
