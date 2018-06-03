package de.uni_leipzig.crypto_news_docs.dto.crypto.response;


import java.util.List;

public class CryptoCurrencyExchangeDto {


	private String exchangeName;
	private String position;
	private List<CryptoCurrencyDto> cryptoCurrencies;
	public CryptoCurrencyExchangeDto() {

	}

	public CryptoCurrencyExchangeDto(String exchangeName, String position,
			List<CryptoCurrencyDto> cryptoCurrencies,
			List<CryptoCurrencyPriceDto> cryptoCurrencyPrices) {
		this.exchangeName = exchangeName;
		this.position = position;
		this.cryptoCurrencies = cryptoCurrencies;
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

	public List<CryptoCurrencyDto> getCryptoCurrencies() {
		return cryptoCurrencies;
	}

	public void setCryptoCurrencies(
			List<CryptoCurrencyDto> cryptoCurrencies) {
		this.cryptoCurrencies = cryptoCurrencies;
	}

}
