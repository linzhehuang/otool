package thread;

import java.util.ArrayList;
import java.util.List;

public class ThreadTest {
	public static void main(String[] args) {
		List<Object> backupFileList = new ArrayList<Object>();
		backupFileList.add("/u01/app/oracle/oradata/XE/system.dbf");
		// new BackupThread(backupFileList, "/u01/app/oracle/oradata/").run();
	}
}
