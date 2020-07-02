package me.tdm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.json.simple.JSONObject;

@Entity
@Table(name = "filler")
public class Filler extends BaseEntity {

	@Column(name = "regularExpression")
	private String regularExpression;

	public String getRegularExpression() {
		return regularExpression;
	}

	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}

	public static Filler create(JSONObject json) {
		Filler filler = new Filler();
		filler.regularExpression = (String) json.get("regular-expression");
		return filler;
	}

}
