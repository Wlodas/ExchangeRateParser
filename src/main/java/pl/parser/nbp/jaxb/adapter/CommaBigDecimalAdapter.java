package pl.parser.nbp.jaxb.adapter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CommaBigDecimalAdapter extends XmlAdapter<String, BigDecimal> {
	private final DecimalFormat format;
	
	public CommaBigDecimalAdapter() {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		
		format = new DecimalFormat();
		format.setDecimalFormatSymbols(symbols);
		format.setParseBigDecimal(true);
		format.setGroupingUsed(false);
		format.setMaximumFractionDigits(Integer.MAX_VALUE);
	}
	
	@Override
	public BigDecimal unmarshal(String v) throws ParseException {
		return (BigDecimal) format.parse(v);
	}
	
	@Override
	public String marshal(BigDecimal v) {
		return format.format(v);
	}
}