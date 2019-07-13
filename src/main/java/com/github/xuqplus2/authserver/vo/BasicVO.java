package com.github.xuqplus2.authserver.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class BasicVO<T> implements Serializable {

    private Integer code;
    private String message;
    private T data;
}
