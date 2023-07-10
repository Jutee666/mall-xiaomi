package com.imooc.mall2.exception;

import com.imooc.mall2.enums.ResponserEnem;
import com.imooc.mall2.vo.ResponseVo;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;

import static com.imooc.mall2.enums.ResponserEnem.ERROR;

/**
 * ClassName: RuntimeExceptionHandler
 * Package: com.imooc.mall2.exception
 *
 * @Author 马学兴
 * @Create 2023/6/20 13:37
 * @Version 1.0
 * Description:
 */
@ControllerAdvice
public class RuntimeExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    //@ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseVo handler(RuntimeException e){
        return  ResponseVo.error(ERROR,e.getMessage());
    }
    @ExceptionHandler(UserLoginException.class)
    @ResponseBody
    public ResponseVo userLoginHandle(){
        return ResponseVo.error(ResponserEnem.NEED_LOGIN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVo notValidExceptionHandle(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        Objects.requireNonNull(bindingResult.getFieldError());
        return ResponseVo.error(ResponserEnem.PARAM_ERROR,
             bindingResult.getFieldError().getField()+""+bindingResult.getFieldError().getDefaultMessage());

    }
}
