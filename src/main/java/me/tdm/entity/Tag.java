package me.tdm.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.xml.sax.Attributes;

import me.tdm.constant.Status;

@Entity
@Table(name = "tag")
public class Tag extends BaseEntity {

	@ManyToOne
	private Prefiller prefiller;

	@Column(name = "name")
	public String name;

	@OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
	private List<Attribute> nodeAttributeList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Attribute> getNodeAttributeList() {
		return nodeAttributeList;
	}

	public void setNodeAttributeList(List<Attribute> nodeAttributeList) {
		this.nodeAttributeList = nodeAttributeList;
	}

	public Prefiller getPrefiller() {
		return prefiller;
	}

	public void setPrefiller(Prefiller prefiller) {
		this.prefiller = prefiller;
	}

	public static Tag create(String name, String attributes) {
		Tag tag = new Tag();
		tag.setStatus(Status.ACTIVE);
		return tag;
	}

	public static Tag create(String name) {
		Tag tag = new Tag();
		tag.setStatus(Status.ACTIVE);
		tag.setName(name);
		tag.setNodeAttributeList(new ArrayList<>());
		return tag;
	}

	@SuppressWarnings("unchecked")
	public static Tag create(JSONObject json) {
		Tag tag = create((String) json.get("name"));
		JSONArray attributes = (JSONArray) json.get("attribute");
		attributes.forEach(obj -> {
			JSONObject attributeJson = (JSONObject) obj;
			Set<Entry<Object, Object>> attributeIterator = attributeJson.entrySet();
			for (Entry<Object, Object> entry : attributeIterator) {
				Attribute attribute = new Attribute();
				attribute.setName((String) entry.getKey());
				attribute.setValue((String) entry.getValue());
				attribute.setTag(tag);
				tag.nodeAttributeList.add(attribute);
			}

		});
		return tag;
	}

	public boolean isValidAttribute(Attributes attributes) {
		if (getNodeAttributeList().isEmpty())
			return true;

		for (Attribute nodeAttribute : getNodeAttributeList()) {
			for (int i = 0; i < attributes.getLength(); i++) {
				if (nodeAttribute.isMatch(attributes.getLocalName(i), attributes.getValue(i)))
					return true;
			}
		}
		return false;
	}

	public Map<String, String> getMatchAttributes(Attributes attributes) {
		Map<String, String> map = new HashMap<>();
		for (Attribute nodeAttribute : getNodeAttributeList()) {
			for (int i = 0; i < attributes.getLength(); i++) {
				String name = attributes.getLocalName(i);
				String value = attributes.getValue(i);
				if (!nodeAttribute.isMatch(name, value))
					continue;
				map.put(name, value);
			}
		}
		return map;
	}

	public String toString() {
		return String.format("%s %s", name, nodeAttributeList);
	}

}
