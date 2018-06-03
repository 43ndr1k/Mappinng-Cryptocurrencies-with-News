package de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto;


import de.uni_leipzig.crypto_news_docs.model.AbstractIdEntity;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "crypto_currencies_price")
public class CryptoCurrencyPrice extends AbstractIdEntity {

	@Column(length = 15, nullable = false)
	private String unit;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "crypto_currency_price_id")
	private List<TimeSeriesValue> timeSeriesValues;

	/*@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "cryptoCurrencyExchange_Id", nullable = false)
	private CryptoCurrencyExchange cryptoCurrencyExchange;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "cryptoCurrency_Id", nullable = false)
	private CryptoCurrency cryptoCurrency;*/

	public CryptoCurrencyPrice() {

	}

	public CryptoCurrencyPrice(String unit,
			List<TimeSeriesValue> timeSeriesValues) {
		this.unit = unit;
		this.timeSeriesValues = timeSeriesValues;
	}

	public List<TimeSeriesValue> getTimeSeriesValues() {
		return timeSeriesValues;
	}

	public void setTimeSeriesValues(
			List<TimeSeriesValue> timeSeriesValues) {
		this.timeSeriesValues = timeSeriesValues;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}



}
