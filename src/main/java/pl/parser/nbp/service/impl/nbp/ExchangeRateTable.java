package pl.parser.nbp.service.impl.nbp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import pl.parser.nbp.jaxb.adapter.CommaBigDecimalAdapter;
import pl.parser.nbp.jaxb.adapter.LocalDateAdapter;

// XML fields have been stripped to minimum used by application itself.
@Value
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@XmlRootElement(name = "tabela_kursow")
class ExchangeRateTable {
	@XmlElement(name = "data_publikacji")
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	private LocalDate publicationDate;
	
	@XmlElement(name="pozycja")
	private List<ExchangeRateTablePosition> positions;
	
	@Value
	@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
	@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
	static class ExchangeRateTablePosition {
		@XmlElement(name = "kod_waluty")
		private String currencyCode;
		
		@XmlElement(name = "kurs_kupna")
		@XmlJavaTypeAdapter(CommaBigDecimalAdapter.class)
		private BigDecimal buyingRate;
		
		@XmlElement(name = "kurs_sprzedazy")
		@XmlJavaTypeAdapter(CommaBigDecimalAdapter.class)
		private BigDecimal sellingRate;
	}
}
