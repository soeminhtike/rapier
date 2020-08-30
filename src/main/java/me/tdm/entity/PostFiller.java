package me.tdm.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="postfiller")
public class PostFiller extends BaseEntity {
	
	private String regularExpression;

	public String getRegularExpression() {
		return regularExpression;
	}

	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}
	
}
