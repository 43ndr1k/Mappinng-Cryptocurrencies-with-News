package de.uni_leipzig.crypto_news_docs.dto.crypto.create;


import java.util.Date;
import javax.validation.constraints.NotNull;

public class TimeSeriesValueCreateDto {

	@NotNull
	private Date date;
	@NotNull
	private Double value;

	public TimeSeriesValueCreateDto() {

	}

	public TimeSeriesValueCreateDto(Date date, Double value) {
		this.date = date;
		this.value = value;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
}
