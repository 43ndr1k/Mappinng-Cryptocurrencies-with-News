package de.uni_leipzig.crypto_news_docs.dto.crypto.create;

import java.util.List;
import javax.validation.constraints.NotNull;

public class TimeSeriesValuesCreateAsListDto {

	@NotNull
	private String shortName;
	@NotNull
	private String exchange;
	@NotNull
	private String unit;
	@NotNull
	private List<TimeSeriesValueCreateDto> timeSeriesValues;

	public TimeSeriesValuesCreateAsListDto() {}

	public TimeSeriesValuesCreateAsListDto(String exchange, String unit, String shortName,
			List<TimeSeriesValueCreateDto> timeSeriesValues) {
		this.exchange = exchange;
		this.unit = unit;
		this.timeSeriesValues = timeSeriesValues;
		this.shortName = shortName;
	}

	public String getExchange() {
		return exchange;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public List<TimeSeriesValueCreateDto> getTimeSeriesValues() {
		return timeSeriesValues;
	}

	public void setTimeSeriesValues(
			List<TimeSeriesValueCreateDto> timeSeriesValues) {
		this.timeSeriesValues = timeSeriesValues;
	}
}
