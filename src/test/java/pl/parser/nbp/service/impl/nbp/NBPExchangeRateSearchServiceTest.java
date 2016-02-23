package pl.parser.nbp.service.impl.nbp;

import static java.util.stream.Collectors.toList;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static pl.parser.nbp.service.impl.nbp.NBPExchangeRateSearchService.directoryFormat;
import static pl.parser.nbp.service.impl.nbp.NBPExchangeRateSearchService.minYear;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import lombok.val;
import pl.parser.nbp.service.ExchangeRateInfo;
import pl.parser.nbp.service.impl.nbp.ExchangeRateTable.ExchangeRateTablePosition;

@Test(dependsOnGroups = HttpClientTest.TEST_GROUP)
public class NBPExchangeRateSearchServiceTest {
	// test data
	private static final String directoryFileName1 = String.format(directoryFormat, 2014);
	private static final String directoryFileName2 = String.format(directoryFormat, "");
	private static final String directoryFileName3 = String.format(directoryFormat, minYear.getValue());
	
	private static final String currencyCode1 = "EUR";
	private static final String currencyCode2 = "PLN";
	private static final String fakeCurrencyCode = "fake";
	
	private static final LocalDate dateFrom = LocalDate.of(2014, 1, 1);
	private static final LocalDate dateTo = LocalDate.of(2015, 1, 31);
	private static final LocalDate minDate = LocalDate.of(NBPExchangeRateSearchService.minYear.getValue(), 1, 1);
	private static final Year currentYear = Year.of(2015);
	
	private static final LocalDate earlyDate = LocalDate.of(2000, 1, 1);
	private static final LocalDate lateDate = LocalDate.of(2016, 1, 31);
	
	private static final String xmlFileName1 = "xml1";
	private static final String xmlFileName2 = "xml2";
	
	private static final ExchangeRateTablePosition position1 = new ExchangeRateTablePosition(currencyCode1, BigDecimal.ONE, BigDecimal.ONE);
	private static final ExchangeRateTablePosition position2 = new ExchangeRateTablePosition(currencyCode1, BigDecimal.TEN, BigDecimal.TEN);
	private static final ExchangeRateTablePosition position3 = new ExchangeRateTablePosition(currencyCode2, BigDecimal.ONE, BigDecimal.ONE);
	
	private static final ExchangeRateTable table1 = new ExchangeRateTable(dateFrom, Arrays.asList(position1, position3));
	private static final ExchangeRateTable table2 = new ExchangeRateTable(dateFrom, Arrays.asList(position2, position2));
	
	// tested service
	private NBPExchangeRateSearchService service;
	
	@BeforeSuite
	public void init() {
		HttpClient httpClient = mock(HttpClient.class);
		
		doReturn(CompletableFuture.completedFuture(Arrays.asList(xmlFileName1, xmlFileName2)))
			.when(httpClient).getExchangeRateFileNames(directoryFileName1);
		doReturn(CompletableFuture.completedFuture(Collections.emptyList()))
			.when(httpClient).getExchangeRateFileNames(directoryFileName2);
		
		doReturn(CompletableFuture.completedFuture(table1)).when(httpClient).getExchangeRateTable(xmlFileName1);
		doReturn(CompletableFuture.completedFuture(table2)).when(httpClient).getExchangeRateTable(xmlFileName2);
		
		service = new NBPExchangeRateSearchService(httpClient);
		service.setCurrentYearSupplier(() -> currentYear);
	}
	
	public void expectOnlyEarliestDirectory() {
		val result = service.calculateDirectories(earlyDate, minDate).collect(toList());
		assertThat(result).containsOnly(directoryFileName3);
	}
	
	public void expectTwoDirectoriesFoundForDateToOneYearOutsideAvailableRange() {
		val result = service.calculateDirectories(dateFrom, lateDate).collect(toList());
		assertThat(result).containsOnly(directoryFileName1, directoryFileName2);
	}
	
	public void expectNoDataForWrongDates() {
		val result = service.findExchangeRate(currencyCode1, lateDate, lateDate);
		assertThat(result).isEqualTo(new ExchangeRateInfo(currencyCode1, lateDate, lateDate, Double.NaN, Double.NaN));
	}
	
	public void expectNoDataForWrongCurrencyCode() {
		val result = service.findExchangeRate(fakeCurrencyCode, dateFrom, dateTo);
		assertThat(result).isEqualTo(new ExchangeRateInfo(fakeCurrencyCode, dateFrom, dateTo, Double.NaN, Double.NaN));
	}
	
	public void expectNormalExecution() {
		val result = service.findExchangeRate(currencyCode1, dateFrom, dateTo);
		val expectedMeanBuyingRate = service.calculateMeanBuyingRate(Arrays.asList(position1, position2, position2));
		val expectedSalesStandardDeviation = service.calculateSalesStandardDeviation(Arrays.asList(position1, position2, position2));
		
		assertThat(result).isEqualTo(new ExchangeRateInfo(currencyCode1, dateFrom, dateTo, expectedMeanBuyingRate, expectedSalesStandardDeviation));
	}
}
