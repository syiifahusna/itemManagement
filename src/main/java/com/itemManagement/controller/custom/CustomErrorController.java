package com.itemManagement.controller.custom;

import com.itemManagement.exceptionhandler.ApiNotExistException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    @ResponseBody
    public String handleError(HttpServletRequest request) {
        // Determine the HTTP status code
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String requestURI = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            // If the request is for an API endpoint, return JSON
            if (requestURI != null && requestURI.startsWith("/api/")) {
               throw new ApiNotExistException(requestURI + " does not exist");
            }

            // Handle non-API requests by rendering the Thymeleaf error page
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "public/error";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "public/access_denied";
            }
        }

        // Default to Thymeleaf error page
        return "public/error";
    }


}
