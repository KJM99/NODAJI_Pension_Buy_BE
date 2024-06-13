package com.example.pensionBuying.aspect.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedissonLock {

    String value(); // Lock 의 이름 (고유값)
    long waitTime() default 5000L; // Lock 획득을 시도하는 최대 시간 (ms)
    long leaseTime() default 2000L; // Lock 을 획득한 후, 점유하는 최대 시간 (ms)
}
