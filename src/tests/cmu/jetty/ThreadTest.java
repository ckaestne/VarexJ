package cmu.jetty;

import gov.nasa.jpf.util.test.TestJPF;
import org.junit.Test;

/**
 * @author: chupanw
 */
public class ThreadTest extends TestJPF{
    private final String[] config = {"+nhandler.delegateUnhandledNative", "+vm.scheduler_factory.class = gov.nasa.jpf.vm.DefaultSchedulerFactory", "+search.class=.search.RandomSearch", "+classpath+=${jpf-core}/lib/junit-4.11.jar,lib/jetty-all-7.6.18-SNAPSHOT.jar,lib/servlet-api-2.5.jar"};

    /**
     * To see whether threads can switch
     */
    @Test
    public void testSwap() {
        if (verifyNoPropertyViolation(config)) {
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 1000; i++) {
                            System.out.println("T1");
                    }
                }
            });
            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 1000; i++) {
                        System.out.println("T2");
                    }
                }
            });
            t1.start();
            t2.start();
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * To see whether Thread.yield() works
     */
    @Test
    public void testYield(){
        //fixme
    }

}
