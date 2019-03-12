package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONUtil {
	public static JSONArray getJSONArray(ResultSet resultSet, String columnName) {
		return new JSONArray(getList(resultSet, columnName));
	}
	public static List<Object> getList(ResultSet resultSet, String columnName) {
		List<Object> list = new ArrayList<Object>();
		try {
			if(!resultSet.first()) return list;
			do {
				list.add(resultSet.getObject(columnName));
			} while(resultSet.next());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public static JSONObject getJSONObject(HttpServletRequest request) throws UnsupportedEncodingException, IOException {
		// 读取请求内容  
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));  
        String line = null;  
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);  
        }  
        //将JSON字符串转换为JSON对象
        JSONObject jsonObject = new JSONObject(sb.toString());
        return jsonObject;
	}
}
