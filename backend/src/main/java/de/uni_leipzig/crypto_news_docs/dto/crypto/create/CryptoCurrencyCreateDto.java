package de.uni_leipzig.crypto_news_docs.dto.crypto.create;

import javax.validation.constraints.NotNull;

public class CryptoCurrencyCreateDto {

	@NotNull
	private String shortName;
	@NotNull
	private String unit;
	@NotNull
	private String exchange;

	private CryptoCurrencyDescriptionCreateDto description;

	public CryptoCurrencyCreateDto() {

	}

	public CryptoCurrencyCreateDto(String shortName, String unit, String exchange, CryptoCurrencyDescriptionCreateDto description) {
		this.shortName = shortName;
		this.unit = unit;
		this.exchange = exchange;
		this.description = description;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public CryptoCurrencyDescriptionCreateDto getDescription() {
		return description;
	}

	public void setDescription(CryptoCurrencyDescriptionCreateDto description) {
		this.description = description;
	}
}
