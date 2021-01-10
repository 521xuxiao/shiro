package com.shiro.shiro.returnData;

public class ReturnData {
    private String resultMessage = "成功";
    private Object result;
    private Boolean success = true;

    public ReturnData() {
    }

    public ReturnData(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public ReturnData(Object result) {
        this.result = result;
    }

    public ReturnData(Throwable e) {
        this.success=false;
        this.resultMessage=e.getMessage();
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "ReturnData{" +
                "resultMessage='" + resultMessage + '\'' +
                ", result=" + result +
                ", success=" + success +
                '}';
    }
}
