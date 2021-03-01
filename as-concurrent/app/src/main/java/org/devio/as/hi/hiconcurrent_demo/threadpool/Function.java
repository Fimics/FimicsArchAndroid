package org.devio.as.hi.hiconcurrent_demo.threadpool;

public interface Function<T, R> {
    R apply(T value);
}
