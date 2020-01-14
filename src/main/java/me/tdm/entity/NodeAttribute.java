package me.tdm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.xml.sax.Attributes;

@Entity
@Table(name="attribute")
public class NodeAttribute extends BaseEntity {

	@Column(name="name")
	private String name;
	
	@Column(name="value")
	private String value;
	
	@JoinColumn(name="predefinedtagId")
	@OneToOne(fetch = FetchType.EAGER)
	private PredefinedTag predefinedTag;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public PredefinedTag getPredefinedTag() {
		return predefinedTag;
	}

	public void setPredefinedTag(PredefinedTag predefinedTag) {
		this.predefinedTag = predefinedTag;
	}
	
	public String toString() {
		return String.format("%s=%s", name, value);
	}
	
}
