package com.imooc.mall2.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imooc.mall2.enums.ResponserEnem;
import lombok.Data;
import org.springframework.validation.BindingResult;

import java.util.Objects;

/**
 * ClassName: ResponseVo
 * Package: com.imooc.mall2.vo
 *
 * @Author 马学兴
 * @Create 2023/6/20 10:56
 * @Version 1.0
 * Description:
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ResponseVo<T> {
    private Integer status;
    private String msg;
    private T data;

    private ResponseVo(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ResponseVo(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    public static <T>ResponseVo<T> successByMsg(String msg){
        return new ResponseVo<>(ResponserEnem.SUCCESS.getCode(),msg);
    }
    public static <T>ResponseVo<T> success(T data){
        return new ResponseVo<>(ResponserEnem.SUCCESS.getCode(),data);
    }
    public static <T>ResponseVo<T> success(){
        return new ResponseVo<>(ResponserEnem.SUCCESS.getCode(),ResponserEnem.SUCCESS.getDesc());
    }
    public static <T>ResponseVo<T> error(ResponserEnem responserEnem ){
        return new ResponseVo<>(responserEnem.getCode(),responserEnem.getDesc());
    }
    public static <T>ResponseVo<T> error(ResponserEnem responserEnem ,String msg){
        return new ResponseVo<>(responserEnem.getCode(),msg);
    }
    public static <T>ResponseVo<T> error(ResponserEnem responserEnem , BindingResult bindingResult){
        return new ResponseVo<>(responserEnem.getCode(),
                Objects.requireNonNull(bindingResult.getFieldError()).getField()+""+bindingResult.getFieldError().getDefaultMessage());
    }
}
