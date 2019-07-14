package com.github.xuqplus2.authserver.vo.resp;

import com.github.xuqplus2.authserver.util.HttpStatusUtil;
import com.github.xuqplus2.authserver.vo.VO;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class BasicResp extends VO {

    private static final BasicResp OK_RESP = new BasicResp(HttpStatus.OK);
    private static final ResponseEntity OK = new ResponseEntity(OK_RESP, HttpStatus.OK);

    private Integer code;
    private String message;
    private Object data;

    private BasicResp() {
    }

    private BasicResp(HttpStatus status) {
        this(status, status.name(), null);
    }

    public BasicResp(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BasicResp(HttpStatus status, String message, Object data) {
        this(status.value(), message, data);
    }

    public static final ResponseEntity ok() {
        return OK;
    }

    /**
     * 直接用<code>ResponseEntity.ok(data)</code>就好了
     */
    @Deprecated
    public static final ResponseEntity ok(Object data) {
        return ResponseEntity.ok(data);
    }

    public static final ResponseEntity result(BasicResp resp) {
        HttpStatus status = HttpStatusUtil.valueOf(resp.getCode());
        return new ResponseEntity(resp, status);
    }

    public static final ResponseEntity err(HttpStatus status, String message, Object data) {
        return new ResponseEntity(new BasicResp(status.value(), message, data), status);
    }

    public static final ResponseEntity err(Integer code, String message, Object data) {
        return new ResponseEntity(new BasicResp(code, message, data), HttpStatus.valueOf(code));
    }
}

