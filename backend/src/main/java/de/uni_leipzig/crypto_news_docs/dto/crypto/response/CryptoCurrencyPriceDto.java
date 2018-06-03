package de.uni_leipzig.crypto_news_docs.dto.crypto.response;


import java.util.List;

public class CryptoCurrencyPriceDto {

	private String unit;
	private List<TimeSeriesValueDto> timeSeriesValues;

	public CryptoCurrencyPriceDto() {

	}

	public CryptoCurrencyPriceDto(String unit, List<TimeSeriesValueDto> timeSeriesValues) {
		this.unit = unit;
		this.timeSeriesValues = timeSeriesValues;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public List<TimeSeriesValueDto> getTimeSeriesValues() {
		return timeSeriesValues;
	}

	public void setTimeSeriesValues(
			List<TimeSeriesValueDto> timeSeriesValues) {
		this.timeSeriesValues = timeSeriesValues;
	}
}
