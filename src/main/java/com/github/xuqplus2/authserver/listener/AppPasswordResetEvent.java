package com.github.xuqplus2.authserver.listener;

import org.springframework.context.ApplicationEvent;

public class AppPasswordResetEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public AppPasswordResetEvent(Object source) {
        super(source);
    }
}