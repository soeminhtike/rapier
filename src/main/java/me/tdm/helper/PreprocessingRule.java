package me.tdm.helper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PreprocessingRule {
	
	public List<String> loadPredefineRule() {
		List<String> tags= new ArrayList<>();
		tags.add("author");
		tags.add("title");
		
		return tags;
	}

}
