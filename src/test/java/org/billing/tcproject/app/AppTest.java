package org.billing.tcproject.app;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Unit test
 */
public class AppTest {
    @Test
    public void testCalA() throws Exception {
        App calculate = new App();
        int n = calculate.calA(2, 2);

        assertEquals(4, n);
    }
}
