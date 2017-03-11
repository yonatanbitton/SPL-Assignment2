package bgu.spl.a2;

import bgu.spl.a2.VersionMonitor;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class VersionMonitorTest {
    private VersionMonitor v;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        v = new VersionMonitor();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetVersion() throws Exception{
        assertEquals("Version wasn't initialized to 0", 0, v.getVersion());
        v.setVersion(2);
        assertEquals("Expected result 2" ,2, v.getVersion());
    }

    @Test
    public void testInc() throws Exception{
        assertEquals("Version wasn't initialized to 0", 0, v.getVersion());
        v.inc();
        assertEquals(1, v.getVersion());
        v.inc();
        v.inc();
        assertEquals("Expected result 4",3, v.getVersion());

    }

    @Test

    public void testAwait() throws Exception{
        Runnable r = () -> {
            try {
                v.await(0);
                assertEquals("Expected version is 1",1,v.getVersion());
            } catch (InterruptedException e) {
                fail("await exception");
            }
        };
        Thread t=new Thread(r);
        t.start();
        v.inc();
        t.sleep(500);
    }

}