
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

实现原理：

使用两个数组，一个int数组，存放int类型key ，一个是Object的数组，存放value。

重要的几个API
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

put的实现
```
    public void put(int key, E value) {
        //二分查找是否存在改key
        int i = ContainerHelpers.binarySearch(mKeys, mSize, key);

        if (i >= 0) {
            mValues[i] = value;
        } else {
            i = ~i;

            if (i < mSize && mValues[i] == DELETED) {
                mKeys[i] = key;
                mValues[i] = value;
                return;
            }

            if (mGarbage && mSize >= mKeys.length) {
                gc();

                // Search again because indices may have changed.
                i = ~ContainerHelpers.binarySearch(mKeys, mSize, key);
            }

            mKeys = GrowingArrayUtils.insert(mKeys, mSize, i, key);
            mValues = GrowingArrayUtils.insert(mValues, mSize, i, value);
            mSize++;
        }
    }
    
    //二分查找
    static int binarySearch(int[] array, int size, int value) {
        int lo = 0;
        int hi = size - 1;

        while (lo <= hi) {
            final int mid = (lo + hi) >>> 1;
            final int midVal = array[mid];

            if (midVal < value) {
                lo = mid + 1;
            } else if (midVal > value) {
                hi = mid - 1;
            } else {
                return mid;  // value found
            }
        }
        return ~lo;  // value not present
    }
    
```

