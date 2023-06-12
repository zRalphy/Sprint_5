package org.openapitools.api;

import jakarta.annotation.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;

@Getter
@RequiredArgsConstructor
@Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-03-03T12:42:51.561159176Z[GMT]")
public class ApiException extends Exception {
	@Serial
	private static final long serialVersionUID = -5253475372098714468L;
	private final int code;
}
