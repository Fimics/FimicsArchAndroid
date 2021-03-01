package org.devio.as.hi.hiconcurrent_demo.threadpool;

/**
 * 模拟rxjava 响应式编程
 *
 * @param <T>
 */
public class HiTaskFlow<T> {
    HiTask source;

    private HiTaskFlow(HiTask source) {
        this.source = source;
    }

    // create 方法在创建时持有了被观察者---HiTask
    // 第一个 T 是静态方法声明的 T     参数的 T 是静态方法声明的泛型
    // 上下线在作为参数时 表示的就是类型的界限
    // 当泛型作为参数时会有可读和可写的区别  super 表示参数是可以被写的  extends 只能是可读
    public static <T> HiTaskFlow<T> create(HiTask<? extends T> source) {
        return new HiTaskFlow<T>(source); // 返回的是静态方法返回的 T
    }

    public <R> HiTaskFlow<R> map(Function<? super T, R> function) {
        HiTaskWrapper taskWrapper = new HiTaskWrapper(source, function);
        // 这里会把 source 替换成 HiTaskWrapper
        // 在 execute 时实际调用的 observable map 所持有的 source 的 execute 方案
        return new HiTaskFlow<>(taskWrapper);
    }

    public void execute(Observer<? extends T> observer) {
        source.subscribe(observer);
    }


    class HiTaskWrapper<T, R> implements HiTask<R> {
        private HiTask observable;
        private Function<? super T, ? extends R> function;

        public HiTaskWrapper(HiTask source, Function<? super T, ? extends R> function) {
            this.observable = source;
            this.function = function;
        }

        @Override
        public void subscribe(Observer<? super R> observer) {
            ObserverWrapper mapObserver = new ObserverWrapper(observer, function);
            observable.subscribe(mapObserver);
        }

        class ObserverWrapper<T> implements Observer<T> {
            Observer<? super R> observable;
            Function<? super T, ? extends R> function;

            /**
             * @param observable 通过 execute 传递进来的观察者
             * @param function   function 是负责转换的函数
             */
            public ObserverWrapper(Observer<? super R> observable,
                                   Function<? super T, ? extends R> function) {
                this.observable = observable;
                this.function = function;
            }

            @Override
            public void onNext(T value) {
                // 将转换后的值交给 onNext
                R next = function.apply(value);
                observable.onNext(next);
            }

            @Override
            public void onComplete() {
                observable.onComplete();
            }

            @Override
            public void onError(Throwable throwable) {
                observable.onError(throwable);
            }
        }
    }

    public interface HiTask<T> {
        void subscribe(Observer<? super T> observableEmitter);
    }

    public interface Observer<T> {
        void onNext(T value);

        void onComplete();

        void onError(Throwable throwable);
    }
}
