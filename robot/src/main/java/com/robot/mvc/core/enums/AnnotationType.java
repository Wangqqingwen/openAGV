package com.robot.mvc.core.enums;

import com.robot.mvc.core.annnotations.*;
import com.robot.mvc.db.annotation.Entity;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.lang.annotation.Annotation;

public enum AnnotationType {
    CONTROLLER_ANNOTATION(Controller.class, true, "所有Controller类的注解，必须在类添加该注解否则框架忽略扫描"),
    SERVICE_ANNOTATION(Service.class, true, "所有Service类的注解，必须在类添加该注解否则框架忽略扫描"),
    ACTION_ANNOTATION(Action.class, true, "工站动作指令，所有Action类的注解，必须在类添加该注解否则框架忽略扫描"),
    ENTITY_ANNOTATION(Entity.class, false, "所有Entity类的注解，必须在类添加该注解否则框架忽略扫描"),
    JOB_ANNOTATION(Job.class, false, "所有Job类的注解，必须在类添加该注解否则框架忽略扫描"),
    LISTENER_ANNOTATION(Listener.class, true, "所有Listener类的注解，必须在类添加该注解否则框架忽略扫描"),
    WEBSOCKET_ANNOTATION(WebSocket.class, true, "所有Websocket类的注解，必须在类添加该注解否则框架忽略扫描"),


    ;

    Class<? extends Annotation> clazz;
    String name;
    // 是否需要实例化， true为需要
    boolean instance;
    String desc;

    private AnnotationType(Class<? extends Annotation> clazz, boolean instance, String desc) {
        this.clazz = clazz;
        this.instance = instance;
        this.desc = desc;
    }

    public Class<? extends Annotation> getClazz() {
        return clazz;
    }

    public String getName() {
        return clazz.getName();
    }

    /**
     * 是否需要实例化， true为需要
     *
     * @return
     */
    public boolean getInstance() {
        return instance;
    }

    public String desc() {
        return desc;
    }
}
