package com.eicky;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Eicky
 * @Description:
 * @date: 2017/3/21 14:44
 * @version: V1.0
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBase {
    Class<?> type() default View.OnClickListener.class;
    String setter() default "";
    String method() default "";
}
