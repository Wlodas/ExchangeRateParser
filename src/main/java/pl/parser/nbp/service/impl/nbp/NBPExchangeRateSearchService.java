package pl.parser.nbp.service.impl.nbp;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.jooq.lambda.Unchecked;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.parser.nbp.service.ExchangeRateInfo;
import pl.parser.nbp.service.ExchangeRateSearchService;
import pl.parser.nbp.service.impl.nbp.ExchangeRateTable.ExchangeRateTablePosition;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class NBPExchangeRateSearchService implements ExchangeRateSearchService {
	private static final Mean mean = new Mean();
	private static final StandardDeviation standardDeviation = new StandardDeviation();
	
	static final String directoryFormat = "dir%s";
	static final Year minYear = Year.of(2002);
	
	@Setter(AccessLevel.PACKAGE) // for testing purposes
	private Supplier<Year> currentYearSupplier = Year::now;
	
	private final HttpClient httpClient;
	
	@Override
	public ExchangeRateInfo findExchangeRate(String currencyCode, LocalDate dateFrom, LocalDate dateTo) {
		List<ExchangeRateTablePosition> positions = calculateDirectories(dateFrom, dateTo)
			// query exchange rate directories
			.map(directoryName -> httpClient.getExchangeRateFileNames(directoryName))
			.flatMap(Unchecked.function(filenameStream -> filenameStream.get().stream()))
			
			// query exchange rate tables
			.map(filename -> httpClient.getExchangeRateTable(filename))
			.map(Unchecked.function(Future::get))
			
			// filter data by publication date and currency code
			// TODO: dates should be also checked on XML filenames instead of internal XML values for better performance
			.filter(table -> !table.getPublicationDate().isBefore(dateFrom))
			.filter(table -> !table.getPublicationDate().isAfter(dateTo))
			.flatMap(table -> table.getPositions().stream())
			.filter(position -> currencyCode.equals(position.getCurrencyCode()))
			.collect(toList())
		;
		
		return new ExchangeRateInfo(
			currencyCode,
			dateFrom,
			dateTo,
			calculateMeanBuyingRate(positions),
			calculateSalesStandardDeviation(positions)
		);
	}
	
	Stream<String> calculateDirectories(LocalDate dateFrom, LocalDate dateTo) {
		int currentYear = currentYearSupplier.get().getValue();
		
		return IntStream.rangeClosed(minYear.getValue(), currentYear)
			// filter dates
			.filter(year -> year >= dateFrom.getYear())
			.filter(year -> year <= dateTo.getYear())
			// generate list of XML directories; current year's directory name has no number
			.mapToObj(year -> String.format(directoryFormat, year == currentYear ? "" : year))
		;
	}
	
	double calculateMeanBuyingRate(Collection<ExchangeRateTablePosition> positions) {
		return mean.evaluate(positions.stream()
			.map(p -> p.getBuyingRate())
			.mapToDouble(BigDecimal::doubleValue)
			.toArray()
		);
	}
	
	double calculateSalesStandardDeviation(Collection<ExchangeRateTablePosition> positions) {
		return standardDeviation.evaluate(positions.stream()
			.map(p -> p.getSellingRate())
			.mapToDouble(BigDecimal::doubleValue)
			.toArray()
		);
	}
	
}
