package com.github.xuqplus2.authserver.vo.resp;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RegisterResp<T> implements Serializable {

    protected Integer code;
    protected String message;
    protected T data;
}
