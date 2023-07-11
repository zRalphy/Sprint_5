package org.openapitools.api;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

@Component
public class ApiUtil {
	public static void setExampleResponse(NativeWebRequest req, String contentType, String example) {
		try {
			HttpServletResponse res = req.getNativeResponse(HttpServletResponse.class);
			if (res != null) {
				res.setCharacterEncoding("UTF-8");
				res.addHeader("Content-Type", contentType);
				res.getWriter().print(example);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}