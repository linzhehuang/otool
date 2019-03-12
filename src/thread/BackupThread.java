package thread;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import database.DBUtil;
import util.FileUtil;

public class BackupThread extends Thread {
	List<Object> backupFileList = null;
	String backupPath = "";
	DBUtil dbUtil = null;
	public BackupThread(List<Object> backupFileList,String backupPath,DBUtil dbUtil) {
		this.backupFileList = backupFileList;
		if(File.separator.equals(backupPath.substring(backupPath.length()-1))) {
			this.backupPath = backupPath;
		} else {
			this.backupPath = backupPath + File.separator;
		}
		this.dbUtil = dbUtil;
	}
	@Override
	public void run() {
		// 关闭数据库
		//dbUtil.shutdown();
		// 开始备份文件
		for(int i = 0;i < backupFileList.size();i++) {
			String srcFileName = (String)backupFileList.get(i);
			String[] arr = srcFileName.split(File.separator);
			String fileName = arr[arr.length-1];
			String dstFileName = backupPath + fileName;
			System.out.println(srcFileName);
			System.out.println(dstFileName);
			try {
				FileUtil.copy(srcFileName, dstFileName);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		// 启动数据库
		// dbUtil.startup();
	}
}
