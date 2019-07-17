package com.github.xuqplus2.authserver.thread.local.b;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BTest {

    public static void a() {
        log.info("当前线程名=>{}, id=>{}", Thread.currentThread().getName(), Thread.currentThread().getId());
    }

    public void b() {
        log.info("当前线程名=>{}, id=>{}", Thread.currentThread().getName(), Thread.currentThread().getId());
    }

    public static class BRunnable implements Runnable {

        @Override
        public void run() {
            log.info("当前线程名=>{}, id=>{}", Thread.currentThread().getName(), Thread.currentThread().getId());
        }
    }

    public static class BThread extends Thread {

        private Object bThreadProperty;

        public BThread(Object bThreadProperty) {
            super();
            this.bThreadProperty = bThreadProperty;
        }

        @Override
        public void run() {
            log.info("bThreadProperty=>{}, id=>{}", this.bThreadProperty);
        }
    }
}
