package com.github.xuqplus2.authserver.util;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class RandomUtilTest {

    @Test
    public void string() {
        String string = RandomUtil.string(64);
        String string1 = RandomUtil.string(64);

        System.out.println(string);
        System.out.println(string1);

        Assert.assertEquals(64, string.length());
        Assert.assertEquals(64, string1.length());
    }

    @Test
    public void b() {
        String string = RandomUtil.string(0);
        String string1 = RandomUtil.string(1);
        String string2 = RandomUtil.string(2);

        System.out.println(string);
        System.out.println(string1);
        System.out.println(string2);

        Assert.assertEquals(0, string.length());
        Assert.assertEquals(1, string1.length());
        Assert.assertEquals(2, string2.length());
    }

    @Test
    public void c() { // 没有换行符
        String string = RandomUtil.secureString(222);

        System.out.println(string);

        Assert.assertEquals(222, string.length());
    }

    @Test
    public void d() { // 没有换行符
        String string = RandomUtil.numeric(22);

        System.out.println(string);

        Assert.assertEquals(22, string.length());
    }

    @Test
    public void e() {
        for (int i = 1, j = 0; j < 31; j++) {
            System.out.println(String.format("j=%s, i=%s", j, i));
            i <<= 1;
        }

        System.err.println(Integer.MAX_VALUE);
        System.err.println(Long.MAX_VALUE);
    }

    @Test
    public void f() {
        System.err.println(RandomUtil.numeric(0));
        System.err.println(RandomUtil.numeric(1));
        System.err.println(RandomUtil.numeric(2));
        System.err.println(RandomUtil.numeric(3));
        System.err.println(RandomUtil.numeric(4));
        System.err.println(RandomUtil.numeric(5));
        System.err.println(RandomUtil.numeric(6));
        System.err.println(RandomUtil.numeric(7));
        System.err.println(RandomUtil.numeric(8));
        System.err.println(RandomUtil.numeric(9));
        System.err.println(RandomUtil.numeric(10));
        System.err.println(RandomUtil.numeric(20));

        for (int i = 0; i < 50; i++) {
            String numeric = RandomUtil.numeric(i);
            System.out.println(String.format("i=%s, numeric=%s", i, numeric));
            Assert.assertEquals(i, numeric.length());
        }
    }

    @Test
    public void g() {
        System.err.println(-12123);
        System.err.println(12123);

//        000000012123
        System.err.println(String.format("%012d", 12123));
//        -00000012123
        System.err.println(String.format("%012d", -12123));
    }
}