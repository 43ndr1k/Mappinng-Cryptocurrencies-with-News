package de.uni_leipzig.crypto_news_docs.model.assets.currency.country;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;

class CompositeId implements Serializable {

	private String shortName;
	private Double value;
	private Date date;
}
