package de.uni_leipzig.crypto_news_docs.dto.country;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.validation.constraints.NotNull;

public class CountryCurrencyDto {

	/*private static final SimpleDateFormat dateFormat
			= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");*/



	private Double value;
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")

	private Date date;

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
