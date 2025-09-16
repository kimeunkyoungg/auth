package com.rest1.global.exception;

//우리의 비즈니스 안에서만 일어나는 예외를 처리하기 위해 따로 만들어준다.
//RuntimeException은 원래 전체적으로 일어나는 예외처리였지만 ServiceException을 만들어서
//범위를 좁혀준다

//우리 서비스 안에서의 대답 양식은 항상 보고서 양식을 따라야한다
public class ServiceException extends RuntimeException{

    private String resultCode;
    private String msg;

    public ServiceException(String resultCode, String msg){
        super("%s : %s".formatted(resultCode, msg));

        this.resultCode = resultCode;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public String getResultCode() {
        return resultCode;
    }
}
