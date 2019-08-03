package com.ls.hadoop.hdfs;

/**
 * @Author: lishuai
 * @CreateDate: 2018/9/9 18:28
 */
public class FIleBean {
    private int filetype;
    private String filename;
    private long filesize;

    public int getFiletype() {
        return filetype;
    }

    public void setFiletype(int filetype) {
        this.filetype = filetype;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FIleBean fIleBean = (FIleBean) o;

        if (filetype != fIleBean.filetype) return false;
        if (filesize != fIleBean.filesize) return false;
        return filename != null ? filename.equals(fIleBean.filename) : fIleBean.filename == null;
    }

    @Override
    public int hashCode() {
        int result = filetype;
        result = 31 * result + (filename != null ? filename.hashCode() : 0);
        result = 31 * result + (int) (filesize ^ (filesize >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "FIleBean{" +
                "filetype=" + filetype +
                ", filename='" + filename + '\'' +
                ", filesize=" + filesize +
                '}';
    }
}
