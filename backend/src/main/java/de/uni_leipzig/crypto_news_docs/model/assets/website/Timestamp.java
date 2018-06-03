package de.uni_leipzig.crypto_news_docs.model.assets.website;

import java.sql.Date;
import de.uni_leipzig.crypto_news_docs.model.AbstractIdEntity;
import javax.persistence.Entity;
import javax.persistence.Table;

//@Entity
//@Table(name = "timestamp")
public class Timestamp {//extends AbstractIdEntity  {
  
  Date timestamp;

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date date) {
    this.timestamp = date;
  }

//  Date date;
//
//  public Date getDate() {
//    return date;
//  }
//
//  public void setDate(Date date) {
//    this.date = date;
//  }

}