package handler;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import database.DB;
import database.DBUtil;
import thread.BackupThread;
import util.JSONUtil;
import util.ResponseUtil;

@WebServlet("/Backup")
public class Backup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public Backup() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBUtil dbUtil = DB.getDBUtil(request);
		JSONObject data = new JSONObject();
		JSONObject requestData = JSONUtil.getJSONObject(request);
		String backupPath = requestData.getString("backupPath");
		// 获取备份文件列表
		List<Object> backupFileList = generateBackupFileList(dbUtil);
		List<Object> dstFileList = generateDstFileList(backupFileList, backupPath);
		show(backupFileList);
		show(dstFileList);
		// 开始备份
		new BackupThread(backupFileList, backupPath, dbUtil).run();
		// 添加数据
		data.put("success", true);
		data.put("backupFileList", new JSONArray(backupFileList));
		data.put("dstFileList", new JSONArray(dstFileList));
		// 响应请求
		ResponseUtil.sendJSON(response, data);
		// 关闭数据库连接
		dbUtil.destoryConnection();
	}
	private List<Object> generateBackupFileList(DBUtil dbUtil) {
		String sql = "", columnName = "";
		ResultSet resultSet = null;
		ArrayList<Object> list = new ArrayList<Object>();
		// 
		String[][] sqlList = {
				{"name","SELECT name FROM v$datafile"},
				{"name","SELECT name FROM v$controlfile"},
		};
		for(int i = 0;i < sqlList.length;i++) {
			columnName = sqlList[i][0];
			sql = sqlList[i][1];
			System.out.println(sql);
			resultSet = dbUtil.getQueryResultSet(sql);
			list.addAll(JSONUtil.getList(resultSet, columnName));
		}
		return list;
	}
	private List<Object> generateDstFileList(List<Object> backupFileList, String backupPath) {
		String backup = "";
		if(File.separator.equals(backupPath.substring(backupPath.length()-1))) {
			backup = backupPath;
		} else {
			backup = backupPath + File.separator;
		}
		 List<Object> list = new ArrayList<Object>();
		for(int i = 0;i < backupFileList.size();i++) {
			String srcFileName = (String)backupFileList.get(i);
			String[] arr = srcFileName.split(File.separator);
			String fileName = arr[arr.length-1];
			String dstFileName = backup + fileName;
			list.add(dstFileName);
		}
		return list;
	}
	// debug
	private void show(List<Object> list) {
		for(int i = 0;i < list.size();i++) {
			String item = (String)list.get(i);
			System.out.println(item);
		}
	}
}
