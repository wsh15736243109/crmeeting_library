package com.cr.pn.netWork.netty.base.annotation.server;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NettyServerConfig {

    int port() default 8020;

    String bindIp() default "0.0.0.0";

    String name() default "";

}
