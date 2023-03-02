package db_tests_v2;

import org.testng.Assert;
import org.testng.annotations.Test;

public class db_test_v2 extends BaseTest{

@Test
    void multiplyThreeTimesFour(){
    int actual = 3 * 4;
    int expected = 12;

    Assert.assertEquals(actual,expected, "Result of multiplication is wrong! ");
}
}
