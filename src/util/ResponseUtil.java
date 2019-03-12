package util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class ResponseUtil {
	// 返回JSON数据
	public static void sendJSON(HttpServletResponse response, JSONObject data) throws IOException {
		PrintWriter writer = response.getWriter();
		writer.write(data.toString());
		response.setContentType("application/json");
		response.setStatus(200);
		response.flushBuffer();
		writer.close();
	}
}
