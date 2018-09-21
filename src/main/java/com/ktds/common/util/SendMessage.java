package com.ktds.common.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class SendMessage {

	public static void send(HttpServletResponse response, String message) {
		try {
			PrintWriter writer = response.getWriter();
			writer.append(message);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
