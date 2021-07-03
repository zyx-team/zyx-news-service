package com.zyx.user.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Test {
    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {

        Integer a = Integer.valueOf("100000");
        int b = 100000;
        System.out.println(a == b);

        Integer c = 1000;
        Integer d = 1000;
        System.out.println(c == d);

        Integer e = 100;
        Integer f = 100;
        System.out.println(e == f);

        String s2 = "a" + "b";
        String s3 = "a";
        String s4 = "b";
        String s5 = s3 + s4;
        System.out.println(s2 == s5);

        List<Integer> intList = new ArrayList<>();
        Collections.addAll(intList, 11,12,23,14,15);
        for(int i = 0; i < intList.size(); i++) {
            Integer value = intList.get(i);
            if(value > 10) {
                intList.remove(i);
//                i--;
            }
        }
        System.out.println(intList);


        Collection<String> list=new ArrayList<>();
        list.add("a");
        list.add("a");
        list.add("b");
        list.add("b");
        list.add("c");
        //System.out.println(((ArrayList<String>) list).indexOf("a"));
        System.out.println("a:"+listTest(list, "a"));
        System.out.println("b:"+listTest(list, "b"));
        System.out.println("c:"+listTest(list, "c"));
        System.out.println("xxx:"+listTest(list, "xxx"));


    }

    public static int listTest(Collection<String>list,String s) {
        int count=0;
        while (list.contains(s)){  // 判断当前集合中是否“s”。

            //public boolean remove(E e)把给定的对象在当前集合中删除。
            list.remove(s);  // 条件成立进行循环，把含有“s”的元素删除
            count++;
        }
        return count;
    }


}
