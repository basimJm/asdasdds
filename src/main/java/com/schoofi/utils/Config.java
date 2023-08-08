package com.schoofi.utils;

import com.schoofi.constants.AppConstants;

public interface Config {

	// used to share GCM regId with application server - using php app server
	static final String APP_SERVER_URL = "http://192.168.1.17/gcm/gcm.php?shareRegId=1";

	// GCM server using java
	// static final String APP_SERVER_URL =
	// "http://192.168.1.17:8080/GCM-App-Server/GCMNotification?shareRegId=1";

	// Google Project Number
	static final String GOOGLE_PROJECT_ID = "512218038480";
	static final String MESSAGE_KEY = "message";

	public static final String FILE_UPLOAD_URL = AppConstants.SERVER_URLS.SERVER_URL+"fileUpload.php";

	// Directory name to store captured images and videos
	public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";

}
