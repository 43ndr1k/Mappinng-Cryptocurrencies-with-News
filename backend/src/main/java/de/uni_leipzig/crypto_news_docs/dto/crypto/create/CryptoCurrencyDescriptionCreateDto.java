package de.uni_leipzig.crypto_news_docs.dto.crypto.create;

import de.uni_leipzig.crypto_news_docs.model.AbstractIdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

public class CryptoCurrencyDescriptionCreateDto {

	@NotNull
	private String shortName;

	private String longName;

	private String wikiEnglish;

	private String wikiGerman;

	public CryptoCurrencyDescriptionCreateDto() {
	}

	public CryptoCurrencyDescriptionCreateDto(String shortName, String longName, String wikiEnglish, String wikiGerman) {
		this.shortName = shortName;
		this.longName = longName;
		this.wikiEnglish = wikiEnglish;
		this.wikiGerman = wikiGerman;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public String getWikiEnglish() {
		return wikiEnglish;
	}

	public void setWikiEnglish(String wikiEnglish) {
		this.wikiEnglish = wikiEnglish;
	}

	public String getWikiGerman() {
		return wikiGerman;
	}

	public void setWikiGerman(String wikiGerman) {
		this.wikiGerman = wikiGerman;
	}
}
