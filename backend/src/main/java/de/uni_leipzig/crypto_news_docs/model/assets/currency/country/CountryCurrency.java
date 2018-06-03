package de.uni_leipzig.crypto_news_docs.model.assets.currency.country;

import de.uni_leipzig.crypto_news_docs.model.AbstractIdEntity;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "country_currencies")
public class CountryCurrency extends AbstractIdEntity {

	private Double value;
	private Date date;

	public CountryCurrency() {
	}

	public CountryCurrency(Double value, Date date) {

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

}