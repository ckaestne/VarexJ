package cmu;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.util.test.TestJPF;

import org.junit.Test;

public class ExampleRunner extends TestJPF {

	@Test
	public void runGPL() {
		 
		// TODO revise (call example directly?)
		Config config = JPF.createConfig(new String[] { "C:\\Users\\Loaner\\git\\Variability-Aware-Interpreter\\src\\tests\\cmu\\GPL.jpf" });
		JPF jpf = new JPF(config);
		jpf.run();
	}

}