package com.example.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /** 존재하지 않는 리소스 (게시글 없음 등) */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleIllegalArgument(IllegalArgumentException e, Model model) {
        model.addAttribute("status", 404);
        model.addAttribute("message", e.getMessage());
        return "error/error";
    }

    /** 정적 리소스 없음 (favicon 등) */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoResource(NoResourceFoundException e, Model model) {
        model.addAttribute("status", 404);
        model.addAttribute("message", "요청하신 리소스를 찾을 수 없습니다.");
        return "error/error";
    }

    /** 404 - 페이지 없음 */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NoHandlerFoundException e, Model model) {
        model.addAttribute("status", 404);
        model.addAttribute("message", "요청하신 페이지를 찾을 수 없습니다.");
        return "error/error";
    }

    /** 파일 업로드 용량 초과 */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public String handleMaxUploadSize(MaxUploadSizeExceededException e, Model model) {
        model.addAttribute("status", 413);
        model.addAttribute("message", "파일 크기가 너무 큽니다. 10MB 이하만 업로드 가능합니다.");
        return "error/error";
    }

    /**
     * Spring Security 인증 예외 - 반드시 재throw (Security 필터가 직접 처리해야 함)
     */
    @ExceptionHandler(AuthenticationException.class)
    public void handleAuthentication(AuthenticationException e) throws AuthenticationException {
        throw e;
    }

    /**
     * Spring Security 권한 예외 - 반드시 재throw (Security 필터가 직접 처리해야 함)
     */
    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDenied(AccessDeniedException e) throws AccessDeniedException {
        throw e;
    }

    /** 그 외 모든 예외 */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception e, Model model, HttpServletRequest request) {
        model.addAttribute("status", 500);
        model.addAttribute("message", "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        return "error/error";
    }
}
