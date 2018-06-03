package de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto;


import de.uni_leipzig.crypto_news_docs.model.AbstractIdEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "crypto_currencies_exchanges")
public class CryptoCurrencyExchange extends AbstractIdEntity {

	@Column(unique = true, nullable = false)
	private String exchangeName;

	@Column(nullable = false)
	private String position;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "crypto_currency_exchange_id")
	private List<CryptoCurrency> cryptoCurrencies;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "crypto_currency_exchange_id")
	private List<CryptoCurrencyPrice> cryptoCurrencyPrices = new ArrayList<>();

	public CryptoCurrencyExchange() {
	}

	public CryptoCurrencyExchange(String exchangeName, String position,
			List<CryptoCurrency> cryptoCurrencies,
			List<CryptoCurrencyPrice> cryptoCurrencyPrices) {
		this.exchangeName = exchangeName;
		this.position = position;
		this.cryptoCurrencies = cryptoCurrencies;
		this.cryptoCurrencyPrices = cryptoCurrencyPrices;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public List<CryptoCurrency> getCryptoCurrencies() {
		return cryptoCurrencies;
	}

	public void setCryptoCurrencies(
			List<CryptoCurrency> cryptoCurrencies) {
		this.cryptoCurrencies = cryptoCurrencies;
	}

	public List<CryptoCurrencyPrice> getCryptoCurrencyPrices() {
		return cryptoCurrencyPrices;
	}

	public void setCryptoCurrencyPrices(
			List<CryptoCurrencyPrice> cryptoCurrencyPrices) {
		this.cryptoCurrencyPrices = cryptoCurrencyPrices;
	}
}
