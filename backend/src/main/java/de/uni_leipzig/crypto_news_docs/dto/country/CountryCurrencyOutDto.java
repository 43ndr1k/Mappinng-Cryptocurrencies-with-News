package de.uni_leipzig.crypto_news_docs.dto.country;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class CountryCurrencyOutDto {

	/*private static final SimpleDateFormat dateFormat
			= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");*/


	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	private Date date;
	private Double value;

	public CountryCurrencyOutDto() {
	}

	public CountryCurrencyOutDto(Double value, Date date) {
		this.value = value;
		this.date = date;
	}



	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	/*public Date getSubmissionDateConverted() throws ParseException {
		return dateFormat.parse(this.date);
	}

	public void setSubmissionDate(Date date) {
		this.date = dateFormat.format(date);
	}*/
}
