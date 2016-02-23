package pl.parser.nbp.jaxb.adapter;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.text.ParseException;

import org.testng.annotations.Test;

@Test(groups = CommaBigDecimalAdapterTest.TEST_GROUP)
public class CommaBigDecimalAdapterTest {
	public static final String TEST_GROUP = "CommaBigDecimalAdapterTest";
	
	private static final String stringValue = "1234,5678";
	private static final BigDecimal bigDecimalValue = new BigDecimal("1234.5678");
	
	private final CommaBigDecimalAdapter adapter = new CommaBigDecimalAdapter();
	
	public void unmarshal() throws ParseException {
		assertEquals(adapter.unmarshal(stringValue), bigDecimalValue);
	}
	
	public void marshal() {
		assertEquals(adapter.marshal(bigDecimalValue), stringValue);
	}
}
