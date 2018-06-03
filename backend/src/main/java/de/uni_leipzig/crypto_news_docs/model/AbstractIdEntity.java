package de.uni_leipzig.crypto_news_docs.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractIdEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	public AbstractIdEntity() {

		// empty constructor for jpa
	}

	public long getId() {

		return this.id;
	}

	public void setId(long id) {

		this.id = id;
	}
}
