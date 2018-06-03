package de.uni_leipzig.crypto_news_docs.dto.website;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Date;

public class TimestampDto {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  Date timestamp;

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }


}