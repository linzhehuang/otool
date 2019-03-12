var curBackupFileIndex = 0,timer;
var backupFileList,dstFileList;
// 获取备份路径
function getBackupPath() {
	return $("backupPathInput").value;
}
// 开始备份
function beginBackup() {
	// 获取数据
	var backupPath = getBackupPath();
	// TODO 数据合法性检测
	
	//
	ajax({
		url: "./Backup",
		data: {
			"backupPath" : backupPath
		},
		success: function(xhr) {
	      var response = convertJSONString(xhr.responseText);
	      if(response.success) {
	    	  // 开始备份成功，则请求其备份进度
	    	  backupFileList = response.backupFileList;
	    	  dstFileList = response.dstFileList;
	    	  console.log("begin");
	    	  timer = setInterval("getBackupStatus(backupFileList, dstFileList);", 1000);
	      } else {
	    	  
	      }
	    }
	});
}
function getBackupStatus(backupFileList, dstFileList) {
	console.log("running");
	var curBackupSrcFile = backupFileList[curBackupFileIndex];
	var curBackupDstFile = dstFileList[curBackupFileIndex];
	ajax({
		url: "./GetBackupStatus",
		data: {
			"srcFileName" : curBackupSrcFile,
			"dstFileName" : curBackupDstFile
		},
		success: function(xhr) {
	      var response = convertJSONString(xhr.responseText);
	      var status = response.status;
	      if(status == 100) {
	    	  curBackupFileIndex++;
	    	  // 备份完成，停止轮询
	    	  if(curBackupFileIndex == (backupFileList.length-1)) clearInterval(timer);
	      }
	      // 更改视图
	      console.log("current = " + curBackupFileIndex);
	      console.log(status);
	    }
	});
}