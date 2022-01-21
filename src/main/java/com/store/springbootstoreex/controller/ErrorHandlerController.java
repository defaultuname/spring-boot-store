package com.store.springbootstoreex.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/*
Контроллер, отвечающий за перехватку ошибок.
 */

@ControllerAdvice
public class ErrorHandlerController {

    @RequestMapping("/error")
    public void handleError(HttpServletRequest request) { // Метод, отвечающий за ловлю ошибок и передачу их методу-парсеру
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE); // Получаем статус-код ощибки

        if (status != null) {
            errorHandler(new Exception());
        }
    }

    @ExceptionHandler(value = Exception.class)
    private ModelAndView errorHandler(Exception e) { // Метод-персер отвечает за передачу ошибки в качестве аттрибута на Thymeleaf
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.setViewName("error"); // error.html - страница для ошибок
        return mav;
    }
}