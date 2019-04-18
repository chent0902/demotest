/**
 *  Copyright (c) 2012-2014 http://www.eryansky.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.family.demotest.web.util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 生成随机数
 * 
 * @author : wujf
 */
public class RandomUtils
{

    private RandomUtils()
    {
    }

    /**
     * 生成指定范围的随机数
     * 
     * @param minValue
     *            最小值
     * @param maxValue
     *            最大值
     * @return
     */
    public static int getRandom(int minValue, int maxValue)
    {

        Random random = new Random();
        int num = random.nextInt(maxValue)%(maxValue-minValue+1)+minValue;

        return num;
    }

    /**
     * 创建值小于maxValue的size个值数组
     * 
     * @param size
     * @param maxValue
     * @return
     */
    public static Integer[] createIntArray(int size, int maxValue)
    {

        Integer[] values = new Integer[size];
        Set<Integer> hashSet = new HashSet<Integer>();

        // 生成随机数字并存入HashSet
        while(hashSet.size()<values.length)
        {
            hashSet.add(randomNum(maxValue));
        }

        values = hashSet.toArray(values);

        return values;
    }

    /**
     * 生成小于maxvalue随机值
     * 
     * @param maxValue
     * @return
     */
    public static int randomNum(int maxValue)
    {
        Random random = new Random();// 定义随机类
        int result = random.nextInt(maxValue);// 返回[0,maxValue)集合中的整数，注意不包括maxValue值
        // +1后，[0,10)集合变为[1,11)集合，满足要求
        return result;
    }

    public static void main(String[] args)
    {
        System.out.println(getRandom(10, 20));
    }
}
