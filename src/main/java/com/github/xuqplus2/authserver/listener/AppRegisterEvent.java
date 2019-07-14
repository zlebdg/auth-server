package com.github.xuqplus2.authserver.listener;

import org.springframework.context.ApplicationEvent;

public class AppRegisterEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public AppRegisterEvent(Object source) {
        super(source);
    }
}
