package com.kairlec.utils;

import com.kairlec.pojo.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PasswordCoderUtilsTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
        public void test() throws Exception{
        User user=new User();
        user.updatePassword("kairlec");
        System.out.println(user.getPassword());
        user.updatePassword("kairlec2");
        System.out.println(user.getPassword());
    }

}
