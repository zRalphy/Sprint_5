package org.openapitools.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;

@Getter
@RequiredArgsConstructor
public class ApiException extends Exception {
	@Serial
	private static final long serialVersionUID = -5253475372098714468L;
	private final int code;
}
