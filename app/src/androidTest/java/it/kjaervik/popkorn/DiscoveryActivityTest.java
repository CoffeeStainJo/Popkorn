package it.kjaervik.popkorn;

import android.test.AndroidTestCase;


public class DiscoveryActivityTest extends AndroidTestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    public void testCreateGridView() {

        int a = 5;
        int b = 3;
        int c = 5;
        int d = 10;

        assertEquals("X should be equal", a, c);
        assertTrue("Y should be true", d > a);

        if (b > d) {
            fail("XX should never happen");
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}