package de.uni_leipzig.crypto_news_docs.dto.crypto.create;


import de.uni_leipzig.crypto_news_docs.model.AbstractIdEntity;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrency;
import de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto.CryptoCurrencyPrice;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

public class CryptoCurrencyExchangeInitCreateDto {

	@NotNull
	private String exchangeName;
	@NotNull
	private String position;

	private List<CryptoCurrencyCreateDto> cryptoCurrencies;
	//private List<CryptoCurrencyPriceCreateDto> cryptoCurrencyPrices;


	public CryptoCurrencyExchangeInitCreateDto() {

	}

	public CryptoCurrencyExchangeInitCreateDto(String exchangeName, String position,
			List<CryptoCurrencyCreateDto> cryptoCurrencies,
			List<CryptoCurrencyPriceCreateDto> cryptoCurrencyPrices) {
		this.exchangeName = exchangeName;
		this.position = position;
		this.cryptoCurrencies = cryptoCurrencies;
		//this.cryptoCurrencyPrices = cryptoCurrencyPrices;
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

	public List<CryptoCurrencyCreateDto> getCryptoCurrencies() {
		return cryptoCurrencies;
	}

	public void setCryptoCurrencies(
			List<CryptoCurrencyCreateDto> cryptoCurrencies) {
		this.cryptoCurrencies = cryptoCurrencies;
	}

/*	public List<CryptoCurrencyPriceCreateDto> getCryptoCurrencyPrices() {
		return cryptoCurrencyPrices;
	}

	public void setCryptoCurrencyPrices(
			List<CryptoCurrencyPriceCreateDto> cryptoCurrencyPrices) {
		this.cryptoCurrencyPrices = cryptoCurrencyPrices;
	}*/
}
