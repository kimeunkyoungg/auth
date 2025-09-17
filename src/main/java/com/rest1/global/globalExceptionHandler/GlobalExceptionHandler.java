package com.rest1.global.globalExceptionHandler;

import com.rest1.global.exception.ServiceException;
import com.rest1.global.rsData.RsData;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice //Controller 실행을 지켜보다 해당 에러가 발생하면 로직 실행하도록
public class GlobalExceptionHandler {


    //예외 처리 함수
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseBody
    public RsData<Void> handleException(){
        return new RsData<Void>(
                "404-1",
                "존재하지 않는 데이터입니다"
        );

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public RsData<Void> handleException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getAllErrors()
                .stream()
                .filter(error -> error instanceof FieldError)
                .map(error -> (FieldError) error)
                .map(error -> error.getField() + "-" + error.getCode() + "-" + error.getDefaultMessage())
                .sorted(Comparator.comparing(String::toString))
                .collect(Collectors.joining("\n"));

        return new RsData<Void>(
                "400-1",
                message
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public RsData<Void> handleException(HttpMessageNotReadableException e) {
        return new RsData<Void>(
                "400-2",
                "잘못된 형식의 요청 데이터입니다."
        );
    }

    //비번 틀렸을때 -> RuntimeException 상속받은 ServiceException 사용
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public RsData<Void> handleException(ServiceException e) {
        return new RsData<Void>(
                e.getResultCode(),
                e.getMsg()
        );
    }

//    @ExceptionHandler(HandlerMethodValidationException.class)
//    @ResponseBody
//    public RsData<Void> handleException(HandlerMethodValidationException e) {
//
//        e.printStackTrace(); //로그에 찍히도록함
//        return new RsData<Void>(
//                "400-3",
//                "요청 파라미터가 잘못되었습니다"
//        );
//    }
}

//프로젝트 전반적으로 생기는 에러에 대해 처리를 할 수 있는 클래스
//**Tdd의 다건조회, 없는 아이디 실행 시**

//ApiV1Postcontroller에서 다건 조회할때 없는 id를 요청하면 에러 터짐(NoSuchElementExcetion)
//해당 클래스에 NoSuchElementExcetion 발생 시 처리 로직이 구현되어있으므로 실행(@ExceptionHandler)
//그냥 반환하는 것이 아니라 RsData 보고서 양식대로 반환하도록 함
//ResponseBody가 있으므로 프록시를 통해 실행(ResponseAspect.class)

//보고서 양식(RsData)를 만들어놨으면 모든 값 응답 시 보고서를 사용할 수 있도록 함
