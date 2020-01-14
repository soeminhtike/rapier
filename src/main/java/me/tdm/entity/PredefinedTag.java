package me.tdm.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.xml.sax.Attributes;

import me.tdm.constant.Status;

@Entity
@Table(name = "predefinedtag")
public class PredefinedTag extends BaseEntity {

	@Column(name = "name")
	public String name;

	@OneToMany(mappedBy = "predefinedTag", cascade =  CascadeType.ALL)
	private List<NodeAttribute> nodeAttributeList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<NodeAttribute> getNodeAttributeList() {
		return nodeAttributeList;
	}

	public void setNodeAttributeList(List<NodeAttribute> nodeAttributeList) {
		this.nodeAttributeList = nodeAttributeList;
	}

	public static PredefinedTag create(String name, String attributes) {
		PredefinedTag tag = new PredefinedTag();
		tag.setStatus(Status.ACTIVE);
		return tag;
	}
	
	public static PredefinedTag create(String name) {
		PredefinedTag tag = new PredefinedTag();
		tag.setStatus(Status.ACTIVE);
		tag.setName(name);
		tag.setNodeAttributeList(new ArrayList<>());
		return tag;
	}
	
	public boolean isValidAttribute(Attributes attributes) {
		if (getNodeAttributeList().isEmpty())
			return true;

		for (NodeAttribute nodeAttribute : getNodeAttributeList()) {
			for (int i = 0; i < attributes.getLength(); i++) {
				String name = attributes.getLocalName(i);
				String value = attributes.getValue(i);
				if (nodeAttribute.getName().equals("*")) {
					if (value.contains(nodeAttribute.getValue()))
						return true;
				} else if (nodeAttribute.getName().equals(name)) {
					if (nodeAttribute.getValue().equals("*"))
						return true;
					if (nodeAttribute.getValue().contains(value))
						return true;
				}

			}
		}
		return false;
	}
	
	public String toString() {
		return String.format("%s %s", name, nodeAttributeList);
	}

}
