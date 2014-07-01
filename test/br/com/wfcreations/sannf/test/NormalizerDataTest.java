package br.com.wfcreations.sannf.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.wfcreations.sannf.data.SupervisedPattern;
import br.com.wfcreations.sannf.data.SupervisedSet;
import br.com.wfcreations.sannf.data.normalization.INormalizer;
import br.com.wfcreations.sannf.data.normalization.Range;
import junit.framework.TestCase;

public class NormalizerDataTest extends TestCase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	protected void setUp() throws Exception {
		super.setUp();
	}

	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void test() {
		SupervisedSet data = new SupervisedSet(4, 1);
		data.addPattern(new SupervisedPattern(new double[] { 2104, 5, 1, 45 }, new double[] { 460 }));
		data.addPattern(new SupervisedPattern(new double[] { 1416, 3, 2, 40 }, new double[] { 232 }));
		data.addPattern(new SupervisedPattern(new double[] { 1534, 3, 2, 30 }, new double[] { 315 }));
		data.addPattern(new SupervisedPattern(new double[] { 852, 2, 1, 36 }, new double[] { 178 }));

		printData(data);

		INormalizer mean = new Range(-1, 1);
		mean.normalize(data);

		printData(data);

	}

	private void printData(SupervisedSet data) {
		for (SupervisedPattern pattern : data.getPatterns()) {
			for (double v : pattern.getInputs()) {
				System.out.print(v);
				System.out.print(' ');
			}
			System.out.print(" | ");
			for (double v : pattern.getOutputs()) {
				System.out.print(v);
				System.out.print(' ');
			}
			System.out.println();
		}
	}
}
