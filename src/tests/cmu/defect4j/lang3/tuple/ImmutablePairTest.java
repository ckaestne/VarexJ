package cmu.defect4j.lang3.tuple;

import gov.nasa.jpf.util.test.TestJPF;
import org.junit.Test;

public class ImmutablePairTest extends TestJPF {

    private final String[] config = {"+nhandler.delegateUnhandledNative", "+classpath+=${jpf-core}/lib/junit-4.11.jar,${jpf-core}/lib/commons-lang3-3.2-SNAPSHOT.jar,${jpf-core}/lib/easymock.jar,${jpf-core}/lib/commons-io.jar"};
    public static void main(String[] testMethods){
        runTestsOfThisClass(testMethods);
    }
    @Test(timeout=10000)
    public void testHashCode() throws Exception {
        if (verifyNoPropertyViolation(config)) {
               org.apache.commons.lang3.tuple.ImmutablePairTest object = new org.apache.commons.lang3.tuple.ImmutablePairTest();
               object.testHashCode();
        }
    }

    @Test(timeout=10000)
    public void testToString() throws Exception {
        if (verifyNoPropertyViolation(config)) {
               org.apache.commons.lang3.tuple.ImmutablePairTest object = new org.apache.commons.lang3.tuple.ImmutablePairTest();
               object.testToString();
        }
    }

    @Test(timeout=10000)
    public void testEquals() throws Exception {
        if (verifyNoPropertyViolation(config)) {
               org.apache.commons.lang3.tuple.ImmutablePairTest object = new org.apache.commons.lang3.tuple.ImmutablePairTest();
               object.testEquals();
        }
    }

    @Test(timeout=10000)
    public void testBasic() throws Exception {
        if (verifyNoPropertyViolation(config)) {
               org.apache.commons.lang3.tuple.ImmutablePairTest object = new org.apache.commons.lang3.tuple.ImmutablePairTest();
               object.testBasic();
        }
    }

    @Test(timeout=10000)
    public void testPairOf() throws Exception {
        if (verifyNoPropertyViolation(config)) {
               org.apache.commons.lang3.tuple.ImmutablePairTest object = new org.apache.commons.lang3.tuple.ImmutablePairTest();
               object.testPairOf();
        }
    }

    @Test(timeout=10000)
    public void testSerialization() throws Exception {
        if (verifyNoPropertyViolation(config)) {
               org.apache.commons.lang3.tuple.ImmutablePairTest object = new org.apache.commons.lang3.tuple.ImmutablePairTest();
               object.testSerialization();
        }
    }

}