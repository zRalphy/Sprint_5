package org.openapitools.api;

import jakarta.annotation.Generated;

@Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-03-03T12:42:51.561159176Z[GMT]")
public class ApiException extends Exception {
    private int code;
    public ApiException(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
