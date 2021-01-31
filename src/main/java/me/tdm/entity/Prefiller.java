package me.tdm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.json.simple.JSONObject;

@Entity
@Table(name = "prefiller")
public class Prefiller extends BaseEntity {

	@Column(name = "regularExpression")
	private String regularExpression;

	public String getRegularExpression() {
		return regularExpression;
	}

	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}

	public static Prefiller create(JSONObject json) {
		Prefiller prefiller = new Prefiller();
		prefiller.regularExpression = (String) json.get("regular-expression");
		return prefiller;
	}

}
