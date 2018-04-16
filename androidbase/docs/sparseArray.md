
##SparseArray

SparseArray是Android SDK提供的一个专门存储<int,Object>类型数据的一种数据结构。

SparseArrays将整数映射到对象。与普通的对象数组不同，索引中可能存在空白。它的目的是比使用HashMap将整数映射到对象更高效，这是因为它避免了自动装箱键，并且其数据结构不依赖每个映射的额外入口对象。

请注意，此容器将其映射保存在数组数据结构中，并使用二分法搜索来查找键。该实现不适用于可能包含大量数据的数据结构。它通常比传统的HashMap慢，因为查找需要二分搜索并添加和删除需要插入和删除数组中的条目。对于大量数据，性能差异不显着，小于50％。


为了提高性能，容器在删除键时包含一个优化：不是立即压缩其数组，而是将删除的条目标记为已删除。然后可以将该条目重新用于相同的密钥，或稍后在所有移除的条目的单个垃圾收集步骤中进行压缩。这个垃圾收集需要随时生成数组或者检索地图大小或条目值。

使用keyAt(int)和valueAt(int)可以遍历此容器中的元素。使用keyAt(int)进行迭代将按照升序顺序返回索引键，valueAt(int)按照升序顺序返回键的值。

使用：
```
    for (int i = 0; i < 20; i++) {
        Person p = new Person();
        p.name = "name ="+i;
        sparseArray.append(i , p);
    }


    for (int i = 0; i < sparseArray.size(); i++) {
        int key = sparseArray.keyAt(i);
        Person p = sparseArray.valueAt(key);

        Log.e("TAG" , "key = "+key + " p =" +p.name);
    }
    
```
打印：
```
04-13 19:14:52.323 2656-2656/ E/TAG: key = 0 p =name =0
04-13 19:14:52.323 2656-2656/ E/TAG: key = 1 p =name =1
04-13 19:14:52.323 2656-2656/ E/TAG: key = 2 p =name =2
04-13 19:14:52.323 2656-2656/ E/TAG: key = 3 p =name =3
04-13 19:14:52.323 2656-2656/ E/TAG: key = 4 p =name =4
04-13 19:14:52.323 2656-2656/ E/TAG: key = 5 p =name =5
04-13 19:14:52.323 2656-2656/ E/TAG: key = 6 p =name =6
04-13 19:14:52.323 2656-2656/ E/TAG: key = 7 p =name =7
04-13 19:14:52.323 2656-2656/ E/TAG: key = 8 p =name =8
04-13 19:14:52.323 2656-2656/ E/TAG: key = 9 p =name =9
04-13 19:14:52.323 2656-2656/ E/TAG: key = 10 p =name =10
04-13 19:14:52.323 2656-2656/ E/TAG: key = 11 p =name =11
04-13 19:14:52.323 2656-2656/ E/TAG: key = 12 p =name =12
04-13 19:14:52.323 2656-2656/ E/TAG: key = 13 p =name =13
04-13 19:14:52.323 2656-2656/ E/TAG: key = 14 p =name =14
04-13 19:14:52.323 2656-2656/ E/TAG: key = 15 p =name =15
04-13 19:14:52.323 2656-2656/ E/TAG: key = 16 p =name =16
04-13 19:14:52.323 2656-2656/ E/TAG: key = 17 p =name =17
04-13 19:14:52.323 2656-2656/ E/TAG: key = 18 p =name =18
04-13 19:14:52.323 2656-2656/ E/TAG: key = 19 p =name =19
```

实现原理：使用两个数组，一个int数组，存放int类型key ，一个是Object的数组，存放value。

重要的几个API使用
```
    SparseArray<Person> sparseArray = new SparseArray<>();
    //增
    
    Person person = new Person();
    person.name = "append";
    sparseArray.append(100 , person);
    person.name = "put";
    sparseArray.put(200 , person); 


    //查
    int key = sparseArray.keyAt(0);
    Log.e("TAG" , "key ="+key);
    Person value = sparseArray.get(key);
    Log.e("TAG" , "value ="+value.name);


    //改
    int index = sparseArray.indexOfKey(100);
    person.name = "修改的1";
    sparseArray.setValueAt(index ,person );
    person.name = "修改的2";
    sparseArray.put(200 , person);
    Log.e("TAG" , sparseArray.get(100).name);
    Log.e("TAG" , sparseArray.get(200).name);
    person.name = "修改的3";
    sparseArray.append(200 , person);
    Log.e("TAG" , sparseArray.get(200).name);
    
    
    //删
    sparseArray.delete(100);
    sparseArray.remove(200);
    sparseArray.delete(1000);//没有不会报错

    Log.e("TAG" , "size ==" + sparseArray.size());
 
```
##### put get delete的具体实现

######put的实现会用到几个重要方法 

