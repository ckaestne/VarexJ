package cmu.defect4j.joda.time.field;

import gov.nasa.jpf.util.test.TestJPF;
import org.junit.Test;
import junit.framework.TestCase;
public class TestPreciseDurationField extends TestJPF {

    private final String[] config = {"+nhandler.delegateUnhandledNative", "+classpath+=${jpf-core}/lib/junit-3.8.2.jar,lib/joda-convert-1.2.jar,jodatime/target/classes/,jodatime/target/test-classes,jodatime/"};

    public static void main(String[] testMethods){
        runTestsOfThisClass(testMethods);
    }
	@Test(timeout=120000)
	public void test_constructor() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_constructor");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_getType() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_getType");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_getName() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_getName");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_toString() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_toString");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_isSupported() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_isSupported");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_add_long_int() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_add_long_int");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_add_long_long() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_add_long_long");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_getDifferenceAsLong_long_long() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_getDifferenceAsLong_long_long");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_equals() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_equals");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_hashCode() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_hashCode");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_isPrecise() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_isPrecise");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_getUnitMillis() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_getUnitMillis");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_getValue_long() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_getValue_long");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_getValueAsLong_long() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_getValueAsLong_long");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_getValue_long_long() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_getValue_long_long");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_getValueAsLong_long_long() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_getValueAsLong_long_long");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_getMillis_int() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_getMillis_int");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_getMillis_long() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_getMillis_long");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_getMillis_int_long() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_getMillis_int_long");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_getMillis_long_long() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_getMillis_long_long");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_getDifference_long_int() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_getDifference_long_int");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void test_compareTo() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("test_compareTo");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testSerialization() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.field.TestPreciseDurationField("testSerialization");
			testcase.run();
		}
	}

}