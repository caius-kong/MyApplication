package com.zbiti.myapplication;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Rxjava: RxJava 是一个响应式编程框架，采用观察者设计模式。
 *
 * 解释：有点类似于android中的监听器，button就是被观察者，OnClickListener就是观察者
 *
 * 使用过程：
 * 1) 创建 Observer： 观察者定义处理程序；
 * 2) 创建 Observable： 被观察者订阅回调观察者定义的方法（事件队列）
 * 3) Subscribe (订阅)： 被观察者绑定观察者（或者说绑定对象、指定处理程序）
 *
 * 进阶1：使用Scheduler（线程控制）
 * 1) .subscribeOn(Scheduler.io())： 指定observable所处的线程 （多次使用时，只有第一个 subscribeOn() 起作用）
 * 2) .observeOn(AndroidSchedulers.mainThread())： 指定observer所处的线程 （多次使用，就会多次切换进程）
 *
 * 进阶2：使用变换
 * 1) .map(): 事件的对象变换，1:1
 * 2) .flatMap(): 事件的对象变化，1:N
 *
 * 变换的原理：对lift()进行了包装使用，而lift可以认为是一种代理（产生代理Observable与Subscriber）。【了解】
 *
 * 进阶3：doOnXXX()方法
 * 1) doOnSubscribe(): 在subscibe()调用后事件执行前发生（由后面跟着的subscribeOn()规定线程）
 * 2) doOnNext(): 在OnNext()调用后事件执行前发生
 *
 * 典型应用场景
 * 1) 与 Retrofit 的结合，使Retrofit专注于网络请求本身（目前Retrofit+okhttp是一个完美的网络解决方案）
 */
public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);

        // 初识Rxjava
        helloWorld1();
        helloWorld2();
        helloWorld3();

        // 了解正真的Rxjava - 异步
        loadImage();

        // 接触Rxjava真正强大的地方
        mapDemo();
        flatMapDemo();
    }

    /**
     * 典型的Rxjava
     *
     * Subscriber是Observer的实现类，其实在 RxJava 的 subscribe 过程中，Observer 也总是会先被转换成一个 Subscriber 再使用。
     * 所以如果你只想使用基本功能，选择 Observer 和 Subscriber 是完全一样的
     *
     */
    public void helloWorld1() {
        // 被观察者，定义主题，变化者，事件队列
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hello");
                subscriber.onNext("world");
                subscriber.onCompleted();
            }
        });
        // 创建被观察者时，RxJava 还提供了一些方法用来快捷创建事件队列
//        String[] strs = new String[]{"hello","world"};
//        Observable<String> observable = Observable.from(strs);
//        Observable<String> observable = Observable.just("hello", "world");

        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("--->onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("--->onError");
            }

            @Override
            public void onNext(String s) {
                System.out.println("--->" + s);
            }
        });
    }

    /**
     * 使用Action1、Action0自动创建Subscriber
     */
    public void helloWorld2() {
        String[] strs = new String[]{"hello2", "world2"};
        Observable<String> observable = Observable.from(strs);

        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("--->" + s);
            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                System.out.println("--->onError");
            }
        };
        Action0 onCompletedAction = new Action0() {
            @Override
            public void call() {
                System.out.println("--->onCompleted");
            }
        };
        // .subscribe(Subscriber) // 指定定义的观察者
        // .subscribe(Action1 next, [Action1 error], [Action0 complete]) // 根据定义，自动创建出观察者
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
    }

    /**
     * 自动创建Subscriber，且仅定义onNext的形式
     */
    public void helloWorld3() {
        String[] strs = new String[]{"hello3", "world3"};
        Observable.from(strs)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("--->" + s);
                    }
                });
    }

    /**
     * 使用 Scheduler ，进行后台取数据，前台显示
     * 优势：就算加载图片需要10秒，前台界面也不会卡顿。
     */
    public void loadImage() {
        Observable
                .create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        subscriber.onNext(R.mipmap.ic_launcher);
                    }
                })
                // 由后面跟着的 subscribeOn()规定线程
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        System.out.println("-doOnSubscribe-->在io线程执行");
                    }
                })
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("--->onNext执行前之前，可以用来验证取出的数据等");
                        if (integer != null) {
                            System.out.println("--->图片资源id正确！");
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onStart() {
                        System.out.println("-onStart-->在subscribe执行的线程执行");
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("-onNext-->显示图片");
                        imageView.setImageResource(integer);
                    }
                });
    }

    /**
     * 所谓变换，就是将事件序列中的 对象 或 整个序列 进行加工处理，转换成不同的 对象 或 事件序列
     */
    public void mapDemo() {
        // map: Student --> String
        Student[] students = initStudents();
        Observable.from(students)
                .map(new Func1<Student, String>() {
                    @Override
                    public String call(Student student) {
                        return student.getName();
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String name) {
                        System.out.println("--->" + name);
                    }
                });
    }

    /**
     * 把事件拆成了两级: 通过一组新创建的 Observable 将初始的对象『铺平』之后通过统一路径分发了下去。
     * 而这个『铺平』就是 flatMap() 所谓的 flat。
     */
    public void flatMapDemo() {
        // flatMap: Student --> Observable<Course>【N个】 --> Course【N个】（每个Observable依次取出Course，平铺后分发下去）
        Student[] students = initStudents();
        Observable.from(students)
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        return Observable.from(student.getCourses());
                    }
                })
                .subscribe(new Action1<Course>() {
                    @Override
                    public void call(Course course) {
                        System.out.println("--->" + course.getName());
                    }
                });
    }

    public Student[] initStudents() {
        Course course1 = new Course("数学");
        Course course2 = new Course("语文");
        Course course3 = new Course("英语");
        Set<Course> set = new HashSet<Course>();
        set.add(course1);
        set.add(course2);
        set.add(course3);
        Student student1 = new Student("kong", set);
        Student student2 = new Student("yun", set);
        Student student3 = new Student("hui", set);
        return new Student[]{student1, student2, student3};
    }
}
