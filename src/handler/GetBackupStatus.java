package handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import util.FileUtil;
import util.JSONUtil;
import util.ResponseUtil;

@WebServlet("/GetBackupStatus")
public class GetBackupStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public GetBackupStatus() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject data = new JSONObject();
		JSONObject requestData = JSONUtil.getJSONObject(request);
		String srcFileName = requestData.getString("srcFileName");
		String dstFileName = requestData.getString("dstFileName");
		// 添加数据
		data.put("status", FileUtil.getCopyState(srcFileName, dstFileName));
		// 响应请求
		ResponseUtil.sendJSON(response, data);
	}

}
