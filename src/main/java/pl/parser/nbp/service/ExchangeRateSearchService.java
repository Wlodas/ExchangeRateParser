package pl.parser.nbp.service;

import java.time.LocalDate;

public interface ExchangeRateSearchService {
	ExchangeRateInfo findExchangeRate(String currencyCode, LocalDate dateFrom, LocalDate dateTo);
}
