package com.github.xuqplus2.authserver.vo.resp;

import com.github.xuqplus2.authserver.util.HttpStatusUtil;
import com.github.xuqplus2.authserver.vo.VO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
public class BasicResp extends VO {

    private static final BasicResp OK_RESP = new BasicResp(HttpStatus.OK);
    private static final ResponseEntity OK = new ResponseEntity(OK_RESP, HttpStatus.OK);

    /**
     * 继承<code>HttpStatus</code>状态码
     * --返回200时为正确执行流程
     * --返回 != 200时, 为异常或错误流程, 这时<code>message</code>就很重要了, 用于前端的错误提示, i18n由前端翻译
     */
    private Integer code;
    /**
     * --正常执行流程时不重要, 一般会返回 "OK"
     * --出现异常或错误时很重要, 要让前端一下子明白错误出在哪里
     * ----同一种错误原因不要返回不同的<code>message</code>, 这样增加了i18n工作量
     */
    private String message;
    /**
     * --正常执行时返回数据, 一般会装入一个接口文档定义的对象
     * --异常或错识破时可以返回更详细的信息, 如果除了<code>message</code>以处没有更详细的信息, 则返回null, 一般会返回null
     */
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

    public static final ResponseEntity ok(Object data) {
        return ResponseEntity.ok(new BasicResp(HttpStatus.OK, HttpStatus.OK.name(), data));
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

