package com.github.xuqplus2.authserver.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Slf4j
@Aspect
@Component
public class BindingResultProcessor {

    /**
     * 切入点A
     * <p>
     * 表达式格式说明
     * <p>
     * 修饰符 返回值 类全名 方法名 参数名 异常名
     */
    /*@Pointcut("execution(public * com.github.xuqplus2.authserver.controller.captcha.CaptchaController.*(..))")
    public void pointA() {
    }*/

    /**
     * 从切点中拿到当前栈
     *
     * @param point
     */
    /*@Before("pointA()")
    public void beforeA(JoinPoint point) {
        log.info("{}", point);
    }*/

    /**
     * 因为spring aop的实现方式是代理,
     * 切点所切到的类里如有<code>a(),b()</code>两方法同一个<code>@Pointcut</code>切到
     * 且<code>a(){b();...;}</code>两个方法有调用关系时, 切点方法只会被执行一次
     * <p>
     * 这种实现有其优势, 同样会引发一些问题, 参考: (https://blog.mythsman.com/post/5d301cf2976abc05b34546be/)
     */
    @Pointcut("execution(public * com.github.xuqplus2.authserver.controller..*Controller.*(..,org.springframework.validation.BindingResult,..))")
    public void pointB() {
    }

    @Before("pointB()")
    public void beforeB(JoinPoint point) {
        Object[] args = point.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult bindingResult = (BindingResult) arg;
                if (null != bindingResult && bindingResult.hasErrors()) {
                    FieldError fieldError = bindingResult.getFieldError();
                    log.info("fieldError, objectName={}, field={}, message={}",
                            fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
                    throw new RuntimeException(fieldError.getDefaultMessage());
                }
                return;
            }
        }
    }
}
