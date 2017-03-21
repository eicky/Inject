package com.eicky;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Eicky
 * @Description:
 * @date: 2017/3/21 15:01
 * @version: V1.0
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(type = View.OnClickListener.class, method = "onClick")
public @interface OnClick {
    int[] value();
}
