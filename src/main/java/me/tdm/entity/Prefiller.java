package me.tdm.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Entity
@Table(name = "prefiller")
public class Prefiller extends BaseEntity {
	
	private static Logger logger = Logger.getLogger(Prefiller.class);

	@OneToMany(mappedBy = "prefiller", cascade = CascadeType.ALL)
	private List<Tag> tagList;

	public Prefiller() {
		tagList = new ArrayList<>();
	}

	public List<Tag> getTagList() {
		return tagList;
	}

	public void setTagList(List<Tag> tagList) {
		this.tagList = tagList;
	}
	
	private static void createTagAndAddToPrefiller(Object obj, Prefiller prefiller) {
		Tag tag = Tag.create((JSONObject) obj);
		tag.setPrefiller(prefiller);
		prefiller.tagList.add(tag);
	}

	@SuppressWarnings("unchecked")
	public static Prefiller createPrefiller(JSONObject json) {
		Prefiller prefiller = new Prefiller();
		JSONArray tagJsonArray = (JSONArray) json.get("tag");
		logger.info("prefiller tag :" + tagJsonArray);
		tagJsonArray.forEach(obj -> createTagAndAddToPrefiller(obj, prefiller));
		return prefiller;
	}

}
