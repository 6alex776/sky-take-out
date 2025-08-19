package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReportVO implements Serializable {

    //日期，以逗号分隔，例如：2022-10-01,2022-10-02,2022-10-03
    private String dateList;

    //用户总量，以逗号分隔，例如：200,210,220
    private String totalUserList;

    //新增用户，以逗号分隔，例如：20,21,10
    private String newUserList;

    /**
     * getter和setter方法
     */
    public String getDateList() {
        return dateList;
    }

    public void setDateList(String dateList) {
        this.dateList = dateList;
    }

    public String getTotalUserList() {
        return totalUserList;
    }

    public void setTotalUserList(String totalUserList) {
        this.totalUserList = totalUserList;
    }

    public String getNewUserList() {
        return newUserList;
    }

    public void setNewUserList(String newUserList) {
        this.newUserList = newUserList;
    }

    @Override
    public String toString() {
        return "UserReportVO{" +
                "dateList='" + dateList + '\'' +
                ", totalUserList='" + totalUserList + '\'' +
                ", newUserList='" + newUserList + '\'' +
                '}';
    }
}