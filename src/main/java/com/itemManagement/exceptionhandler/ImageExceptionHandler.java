package com.itemManagement.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itemManagement.enums.OperationStatusEnum;
import com.itemManagement.payload.ResponseMessage;

@ControllerAdvice
public class ImageExceptionHandler {
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc, RedirectAttributes redirectAttributes) {
        ResponseMessage responseMessage = new ResponseMessage(
                HttpStatus.NOT_ACCEPTABLE,
                OperationStatusEnum.FAILED,
                "Image file size is too big",
                null
        );
        redirectAttributes.addFlashAttribute("responseMessage", responseMessage);
        return "redirect:/user/profile";
    }
}
