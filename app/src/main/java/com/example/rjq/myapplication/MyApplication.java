package com.example.rjq.myapplication;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.litepal.LitePal;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyApplication extends Application {

    private static Context context;
    public static Application app;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        app = this;
        Log.d("Application create", context.toString());
        LitePal.initialize(context);

        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.add(new Button(this));

//        ArrayList<? super TextView> list = new ArrayList<Button>();  //报错
        ArrayList<? super TextView> list1 = new ArrayList<View>();
        list1.add(new Button(this));    //这里可以添加成功是因为 Button 是 TextView的子类，看上面的textViews
        //因为下界规定了元素的最小粒度的下限，实际上是放松了容器元素的类型控制。既然元素是TextView的基类，那往里存粒度比TextView小的(包括TextView)都可以(多态)。
        //但往外读取元素就费劲了，只有所有类的基类Object对象才能装下。但这样的话，元素的类型信息就全部丢失。
//        list1.add(new View(this));  //报错
        Object button = list1.get(0);

        ArrayList<? extends TextView> list2 = new ArrayList<>();
//        list2.add(new Button(this));
//        TextView button2 = list2.get(0);

//        Plate<Fruit> p = new Plate<Apple>();
        Plate<? extends Fruit> p2 = new Plate<Apple>();
        p2.get();
//        p2.set(new Apple());
//        p2.set(new Fruit());
//        Plate<? super Fruit> p3 = new Plate<Apple>();
        Plate<? super Fruit> p4 = new Plate<Object>();
    }

    public static Context getContext() {
        return context;
    }
}

class Fruit {
}

class Apple extends Fruit {
}

class Plate<T> {
    private T item;

    public Plate() { }
    public Plate(T t) {
        item = t;
    }

    public void set(T t) {
        item = t;
    }

    public T get() {
        return item;
    }
}
