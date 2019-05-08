package com.test.crawler.entity;

/**
 * @Author: LIUQI
 * @Date: 2019/5/7 20:00
 * @Description: todo
 */
public class ResultMsg {

    private String code;
    private String message;

    public static ResultMsg ok(){
        return new ResultMsg("000","提交成功");
    }

    public ResultMsg(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
