package de.uni_leipzig.crypto_news_docs.model.assets.currency.country;

import de.uni_leipzig.crypto_news_docs.model.AbstractIdEntity;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "country_currencies_exchanges")
public class CountryCurrencyExchange extends AbstractIdEntity {

	private String exchange;

	private String unit;

	private String shortName;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "crypto_currency_exchanges_id")
	private List<CountryCurrency> timeSeriesValues;

	public CountryCurrencyExchange(String exchange, String unit, String shortName,
			List<CountryCurrency> timeSeriesValues) {
		this.exchange = exchange;
		this.unit = unit;
		this.shortName = shortName;
		this.timeSeriesValues = timeSeriesValues;
	}

	public CountryCurrencyExchange() {

	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public List<CountryCurrency> getTimeSeriesValues() {
		return timeSeriesValues;
	}

	public void setTimeSeriesValues(
			List<CountryCurrency> timeSeriesValues) {
		this.timeSeriesValues = timeSeriesValues;
	}

	public String getExchange() {
		return exchange;
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
}