package com.bonc.dw3.entity;

/**
 * <p>Title: BONC -  DownloadFile</p>
 * <p>Description:  </p>
 * <p>Copyright: Copyright BONC(c) 2013 - 2025 </p>
 * <p>Company: 北京东方国信科技股份有限公司 </p>
 *
 * @author zhaojie
 * @version 1.0.0
 */
public class DownloadFile {

    private String fileId;
    private String filePath;
    private String fileDate;
    private String subjectName;
    private String dimensionName;
    private String downloadTime;
    private String isAvailable;
    private String userId;

    public DownloadFile(){}

    public DownloadFile(String fileId, String filePath, String fileDate, String subjectName, String dimensionName, String downloadTime, String isAvailable, String userId) {
        this.fileId = fileId;
        this.filePath = filePath;
        this.fileDate = fileDate;
        this.subjectName = subjectName;
        this.dimensionName = dimensionName;
        this.downloadTime = downloadTime;
        this.isAvailable = isAvailable;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "DownloadFile [fileId=" + fileId + ", filePath=" + filePath + ", fileDate="
                + fileDate + ", subjectName=" + subjectName + ", dimensionName="
                + dimensionName+",downloadTime=" + downloadTime + ",isAvailable=" + isAvailable
                +",userId="+userId+"]";
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getDimensionName() {
        return dimensionName;
    }

    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    public String getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(String downloadTime) {
        this.downloadTime = downloadTime;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



}
