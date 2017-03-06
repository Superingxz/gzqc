package com.xolo.gzqc.bean.child;

/**
 * 自动更新
 */
public class UpDate {

    /**
     * versionno : 1.0.1
     * download_url : http://120.24.208.159/gzqiche/resources/download/gzqiche.apk
     * update_content : 测试
     */

    private String versionno;
    private String download_url;
    private String update_content;



    public String getVersionno() {
        return versionno;
    }

    public void setVersionno(String versionno) {
        this.versionno = versionno;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getUpdate_content() {
        return update_content;
    }

    public void setUpdate_content(String update_content) {
        this.update_content = update_content;
    }
}
