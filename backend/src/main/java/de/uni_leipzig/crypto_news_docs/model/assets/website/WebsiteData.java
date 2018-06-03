package de.uni_leipzig.crypto_news_docs.model.assets.website;

import de.uni_leipzig.crypto_news_docs.model.AbstractIdEntity;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "website_data")
public class WebsiteData extends AbstractIdEntity {
  
  @Column(nullable=false, columnDefinition="varchar(10000)")
  String source;
  
  @Column(nullable=false)
  Date date;

  @Column(nullable=false, columnDefinition="varchar(10000)")
  String title;
  
  @Column(nullable=true, columnDefinition="varchar(10000)")
  String body;
  
  @OneToOne(cascade = CascadeType.ALL)
  //@JoinColumn(name = "studio_id")
  MetaInfo metaInfo;
  
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "crypto_currency_news_id")
  CryptoCurrencyNews cryptoCurrency;
  
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }
  
  public CryptoCurrencyNews getCurrency() {
    return cryptoCurrency;
  }

  public void setCurrency(CryptoCurrencyNews cryptoCurrency) {
    this.cryptoCurrency = cryptoCurrency;
  }

  public MetaInfo getMetaInfo() {
    return metaInfo;
  }

  public void setMetaInfo(MetaInfo metaInfo) {
    this.metaInfo = metaInfo;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }


  
}