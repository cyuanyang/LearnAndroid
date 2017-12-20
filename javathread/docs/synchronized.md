
#### synchronized关键字

#### synchronized修饰的方法

synchronized方法会将当前的对象加锁，如果有多个synchronized修饰的方法，当正在运行其中一个synchronized的方法的时候，
其他的所有的都会被阻塞，等待其执行完后在执行其他的。

对于不同的实例则不会有任何影响 跟没有synchronized修饰一样

[例子SynchronizedTest.java]()

#### synchronized修饰的静态方法

这个跟上一个基本一样的 只不过synchronized修饰的静态方法会这用于所有的实例

[例子SynchronizedTest.java]()

#### synchronized代码块

