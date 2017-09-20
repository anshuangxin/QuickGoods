package gly.quickgoods.modle;


import gly.quickgoods.constants.CommConstants;

/**
 * Restful接口统一返回响应类
 *
 * @param <T>
 * @author liwei
 */
public class WSResult<T> {
    private int code = 1; // 响应码，0：成功，1：失败，可自定义其他响应码
    private String msg = "fail"; // 响应消息：一般设置失败原因等
    private T result; // 查询类接口设置查询结果列表

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
        if (code == CommConstants.SUCCESS) {
            msg = "success";
        } else {
            msg = "fail";
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
