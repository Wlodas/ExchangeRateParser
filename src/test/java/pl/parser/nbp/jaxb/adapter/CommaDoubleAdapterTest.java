package pl.parser.nbp.jaxb.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;

import org.assertj.core.data.Offset;
import org.testng.annotations.Test;

/**
 * Deprecated, because double type is not very precise to use for money.
 */
@Deprecated
@Test(enabled = false)
public class CommaDoubleAdapterTest {
	private static final String stringValue = "1234,5678";
	private static final double doubleValue = 1234.5678;
	private static final double doubleValueOffset = 0.00001;
	
	private final CommaDoubleAdapter adapter = new CommaDoubleAdapter();
	
	public void unmarshal() throws ParseException {
		assertThat(adapter.unmarshal(stringValue)).isEqualTo(doubleValue, Offset.offset(doubleValueOffset));
	}
	
	public void marshal() {
		assertThat(adapter.marshal(doubleValue)).isEqualTo(stringValue);
	}
}
