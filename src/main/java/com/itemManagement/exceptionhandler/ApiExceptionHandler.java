package com.itemManagement.exceptionhandler;

import com.itemManagement.enums.OperationStatusEnum;
import com.itemManagement.payload.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiNotExistException.class)
    public ResponseEntity<ResponseMessage> handleApiNotExistException(ApiNotExistException ex) {
        ResponseMessage responseMessage = new ResponseMessage(HttpStatus.NOT_FOUND, OperationStatusEnum.FAILED,
                ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
    }

}
