package org.devio.as.hi.hiconcurrent_demo.coroutine;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlinx.coroutines.DelayKt;

/**
 * suspend fun request1(): String {
 * val request2 = request2();
 * return "result from request1 " + request2;
 * }
 * <p>
 * <p>
 * suspend fun request2(): String {
 * delay(2 * 1000)
 * Log.e(TAG, "request2 completed")
 * return "result from request2"
 * }
 */
public class CoroutineScene2_decompiled {

    private static final String TAG = "CoroutineScene2";

    public static final Object request1(Continuation preCallback) {

        ContinuationImpl request1Callback;
        if (!(preCallback instanceof ContinuationImpl) || (((ContinuationImpl) preCallback).label & Integer.MIN_VALUE) == 0) {
            request1Callback = new ContinuationImpl(preCallback) {

                @Override
                Object invokeSuspend(@NotNull Object resumeResult) {
                    this.result = resumeResult;
                    this.label |= Integer.MIN_VALUE;
                    Log.e(TAG, "request1 has resumed");
                    return request1(this);
                }
            };
        } else {
            request1Callback = (ContinuationImpl) preCallback;
        }

        switch (request1Callback.label) {
            case 0: {
                // Object delay = DelayKt.delay(2000, request1Callback);
                Object request2 = request2(request1Callback);
                if (request2 == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                    Log.e(TAG, "request1 has suspended");
                    return IntrinsicsKt.getCOROUTINE_SUSPENDED();
                }
            }
        }

        Log.e(TAG, "request1 completed");
        return "result from request1" + request1Callback.result;
    }

    public static final Object request2(Continuation preCallback) {

        ContinuationImpl request2Callback;
        if (!(preCallback instanceof ContinuationImpl) || (((ContinuationImpl) preCallback).label & Integer.MIN_VALUE) == 0) {
            request2Callback = new ContinuationImpl(preCallback) {

                @Override
                Object invokeSuspend(@NotNull Object resumeResult) {
                    this.result = resumeResult;
                    this.label |= Integer.MIN_VALUE;
                    Log.e(TAG, "request2 has resumed");
                    return request2(this);
                }
            };
        } else {
            request2Callback = (ContinuationImpl) preCallback;
        }

        switch (request2Callback.label) {
            case 0: {
                Object delay = DelayKt.delay(2000, request2Callback);
                if (delay == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                    Log.e(TAG, "request2 has suspended");
                    return IntrinsicsKt.getCOROUTINE_SUSPENDED();
                }
            }
        }

        Log.e(TAG, "request2 completed");
        return "result from request2";
    }


    static abstract class ContinuationImpl<T> implements Continuation<T> {
        private Continuation preCallback;
        int label;
        Object result;

        public ContinuationImpl(Continuation preCallback) {
            this.preCallback = preCallback;
        }

        @NotNull
        @Override
        public CoroutineContext getContext() {
            return preCallback.getContext();
        }

        @Override
        public void resumeWith(@NotNull Object resumeResult) {
            Object suspend = invokeSuspend(resumeResult);
            if (suspend == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                return;
            }
            preCallback.resumeWith(suspend);
        }

        abstract Object invokeSuspend(@NotNull Object resumeResult);
    }
}
