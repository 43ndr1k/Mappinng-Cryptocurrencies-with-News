package de.uni_leipzig.crypto_news_docs.dto.crypto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class TimeSeriesValueDto {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Date date;
	private Double value;

	public TimeSeriesValueDto() {

	}

	public TimeSeriesValueDto(Date date, Double value,
			CryptoCurrencyDto cryptoCurrency) {
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
