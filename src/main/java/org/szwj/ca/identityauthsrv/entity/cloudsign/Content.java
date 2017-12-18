package org.szwj.ca.identityauthsrv.entity.cloudsign;

public class Content {

    String uuid;

    String outFileId;

    String fileBase64Str;

    String fileExt;

    String fileName;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOutFileId() {
        return outFileId;
    }

    public void setOutFileId(String outFileId) {
        this.outFileId = outFileId;
    }

    public String getFileBase64Str() {
        return fileBase64Str;
    }

    public void setFileBase64Str(String fileBase64Str) {
        this.fileBase64Str = fileBase64Str;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
