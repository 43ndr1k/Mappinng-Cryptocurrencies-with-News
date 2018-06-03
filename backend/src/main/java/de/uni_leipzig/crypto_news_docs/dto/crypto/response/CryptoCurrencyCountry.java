package de.uni_leipzig.crypto_news_docs.dto.crypto.response;

import java.util.Map;

public class CryptoCurrencyCountry {

	Map<String, Double> data;
	String unit;

	public CryptoCurrencyCountry() {

	}

	public CryptoCurrencyCountry(Map<String, Double> data, String unit) {
		this.data = data;
		this.unit = unit;
	}

	public Map<String, Double> getData() {
		return data;
	}

	public void setData(Map<String, Double> data) {
		this.data = data;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}
