package me.tdm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "dataentry")
public class DataEntry extends BaseEntity {

	@Column(name = "location")
	private String location;
	
	@Column(name="internalName")
	private String internalName;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getInternalName() {
		return internalName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}
	
	
}