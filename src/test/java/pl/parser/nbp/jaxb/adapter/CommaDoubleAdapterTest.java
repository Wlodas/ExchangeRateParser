package pl.parser.nbp.jaxb.adapter;

import static org.testng.Assert.assertEquals;

import java.text.ParseException;

import org.testng.annotations.Test;

/**
 * Deprecated, because double type is not very precise to use for money.
 */
@Deprecated
@Test(enabled = false)
public class CommaDoubleAdapterTest {
	private static final String stringValue = "1234,5678";
	private static final double doubleValue = 1234.5678;
	private static final double doubleValueDelta = 0.00001;
	
	private final CommaDoubleAdapter adapter = new CommaDoubleAdapter();
	
	public void unmarshal() throws ParseException {
		assertEquals(adapter.unmarshal(stringValue), doubleValue, doubleValueDelta);
	}
	
	public void marshal() {
		assertEquals(adapter.marshal(doubleValue), stringValue);
	}
}
