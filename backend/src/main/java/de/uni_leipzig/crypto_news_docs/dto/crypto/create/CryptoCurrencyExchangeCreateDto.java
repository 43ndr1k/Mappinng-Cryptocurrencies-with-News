package de.uni_leipzig.crypto_news_docs.dto.crypto.create;


import java.util.List;
import javax.validation.constraints.NotNull;

public class CryptoCurrencyExchangeCreateDto {


	@NotNull
	private String exchangeName;
	@NotNull
	private String position;


	public CryptoCurrencyExchangeCreateDto() {

	}

	public CryptoCurrencyExchangeCreateDto(java.lang.String exchangeName, java.lang.String position,
			List<CryptoCurrencyCreateDto> cryptoCurrencies) {
		this.exchangeName = exchangeName;
		this.position = position;
	}

	public java.lang.String getPosition() {
		return position;
	}

	public void setPosition(java.lang.String position) {
		this.position = position;
	}

	public java.lang.String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(java.lang.String exchangeName) {
		this.exchangeName = exchangeName;
	}


}
