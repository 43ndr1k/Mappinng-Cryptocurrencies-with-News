package de.uni_leipzig.crypto_news_docs.dto.crypto.create;

public class CryptoCurrencyPriceCreateDto {

	private String unit;


	public CryptoCurrencyPriceCreateDto() {

	}

	public CryptoCurrencyPriceCreateDto(String unit) {
		this.unit = unit;

	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}


}
