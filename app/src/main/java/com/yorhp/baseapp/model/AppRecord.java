package com.yorhp.baseapp.model;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;
import com.yorhp.tyhjlibrary.util.common.TimeUtil;

@Table("app_record")
public class AppRecord {

    public AppRecord(long logTime, long outTime) {
        this.logTime = logTime;
        this.outTime = outTime;
    }

    //登录时间
    long logTime;

    //退出时间
    long outTime;

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    int recordId;


    public long getLogTime() {
        return logTime;
    }

    public void setLogTime(long logTime) {
        this.logTime = logTime;
    }

    public long getOutTime() {
        return outTime;
    }

    public void setOutTime(long outTime) {
        this.outTime = outTime;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }


    public String getLogTimeStr() {
        return TimeUtil.getTimeTxt(logTime);
    }

    public String getSpendTimeStr() {
        return TimeUtil.getTimeSpend(logTime, outTime);
    }


}
