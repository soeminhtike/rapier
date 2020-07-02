package me.tdm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="attribute")
public class Attribute extends BaseEntity {

	@Column(name="name")
	private String name;
	
	@Column(name="value")
	private String value;
	
	@JoinColumn(name="tagId")
	@OneToOne(fetch = FetchType.EAGER)
	private Tag tag;

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

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag predefinedTag) {
		this.tag = predefinedTag;
	}
	
	public String toString() {
		return String.format("%s=%s", name, value);
	}
	
}
