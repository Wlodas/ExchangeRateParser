package pl.parser.nbp.service;

import java.time.LocalDate;

import lombok.Value;

@Value
public class ExchangeRateInfo {
	private String currencyCode;
	private LocalDate dateFrom, dateTo;
	private double meanBuyingRate, sellingRateStandardDeviation;
}
