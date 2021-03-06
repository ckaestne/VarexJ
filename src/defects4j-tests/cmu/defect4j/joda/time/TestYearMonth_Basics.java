package cmu.defect4j.joda.time;

import gov.nasa.jpf.util.test.TestJPF;
import org.junit.Test;
import junit.framework.TestCase;
public class TestYearMonth_Basics extends TestJPF {

    private final String[] config = {"+nhandler.delegateUnhandledNative", "+classpath+=${jpf-core}/lib/junit-3.8.2.jar,lib/joda-convert-1.2.jar,jodatime/target/classes/,jodatime/target/test-classes,jodatime/"};

    public static void main(String[] testMethods){
        runTestsOfThisClass(testMethods);
    }
	@Test(timeout=120000)
	public void testIsSupported() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testIsSupported");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testGet() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testGet");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testEqualsHashCode() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testEqualsHashCode");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testCompareTo() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testCompareTo");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testWithChronologyRetainFields_Chrono() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testWithChronologyRetainFields_Chrono");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testWithChronologyRetainFields_sameChrono() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testWithChronologyRetainFields_sameChrono");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testWithChronologyRetainFields_nullChrono() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testWithChronologyRetainFields_nullChrono");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testWithField() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testWithField");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testWithField_nullField() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testWithField_nullField");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testWithField_unknownField() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testWithField_unknownField");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testWithField_same() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testWithField_same");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testWithFieldAdded() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testWithFieldAdded");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testWithFieldAdded_nullField_zero() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testWithFieldAdded_nullField_zero");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testWithFieldAdded_nullField_nonZero() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testWithFieldAdded_nullField_nonZero");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testWithFieldAdded_zero() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testWithFieldAdded_zero");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testWithFieldAdded_unknownField() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testWithFieldAdded_unknownField");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testIsEqual_YM() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testIsEqual_YM");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testIsBefore_YM() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testIsBefore_YM");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testIsAfter_YM() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testIsAfter_YM");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testWithChronologyRetainFields_invalidInNewChrono() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testWithChronologyRetainFields_invalidInNewChrono");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testSerialization() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testSerialization");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testGetValue() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testGetValue");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testGetValues() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testGetValues");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testGetField() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testGetField");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testGetFieldType() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testGetFieldType");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testGetFieldTypes() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testGetFieldTypes");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testWithers() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testWithers");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testToString_String() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testToString_String");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testToString_DTFormatter() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testToString_DTFormatter");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testToLocalDate() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testToLocalDate");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testToInterval() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testToInterval");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testPlus_RP() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testPlus_RP");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testPlusYears_int() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testPlusYears_int");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testPlusMonths_int() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testPlusMonths_int");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testMinus_RP() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testMinus_RP");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testMinusYears_int() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testMinusYears_int");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testMinusMonths_int() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testMinusMonths_int");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testProperty() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testProperty");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testToString_String_Locale() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testToString_String_Locale");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testSize() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testSize");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testGetFields() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testGetFields");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testToDateTime_RI() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testToDateTime_RI");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testToDateTime_nullRI() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testToDateTime_nullRI");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testToInterval_Zone() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testToInterval_Zone");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testToInterval_nullZone() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testToInterval_nullZone");
			testcase.run();
		}
	}

	@Test(timeout=120000)
	public void testToString() throws Exception {
		if (verifyNoPropertyViolation(config)) {
			TestCase testcase = new org.joda.time.TestYearMonth_Basics("testToString");
			testcase.run();
		}
	}

}