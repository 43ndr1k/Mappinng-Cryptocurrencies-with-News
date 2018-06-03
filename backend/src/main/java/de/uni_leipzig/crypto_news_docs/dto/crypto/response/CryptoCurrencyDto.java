package de.uni_leipzig.crypto_news_docs.dto.crypto.response;


import java.util.List;



public class CryptoCurrencyDto {

	private String shortName;
	private CryptoCurrencyDescriptionDto description;
	private List<CryptoCurrencyPriceDto> cryptoCurrencyPrices;

	public CryptoCurrencyDto() {

	}

	public CryptoCurrencyDto(String shortName,
			List<CryptoCurrencyPriceDto> cryptoCurrencyPrices, CryptoCurrencyDescriptionDto description) {
		this.shortName = shortName;
		this.cryptoCurrencyPrices = cryptoCurrencyPrices;
		this.description = description;
	}

	public List<CryptoCurrencyPriceDto> getCryptoCurrencyPrices() {
		return cryptoCurrencyPrices;
	}

	public void setCryptoCurrencyPrices(
			List<CryptoCurrencyPriceDto> cryptoCurrencyPrices) {
		this.cryptoCurrencyPrices = cryptoCurrencyPrices;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public CryptoCurrencyDescriptionDto getDescription() {
		return description;
	}

	public void setDescription(CryptoCurrencyDescriptionDto description) {
		this.description = description;
	}
}
