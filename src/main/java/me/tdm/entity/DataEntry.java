package me.tdm.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "dataentry")
public class DataEntry extends BaseEntity {

	@Column(name = "location")
	private String location;
	
	@Column(name="internalName")
	private String internalName;
	
	@Column(name="extractedPath")
	private String extractedPath;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name="dataentry_rule", joinColumns = { @JoinColumn(name = "dataEntryId")}, inverseJoinColumns = { @JoinColumn(name="ruleId")})
	private List<Rule> ruleList;

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

	public String getExtractedPath() {
		return extractedPath;
	}

	public void setExtractedPath(String extractedPath) {
		this.extractedPath = extractedPath;
	}
	
	public String getDisplayName() {
		String[] temp = getInternalName().split("_");
		return temp.length > 1 ? temp[1] : temp[0];
	}

	public List<Rule> getRuleList() {
		return ruleList;
	}

	public void setRuleList(List<Rule> ruleList) {
		this.ruleList = ruleList;
	}
}
