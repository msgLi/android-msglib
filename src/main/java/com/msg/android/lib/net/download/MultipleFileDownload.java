package com.msg.android.lib.net.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MultipleFileDownload {

	public interface IFileDownloadCallback {

		public static final int STATUS_FAIL = -1;
		public static final int STATUS_START = 0;
		public static final int STATUS_CONNECTED = 1;
		public static final int STATUS_PAUSE = 2;
		public static final int STATUS_PROGRESS = 3;
		public static final int STATUS_COMPLETE = 4;
		public static final int ERR_NET_CUT = 0;
		public static final int ERR_USER_CANCEL = 1;

		void onDownloadStart(int dID);

		void onConnected(int dID, long total, boolean isRangeSupport);

		void onPaused(int dID);

		void onPercentage(int dID, float total, float curr);

		void onComplete(int dID, String url, String localFile);

		void onFaild(int dID, float percent, int errCode);
	}

	public static class DownloadTask {

		public IFileDownloadCallback callback;
		public String urlStr;
		public int dID;
		public int fileSize;
		public String fileName;
		public String filePath;

		@Override
		public int hashCode() {
			return urlStr.hashCode() & filePath.hashCode();
		}

		@Override
		public boolean equals(Object o) {
			// TODO Auto-generated method stub
			if (o instanceof DownloadTask) {
				DownloadTask task = (DownloadTask) o;
				if (this.urlStr.equals(task.urlStr) && this.filePath.equals(task.filePath)) {
					return true;
				} else {
					return super.equals(o);
				}
			} else {
				return super.equals(o);
			}

		}

		public void clear(){
			//callback = null;
		}
	}
	
	//public static String fileDownloadDir = Environment.getExternalStorageDirectory().getAbsolutePath();

	private static Map<DownloadTask, FileDownloadThread> downloadTaskList = new HashMap<DownloadTask, FileDownloadThread>();
	
	public static void startDownloadTask(String url,String fileName,String filePath,IFileDownloadCallback callBack){
		DownloadTask task = new DownloadTask();
		task.callback = callBack;
		task.dID = 0;
		task.fileName = fileName;
		task.filePath = filePath;
		task.urlStr = url;
		FileDownloadThread downloadThread = getTaskThread(task);
		if(downloadThread != null){
			downloadThread.pauseDownload();
			downloadThread.task.clear();
			removeTask(task);
		}
		downloadThread = new FileDownloadThread(task);
		addTask(task,downloadThread);
		downloadThread.start();
	}
	
	public static void pauseDownloadTask(String url,String filePath){
		DownloadTask task = new DownloadTask();
		task.dID = 0;
		File f = new File(filePath);
		task.fileName = f.getName();
		task.filePath = filePath;
		task.urlStr = url;
		FileDownloadThread downloadThread = getTaskThread(task);
		if(downloadThread != null){
			downloadThread.pauseDownload();
			removeTask(task);
		}
	}
	
	private static synchronized void addTask(DownloadTask task,FileDownloadThread downloadThread){
		synchronized (MultipleFileDownload.class) {
			downloadTaskList.put(task, downloadThread);
		}
	}

	private static synchronized DownloadTask getTask(DownloadTask task){
		synchronized (MultipleFileDownload.class) {
			Set<DownloadTask> keys = downloadTaskList.keySet();
			for(DownloadTask t : keys){
				if(t.equals(task)){
					return t;
				}
			}
			return null;
		}
	}
	
	private static synchronized FileDownloadThread getTaskThread(DownloadTask task){
		synchronized (MultipleFileDownload.class) {
			return downloadTaskList.get(task);
		}
	}
	
	private static synchronized void removeTask(DownloadTask task){
		synchronized (MultipleFileDownload.class) {
			FileDownloadThread taskThread = downloadTaskList.get(task);
			if(taskThread != null){
				taskThread.task.clear();
				downloadTaskList.remove(task);
			}
		}
	}
	
	private static long getFileSize(DownloadTask task){
		try {
			URL url = new URL(task.urlStr);  
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();  
			connection.setConnectTimeout(5000);  
			connection.setRequestMethod("GET");  
			long fileSize = connection.getContentLength();  
			connection.disconnect();
			return fileSize;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		} 
	}
	
	private static class FileDownloadThread extends Thread{
		private DownloadTask task;
		
		private volatile boolean runFlag = true;

		public FileDownloadThread(DownloadTask task) {
			super();
			this.task = task;
		}
		
		public void pauseDownload(){
			runFlag = false;
			if(task.callback != null){
				task.callback.onPaused((int)getId());
			}
		}

		public void run(){
			runFlag = true;
			try {
				task.callback.onDownloadStart((int)getId());
				URL url = new URL(task.urlStr);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				File f = new File(task.filePath+"_tmp");
				long completeSize = 0;
				if(f.exists()){
					FileInputStream fis = new FileInputStream(f);
					completeSize = fis.available();
					fis.close();
				}else{
					f.createNewFile();
				}
				long fileSize = getFileSize(task);
				if(fileSize <= 0){
					removeTask(task);
					if(task.callback != null) {
						task.callback.onFaild((int) getId(), 0, 0);
					}
					return;
				}
				conn.setConnectTimeout(5*1000);
	            conn.setRequestMethod("GET");
	            conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
	            conn.setRequestProperty("Accept-Language", "zh-CN");
	            conn.setRequestProperty("Referer", task.urlStr); 
	            conn.setRequestProperty("Charset", "UTF-8");
	            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
	            conn.setRequestProperty("Connection", "Keep-Alive");
	            conn.setRequestProperty("Range", "bytes="+completeSize + "-" + fileSize); 
	            conn.connect();
	            
	            if(conn.getResponseCode() == 200 || conn.getResponseCode() == 206){
	            	Map<String, List<String>> headers = conn.getHeaderFields();
	            	if(headers.get("Accept-Ranges") != null){
	            		if(headers.get("Accept-Ranges").contains("bytes")){
							if(task.callback != null) {
								task.callback.onConnected((int) getId(), fileSize, true);
							}
	            		}else{
							if(task.callback != null) {
								task.callback.onConnected((int) getId(), fileSize, false);
							}
	            		}
	            	}else{
	            		task.callback.onConnected((int)getId(), fileSize, false);
	            	}
	            	RandomAccessFile fileController = new RandomAccessFile(task.filePath+"_tmp", "rwd");
	            	fileController.seek(completeSize);
	            	InputStream is = conn.getInputStream();
	            	byte[] buffer = new byte[1024];
	            	int length = -1;
	            	while((length = is.read(buffer)) > 0 && runFlag){
	            		fileController.write(buffer, 0, length);  
	            		completeSize += length;
						if(task.callback != null){
							task.callback.onPercentage((int)getId(), fileSize, completeSize);
						}
	            	}
	            	if(completeSize == fileSize || length == 0){
						File apkFile = new File(task.filePath);
						if(!apkFile.exists()){
							apkFile.createNewFile();
						}
						f.renameTo(apkFile);
						f.delete();
						if(task.callback != null) {
							task.callback.onComplete((int) getId(), task.urlStr, task.filePath);
						}
	            		removeTask(task);
	            	}
	            	fileController.close();
	            	conn.disconnect();
	            }else{
					if(task.callback != null) {
						task.callback.onFaild((int) getId(), 0, 0);
					}
	            }
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(task.callback != null) {
					task.callback.onFaild((int) getId(), 0, 0);
				}
				removeTask(task);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(task.callback != null) {
					task.callback.onFaild((int) getId(), 0, 0);
				}
				removeTask(task);
			}
			
		}
	}
	
	
}
