package com.ls.model;

import java.util.Date;

/**
 * @program: db-parser-component
 * @author: lishuai
 * @create: 2018-12-04
 */
public class Table {
    public String tableName;
    public String engine;
    public String tableComment;
    public Date createTime;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    @Override
    public String toString() {
        return "Table{" +
                "tableName='" + tableName + '\'' +
                ", engine='" + engine + '\'' +
                ", tableComment='" + tableComment + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