1.ContainerHelpers.binarySearch()

2.gc()

3.GrowingArrayUtils.insert()

```
    public void put(int key, E value) {
        //二分查找是否存在该key，没有值就返回一负数，原因在binarySearch返回值处有分析
        int i = ContainerHelpers.binarySearch(mKeys, mSize, key);
        //如果存在在更新该value，不存在则插入
        if (i >= 0) {
            mValues[i] = value;
        } else {
            //取非得到key要插入的位置，原因在binarySearch返回值处有分析
            i = ~i;
            //如果数组够大且当前索引所在的值为DELETED，直接插进去
            if (i < mSize && mValues[i] == DELETED) {
                mKeys[i] = key;
                mValues[i] = value;
                return;
            }
            //mGarbage只有在删除后才会为true ，
            //这里为什么这样处理呢？因为元素在数组中是一个接着一个的，插入之前如果有删除操作，就会出现中断，
            //gc方法并不是回收方法，它把删除后不连续的元素变成连续的元素。
            if (mGarbage && mSize >= mKeys.length) {
                gc();

                // gc方法可能会引起索引变化，这里重新执行一下搜索
                i = ~ContainerHelpers.binarySearch(mKeys, mSize, key);
            }
            //插入新值到key数组和value数组。
            mKeys = GrowingArrayUtils.insert(mKeys, mSize, i, key);
            mValues = GrowingArrayUtils.insert(mValues, mSize, i, value);
            mSize++;
        }
    }
 ```
 binarySearch方法：
 ```
    //二分查找 找到value在数组中的位置，应为数组中的值是按大小有序排列的 所以可以直接用二分查
    static int binarySearch(int[] array, int size, int value) {
        //低位索引
        int lo = 0;
        //高位索引
        int hi = size - 1;
        
        while (lo <= hi) {
            //中间索引
            final int mid = (lo + hi) >>> 1;
            //中间的值
            final int midVal = array[mid];
            
            if (midVal < value) {
                //中间的值小于目标值，则认为可能在右边，更新低位索引值为中间值前一个
                lo = mid + 1;
            } else if (midVal > value) {
                //中间的值大于于目标值，则认为可能在左边，更新高位索引值为中间值后一个
                hi = mid - 1;
            } else {
                //找到value 返回索引值
                return mid;  // value found
            }
        }
        /**
         *没找到值为什么要取非返回呢。
         *原因是为了实现没有找到，返回的索引值为负数。如果没有找到此时的lo肯定是下一个插入value所在的位置。
         *加入数组中共有3个元素，没有找到目标value此时的lo肯定是 3，取非肯定是负数了。
         **/
        return ~lo;  // value not present
    }
 ```
 insert方法：
 ```
    //将元素插入到指定的数组当中，若指定的数组长度不够，则会创建一个新的数组
    public static <T> T[] insert(T[] array, int currentSize, int index, T element) {
        assert currentSize <= array.length;

        if (currentSize + 1 <= array.length) {
            System.arraycopy(array, index, array, index + 1, currentSize - index);
            array[index] = element;
            return array;
        }

        @SuppressWarnings("unchecked")
        T[] newArray = ArrayUtils.newUnpaddedArray((Class<T>)array.getClass().getComponentType(),
                growSize(currentSize));
        System.arraycopy(array, 0, newArray, 0, index);
        newArray[index] = element;
        System.arraycopy(array, index, newArray, index + 1, array.length - index);
        return newArray;
    }
    
```
gc方法：
```
    //删除后才可能会调用 ，对数组元素重新排序
    private void gc() {
        // Log.e("SparseArray", "gc start with " + mSize);

        int n = mSize;
        int o = 0;
        int[] keys = mKeys;
        Object[] values = mValues;

        for (int i = 0; i < n; i++) {
            Object val = values[i];
            //
            if (val != DELETED) {
                if (i != o) {
                    keys[o] = keys[i];
                    values[o] = val;
                    values[i] = null;
                }

                o++;
            }
        }

        mGarbage = false;
        mSize = o;

        // Log.e("SparseArray", "gc end with " + mSize);
    }
```

###### get 的实现
查询的时候先要得到对应的key在数组中索引，然后根据该索引返回value值。
```
    public E get(int key, E valueIfKeyNotFound) {
        int i = ContainerHelpers.binarySearch(mKeys, mSize, key);

        if (i < 0 || mValues[i] == DELETED) {
            return valueIfKeyNotFound;
        } else {
            return (E) mValues[i];
        }
    }
```

###### 删除实现
```
    public void delete(int key) {
        int i = ContainerHelpers.binarySearch(mKeys, mSize, key);

        if (i >= 0) {
            if (mValues[i] != DELETED) {
                mValues[i] = DELETED;
                mGarbage = true;
            }
        }
    }
```

####### SparseArray与HashMap性能PK




