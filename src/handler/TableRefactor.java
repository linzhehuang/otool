package handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import database.DB;
import database.DBUtil;
import util.JSONUtil;

@WebServlet("/TableRefactor")
public class TableRefactor extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public TableRefactor() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBUtil dbUtil = DB.getDBUtil(request);
		JSONObject requestData = JSONUtil.getJSONObject(request);
		String tableName = requestData.getString("tableName");
		JSONArray columnSet = requestData.getJSONArray("columnSet");
		JSONObject da = requestData.getJSONObject("data");
		String pos = "";
		for(int i = 0;i < columnSet.length();i++) {
			String columnName = columnSet.getJSONObject(i).getString("columnName");
			String columnValue = String.valueOf(columnSet.getJSONObject(i).getInt("columnValue"));
			pos += " " + columnName + "=" +columnValue + " ";
			if(i != columnSet.length()-1) {
				pos += " AND "; 
			}
		}
		String change = da.getString("columnName") + "=?";
		//
		String sql = "UPDATE " + tableName + " SET " + change + " WHERE " + pos;
		System.out.println(sql);
		dbUtil.executeUpdate(sql, da.getString("columnValue"));
	}

}
