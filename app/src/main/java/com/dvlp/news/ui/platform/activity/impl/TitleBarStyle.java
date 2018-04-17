package com.dvlp.news.ui.platform.activity.impl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TitleBarStyle {
    boolean show() default true;
    /**
     * Style枚举
     */
    enum Style{
        WHITE, BLACK

    }

    Style style() default Style.BLACK;
}
