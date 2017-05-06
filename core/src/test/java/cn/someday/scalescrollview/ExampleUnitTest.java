package cn.someday.scalescrollview;

import org.junit.Test;

import java.security.spec.ECField;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testWidth() throws Exception
    {
        int width = 3;
        float out = ((float) width)/2;
        System.out.print(out);
    }

}