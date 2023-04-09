package com.seven.wqb.service.handler;

import com.seven.wqb.domain.JsonResponse;
import com.seven.wqb.domain.exception.ConditionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CommonGlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResponse<String> commonExceptionHandler(HttpServletRequest request, Exception e) {
        String msg = e.getMessage();
        if (e instanceof ConditionException) {
            String errCode = ((ConditionException)e).getCode();
            return new JsonResponse<>(errCode, msg);
        } else {
            return new JsonResponse<>("500", msg);
        }
    }
}
