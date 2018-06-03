package de.uni_leipzig.crypto_news_docs.dto.country;

import java.util.ArrayList;
import java.util.List;

public class CountryCurrencyMapOutDto {

	private List<CountryCurrencyOutDto> timeSeriesValues;
	private String shortName;
	private String exchange;
	private String unit;

	public CountryCurrencyMapOutDto() {
	}

	public CountryCurrencyMapOutDto(
			List<CountryCurrencyOutDto> timeSeriesValues, String shortName, String exchange, String unit) {
		this.timeSeriesValues = timeSeriesValues;
		this.shortName = shortName;
		this.exchange = exchange;
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public List<CountryCurrencyOutDto> getTimeSeriesValues() {
		return timeSeriesValues;
	}

	public void setTimeSeriesValues(List<CountryCurrencyOutDto> timeSeriesValues) {
		this.timeSeriesValues = timeSeriesValues;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}
