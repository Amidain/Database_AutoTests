package db_tests_v1;

import org.testng.Assert;
import org.testng.annotations.Test;

public class db_tests_v1 extends BaseTest{

    @Test
    void addTwoAndFour(){
        int actual = 2+4;
        int expected = 6;

        Assert.assertEquals(actual,expected, "Sum of two numbers is not correct, better learn math!");
    }

    @Test
    void addFiveAndSix(){
        int actual = 5+6;
        int expected = 11;

        Assert.assertEquals(actual,expected, "Sum of two numbers is not correct, better learn math!");
    }
}
