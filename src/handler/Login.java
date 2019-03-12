package handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import util.JSONUtil;
import util.ResponseUtil;
import util.ValidateUtil;

@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public Login() {
        super();
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject data = new JSONObject();
		String password = JSONUtil.getJSONObject(request).getString("password");
		
		if(ValidateUtil.validatePassword(password)) {
			request.getSession().setAttribute("isLogin", true);
			request.getSession().setAttribute("password", password);
			data.put("success", true);
		} else {
			data.put("success", false);
		}
		// 响应请求
		ResponseUtil.sendJSON(response, data);
	}

}
