package com.itemManagement.payload;

import com.itemManagement.enums.OperationStatusEnum;
import org.springframework.http.HttpStatus;

public class ResponseMessage {

    private HttpStatus status;
    private OperationStatusEnum operationStatusEnum;
    private String message;
    private Object body;

    public ResponseMessage(){};

    public ResponseMessage(HttpStatus status, OperationStatusEnum operationStatusEnum, String message, Object body) {
        this.status = status;
        this.operationStatusEnum = operationStatusEnum;
        this.message = message;
        this.body = body;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public OperationStatusEnum getOperationStatusEnum() {
        return operationStatusEnum;
    }

    public void setOperationStatusEnum(OperationStatusEnum operationStatusEnum) {
        this.operationStatusEnum = operationStatusEnum;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
