package com.school.bean;

/**
 * 软件版本信息对象
 * 
 * @author Royal
 * 
 */
public class VersionInfo {

	// 版本描述字符串
	private String version;
	// 版本更新时间
	private String updateTime;
	// 新版本更新下载地址
	private String downloadURL;
	// 更新描述信息
	private String displayMessage;
	// apk名称
	private String apkName;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getDownloadURL() {
		return downloadURL;
	}

	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}

	public String getDisplayMessage() {
		return displayMessage;
	}

	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

}
