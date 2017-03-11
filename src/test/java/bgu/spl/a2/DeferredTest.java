package bgu.spl.a2;

//import sun.plugin.dom.exception.InvalidStateException;

//import static javafx.scene.input.KeyCode.T;
import static org.junit.Assert.*;

/**
 * Created by Yonatan Bitton on 11/12/2016.
 */
public class DeferredTest {
    private Deferred d;

    @org.junit.Before
    public void setUp() throws Exception {
        d=new Deferred();

    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void get() throws Exception {
        try {
            if (d.isResolved()==false);
        } catch (Exception e) {
            fail("no result exception");}
        d.setResult(2);
        assertTrue(d.get()!=null);
    }

    @org.junit.Test
    public void isResolved() throws Exception {
        assertTrue (d.isResolved()==false);
    }

    @org.junit.Test
    public void resolve() throws Exception {
        try {
            if (d.isResolved()==true);
        } catch (Exception e) {
            fail("no result exception");}
        d.resolve(5);
        assertTrue ((Integer)d.get()==5);
    }

    @org.junit.Test
    public void whenResolved() throws Exception {
        Runnable r = () -> {
            Integer x=8;

        };
        d.whenResolved(r);
        assertTrue(d.getCallbacks().get(d.getCallbacks().size()-1)!=null);
    }

}