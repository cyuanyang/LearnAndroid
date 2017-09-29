#### SVG的动画

###### SVG格式的图片

[VectorDrawable](https://developer.android.com/reference/android/graphics/drawable/VectorDrawable.html)用来加载SVG图片的
对应的XML标签为**vector**，文档中有个标签的详细说明

PATH命令:以下这些命令用于路径数据：

```
M = moveto(M X,Y)：将画笔移动到指定的坐标位置，但未发生绘制

L = lineto(L X,Y)：画直线到指定的坐标位置

H = horizontal lineto(H X)：画水平线到指定的X轴坐标

V = vertical lineto(V Y)：画垂直线到指定的Y轴坐标

C = curveto(C X1,Y1,X2,Y2,ENDX,ENDY)：三次贝塞曲线

S = smooth curveto(S X2,Y2,ENDX,ENDY)：三次贝塞曲线

Q = quadratic Belzier curveto(Q X,Y,ENDX,ENDY)：二次贝塞曲线

T = smooth quadratic Belzier curveto(T ENDX,ENDY)：映射前面路径后的终点

A = elliptical Arc(A RX,RY,XROTATION,FLAG1,FLAG2,X,Y)：弧线

Z = closepath()：关闭路径
```

**注释：以上所有命令均允许小写字母。大写表示绝对定位，小写表示相对定位。**

##### SVG图片动画




##### 参考
[](https://medium.com/@shemag8/animated-vector-drawable-e4d7743d372c)