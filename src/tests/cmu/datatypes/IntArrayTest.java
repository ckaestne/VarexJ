package cmu.datatypes;

import gov.nasa.jpf.annotation.Conditional;
import gov.nasa.jpf.util.test.TestJPF;

import org.junit.Test;

public class IntArrayTest extends TestJPF {

	static String JPF_CONFIGURATION = "+search.class= .search.RandomSearch";
	
	@Conditional
	static boolean a = true;
	@Conditional
	static boolean b = true;
	@Conditional
	static boolean c = true;
	@Conditional
	static boolean d = true;

	@Test
	public void intArrayTest() throws Exception {
		if (verifyNoPropertyViolation(JPF_CONFIGURATION)) {
			int[] array = new int[10];
			if (a) {
				array[0] = 1;
			}
			System.out.println(array[0]);
			if (a) {
				assertTrue(array[0] == 1);
			} else {
				assertTrue(array[0] == 0);
			}
			
		}
	}
	
	@Test
	public void intArrayTest2() throws Exception {
		if (verifyNoPropertyViolation(JPF_CONFIGURATION)) {
			int[] array = new int[10];
			if (a) {
				array = new int [11];
			}
			array[0] = 1;
			assertTrue(array[0] == 1);
			
			System.out.println(array.length);
			if (a) {
				assertTrue(array.length == 11);
			} else {
				assertTrue(array.length == 10);
			}
			
		}
	}
	
	@Test
	public void intArrayCopyTest() throws Exception {
		if (verifyNoPropertyViolation(JPF_CONFIGURATION)) {
			int[] array = new int[10];
			for (int i = 0; i < array.length; i++) {
				if (a) {
					array[i] = i;
				} else {
					array[i] = i * i;
				}
			}
			int[] array2 = new int[10];
			System.arraycopy(array, 0, array2, 0, array.length);
			
			for (int i = 0; i < array.length; i++) {
				System.out.println(array2[i]);
				if (a) {
					assertTrue(array2[i] == i);
				} else {
					assertTrue(array2[i] == i * i);
				}
			}
		}
	}

}
