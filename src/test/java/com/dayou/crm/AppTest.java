package com.dayou.crm;

import static org.junit.Assert.assertTrue;

import com.dayou.crm.dao.UserMapper;
import com.dayou.crm.utils.Md5Util;
import com.dayou.crm.utils.UserIDBase64;
import com.dayou.crm.vo.User;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void test1(){

        String a = "gnzLDuqKcGxMNKFokfhOew==";

        Integer integer = UserIDBase64.decoderUserID(a);


    }
}
