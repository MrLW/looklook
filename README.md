#一个MVP的小例子

1、android:clipToPadding属性：见主布局
    <!--
       clipToPadding就是说控件的绘制区域是否在padding里面的，
       true的情况下如果你设置了padding那么绘制的区域就往里 缩，
       clipChildren是指子控件是否超过padding区域，这两个属性默认是true的，
       所以在设置了padding情况下，默认滚动是在 padding内部的，
       要达到上面的效果主要把这两个属性设置了false那么这样子控件就能画到padding的区域了。。
    -->

2、SimpleArrayMap
    HashMap在扩容时采取的做法是：将当前的数据结构所占空间*2，而这对安卓稀缺的资源来说，可是非常大的消耗。
    于是产生了ArrayMap,而SimpleArrayMap是一个纯算法的ArrayMap

3、@SerializedName：被修饰的属性指定属性序列化后的名称，不想被序列化的话使用transient 修饰。

4、WebView

```
WebSettings webSettings= webView.getSettings();
webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//WebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); 默认不使用缓存！
```
LOAD_CACHE_ELSE_NETWORK的意思是:如果有缓存则用缓存，否则从网络上读取


```
//自己适应屏幕
settings.setLoadWithOverviewMode(true);
// 显示放大缩小控制
 settings.setBuiltInZoomControls(true);
 // 设置缓存文件夹
 settings.setAppCachePath(getCacheDir().getAbsolutePath() + "/webViewCache");
```


#二、MVP设计模式

##1、层级责任
Model:负责数据的检索,持久化等操作
View: 负责UI的绘制和用户的交互
Presenter: 作为Model和View的中间协调部分,负责两者之间的业务逻辑处理

##2、MVP和MVC

MVC模式：Model可以直接update data 到View层。所以当某个View的功能很复杂的时候,View和Model的耦合度可能会很高(并且在android的开发中Activity通常会充当controller&view的角色,结果Activity就很臃肿).

MVP模式：没有上面这个问题,View会抽象出来一系列操作UI的接口(Model层也可以),Presenter拿到的都是其他两个层级的接口来做业务逻辑的处理.这样不仅可以使View和Model之间的耦合度降低,还可以更易得进行单元测试


#三、RxJava

##1、线程控制-Scheduler 

 - Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
 - Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
 - Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
 - Schedulers.computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
 - 另外， Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。
 

 有了上面几个Scheduler，就使用 subscribeOn() 和 observeOn() 两个方法来对线程进行控制

 ##2、变换

 ###a、 map()---是一对一的转化

 ```
Observable.just("images/logo.png") // 输入类型 String
    .map(new Func1<String, Bitmap>() {
        @Override
        public Bitmap call(String filePath) { // 参数类型 String
            return getBitmapFromPath(filePath); // 返回类型 Bitmap
        }
    })
    .subscribe(new Action1<Bitmap>() {
        @Override
        public void call(Bitmap bitmap) { // 参数类型 Bitmap
            showBitmap(bitmap);
        }
    });
 ```

 FuncX 和ActiveX十分类似，只是多了一个返回值

 ###b、flatMap()---是一对多的转化





