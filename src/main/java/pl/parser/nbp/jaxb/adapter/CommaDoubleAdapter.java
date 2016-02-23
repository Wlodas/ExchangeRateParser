package pl.parser.nbp.jaxb.adapter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Deprecated, because double type is not very precise to use for money.
 */
@Deprecated
public class CommaDoubleAdapter extends XmlAdapter<String, Double> {
	private final DecimalFormat format;
	
	public CommaDoubleAdapter() {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		
		format = new DecimalFormat();
		format.setDecimalFormatSymbols(symbols);
		format.setGroupingUsed(false);
		format.setMaximumFractionDigits(Integer.MAX_VALUE);
	}
	
	@Override
	public Double unmarshal(String v) throws ParseException {
		return format.parse(v).doubleValue();
	}
	
	@Override
	public String marshal(Double v) {
		return format.format(v.doubleValue());
	}
}
