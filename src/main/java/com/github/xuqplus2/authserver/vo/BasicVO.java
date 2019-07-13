package com.github.xuqplus2.authserver.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder // todo, 这东西继承有问题啊..
@NoArgsConstructor
@AllArgsConstructor
public class BasicVO<T> implements Serializable {

    protected Integer code;
    protected String message;
    protected T data;
}
