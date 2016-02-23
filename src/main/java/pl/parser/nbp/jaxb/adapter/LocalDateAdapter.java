package pl.parser.nbp.jaxb.adapter;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
	@Override
	public LocalDate unmarshal(String v) {
		return LocalDate.parse(v);
	}
	
	@Override
	public String marshal(LocalDate v) {
		return v.toString();
	}
}
