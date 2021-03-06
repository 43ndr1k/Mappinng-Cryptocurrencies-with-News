package de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto;

import de.uni_leipzig.crypto_news_docs.model.AbstractIdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "crypto_currency_description")
public class CryptoCurrencyDescription extends AbstractIdEntity {


	@Column(unique = true, nullable = false)
	private String shortName;


	private String longName;

	@Column(columnDefinition="varchar(1000)")
	private String wikiEnglish;

	@Column(columnDefinition="varchar(1000)")
	private String wikiGerman;

	public CryptoCurrencyDescription() {
	}

	public CryptoCurrencyDescription(String shortName, String longName, String wikiEnglish, String wikiGerman) {
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
