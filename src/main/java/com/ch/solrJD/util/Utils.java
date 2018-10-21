package com.ch.solrJD.util;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @Auther: pch
 * @Date: 2018/10/21 16:36
 * @Description:
 */
public class Utils {
	public static void responseFile(HttpServletResponse response, File file) throws IOException {
		if (response == null || file == null || !file.exists()) {
			return;
		}
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		ServletOutputStream outputStream;
		try {
			outputStream = response.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		int len = -1;
		byte[] buffer = new byte[1024];
		while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
			outputStream.write(buffer, 0, len);
			outputStream.flush();
		}
		outputStream.close();
		inputStream.close();
	}
}
