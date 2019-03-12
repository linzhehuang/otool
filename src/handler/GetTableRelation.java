package handler;

import java.io.IOException;
//import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.org.apache.bcel.internal.generic.Select;

import database.DB;
import database.DBUtil;
import util.JSONUtil;
*/

@WebServlet("/GetTableRelation")
public class GetTableRelation extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public GetTableRelation() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		DBUtil dbUtil = DB.getDBUtil(request);
		String sql = "";
		JSONObject tableNameObject = JSONUtil.getJSONObject(request);
		String tableName = tableNameObject.getString("tableName");
		
		// 获取列名列表
		sql = "SELECT column_name FROM all_tab_cols WHERE table_name=?";
		ResultSet resultSet = dbUtil.getQueryResultSet(sql, tableName);
		JSONArray columnNameList = JSONUtil.getJSONArray(resultSet, "column_name");
		// 获取
		sql = "SELECT all_cons_columns.tsable_name,column_name FROM ";
		//userName tableName
		sql = "SELECT b.table_name,b.column_name FROM all_constraints a,all_cons_columns b WHERE a.owner=? AND a.table_name=? AND a.constraint_type='R' AND a.constraint_name=b.constraint_name";
		*/
	}

}
