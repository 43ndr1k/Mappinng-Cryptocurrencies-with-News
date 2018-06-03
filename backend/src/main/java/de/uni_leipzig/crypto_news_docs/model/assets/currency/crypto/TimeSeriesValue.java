package de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto;


import de.uni_leipzig.crypto_news_docs.model.AbstractIdEntity;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "time_series_values")
public class TimeSeriesValue extends AbstractIdEntity {

	private Date date;
	private Double value;

	/*@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "cryptoCurrencyPrice_Id", nullable = false)
	private CryptoCurrencyPrice cryptoCurrencyPrice;*/

	public TimeSeriesValue() {

	}

	public TimeSeriesValue(Date date, Double value) {
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
