package com.github.xuqplus2.authserver.thread.local.a;

import com.github.xuqplus2.authserver.thread.local.b.BTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class ATest {

    @Test
    public void a() {
        log.info("当前线程名=>{}, id=>{}", Thread.currentThread().getName(), Thread.currentThread().getId());
        b();
        c();
        BTest.a();
        new BTest().b();
        new BTest.BRunnable().run(); // this is wrong

        new Thread(() -> b()).start(); // 新的线程
        new Thread(new BTest.BRunnable()).start(); // 新的线程
    }

    private static void b() {
        log.info("当前线程名=>{}, id=>{}", Thread.currentThread().getName(), Thread.currentThread().getId());
    }

    private void c() {
        log.info("当前线程名=>{}, id=>{}", Thread.currentThread().getName(), Thread.currentThread().getId());
    }

    @Test
    public void local() {
        ThreadLocal threadLocal = new ThreadLocal();
        Object o = threadLocal.get();
        log.info("{}", o);

        threadLocal.set("abc");
        Object o1 = threadLocal.get();
        log.info("{}", o1);

        threadLocal.set("123");
        Object o2 = threadLocal.get();
        log.info("{}", o2);

        ThreadLocal threadLocal1 = new ThreadLocal();
        Object o3 = threadLocal1.get();
        log.info("{}", o3);

        new BTest.BThread("hahaha").start();
        new BTest.BThread("hohoho").start();

        for (int i = 0; i < 3; i++) {
            new BTest.BThread("p=" + i).start();
        }

//        SecurityContextHolder.getContext();
    }
}
