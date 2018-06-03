package de.uni_leipzig.crypto_news_docs.dto.country;

import java.util.List;
import javax.validation.constraints.NotNull;

public class CountryCurrencyMapDto {

	private List<CountryCurrencyDto> timeSeriesValues;
	@NotNull
	private String shortName;
	@NotNull
	private String exchange;
	@NotNull
	private String unit;

	public CountryCurrencyMapDto() {

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

	public List<CountryCurrencyDto> getTimeSeriesValues() {
		return timeSeriesValues;
	}

	public void setTimeSeriesValues(List<CountryCurrencyDto> timeSeriesValues) {
		this.timeSeriesValues = timeSeriesValues;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}
