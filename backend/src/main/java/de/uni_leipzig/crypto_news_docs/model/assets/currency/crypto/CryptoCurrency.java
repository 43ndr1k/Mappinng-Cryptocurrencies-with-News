package de.uni_leipzig.crypto_news_docs.model.assets.currency.crypto;


import de.uni_leipzig.crypto_news_docs.model.AbstractIdEntity;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "crypto_currencies")
public class CryptoCurrency extends AbstractIdEntity {


	@Column(nullable = false)
	private String shortName;

	@OneToOne(cascade = CascadeType.ALL)
	//@JoinColumn(name = "crypto_currency_discription_id")
	private CryptoCurrencyDescription description;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "crypto_currency_id")
	private List<CryptoCurrencyPrice> cryptoCurrencyPrices;

/*	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "cryptoCurrencyExchange_Id", nullable = false)
	private CryptoCurrencyExchange cryptoCurrencyExchange;*/

	public CryptoCurrency() {

	}

	public CryptoCurrency(String shortName,
			List<CryptoCurrencyPrice> cryptoCurrencyPrices, CryptoCurrencyDescription description) {
		this.shortName = shortName;
		this.cryptoCurrencyPrices = cryptoCurrencyPrices;
		this.description = description;
	}

	public List<CryptoCurrencyPrice> getCryptoCurrencyPrices() {
		return cryptoCurrencyPrices;
	}

	public void setCryptoCurrencyPrices(
			List<CryptoCurrencyPrice> cryptoCurrencyPrices) {
		this.cryptoCurrencyPrices = cryptoCurrencyPrices;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public CryptoCurrencyDescription getDescription() {
		return description;
	}

	public void setDescription(
			CryptoCurrencyDescription description) {
		this.description = description;
	}
}
