package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtil {
	
	public static void copy(String srcFileName,String dstFileName) throws FileNotFoundException {
		//TODO: Recode the parameters and file checkout.
		if(!isFileExist(srcFileName)) {
			throw new FileNotFoundException("The soucre file not found!");
		}
		
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		FileChannel fileInputChannel = null;
		FileChannel fileOutputChannel = null;
		try {
			fileInputStream = new FileInputStream(srcFileName);
            fileOutputStream = new FileOutputStream(dstFileName);
            fileInputChannel = fileInputStream.getChannel();
            fileOutputChannel = fileOutputStream.getChannel();
            // Copy the file with file channel.
            fileInputChannel.transferTo(0, fileInputChannel.size(), fileOutputChannel);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int getCopyState(String srcFileName, String dstFileName) {
		double dstFileSize = 0, srcFileSize = new File(srcFileName).length();
		dstFileSize = new File(dstFileName).length();
		return (int)((dstFileSize/srcFileSize)*100);
	}
	// 
	private static boolean isFileExist(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}
}
