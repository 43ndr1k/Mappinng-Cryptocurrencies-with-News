package de.uni_leipzig.crypto_news_docs.model.assets.website;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import de.uni_leipzig.crypto_news_docs.model.AbstractIdEntity;

@Entity
@Table(name = "CryptoCurrencyNews")
public class CryptoCurrencyNews extends AbstractIdEntity {
  
  @Column(nullable=false)
  String currency;

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

}
