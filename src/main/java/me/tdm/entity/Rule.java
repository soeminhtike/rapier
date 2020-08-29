package me.tdm.entity;

import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

@Entity
@Table(name = "rule")
public class Rule extends BaseEntity {
	
	private static Logger logger = Logger.getLogger(Rule.class);

	@OneToOne(cascade = CascadeType.ALL)
	private Filler filler;

	@OneToOne(cascade = CascadeType.ALL)
	private Prefiller prefiller;

	@OneToOne(cascade = CascadeType.ALL)
	private PostFiller postFiller;
	
	@Column(name = "name")
	private String name;

	public Filler getFiller() {
		return filler;
	}

	public void setFiller(Filler filler) {
		this.filler = filler;
	}

	public Prefiller getPrefiller() {
		return prefiller;
	}

	public void setPrefiller(Prefiller prefiller) {
		this.prefiller = prefiller;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static Rule create(JSONObject json) {
		logger.info("json :" + json);
		Prefiller prefiller = Prefiller.create((JSONObject) json.get("prefiller"));
		Filler filler = Filler.create((JSONObject) json.get("filler"));
		return Rule.create(filler, prefiller, (String) json.get("name"));
	}

	public static Rule create(Filler filler, Prefiller prefiller, String name) {
		Rule rule = new Rule();
		rule.filler = filler;
		rule.prefiller = prefiller;
		rule.name = name;
		rule.postFiller = new PostFiller();
		return rule;
	}

}
