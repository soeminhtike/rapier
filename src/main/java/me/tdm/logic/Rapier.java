package me.tdm.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.xml.parsers.SAXParser;

import org.apache.log4j.Logger;
import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import me.tdm.dao.EntityService;
import me.tdm.entity.Attribute;
import me.tdm.entity.Node;
import me.tdm.entity.Rule;
import me.tdm.entity.Tag;

@Service
@Transactional
public class Rapier {

	private static Logger logger = Logger.getLogger(Rapier.class);

	@Value("${target-directory}")
	private String path;

	@Autowired
	private EntityService prefinedTagDao;

	public List<Tag> extractPreProcessingRule(File file) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		List<Tag> predefinedTagList = new ArrayList<>();
		while ((line = reader.readLine()) != null) {
			String[] data = line.split(",");
			if (data.length <= 0)
				continue;
			Tag predefinedTag = Tag.create(data[0]);
			for (int i = 1; i < data.length; i++) {
				Attribute attribute = new Attribute();
				String[] attr = data[i].split(":");
				attribute.setName(attr[0]);
				attribute.setValue(attr[1]);
				attribute.setTag(predefinedTag);
				predefinedTag.getNodeAttributeList().add(attribute);
			}
			predefinedTagList.add(predefinedTag);
		}
		reader.close();
		return predefinedTagList;
	}

	public String preprocessing(InputStream file) throws Exception {
		SAXParser parser = SAXParserImpl.newInstance(null);
		TagExtractor extractor = new TagExtractor(prefinedTagDao.getAllPredefinedTag());
		parser.parse(file, extractor);
		List<Node> nodeList = extractor.getNodes();
		StringBuffer buffer = new StringBuffer();
		nodeList.forEach(node -> buffer.append(node.text + "\n"));
		return buffer.toString();
	}
	
	public String preprocessing(File file) throws Exception {
		logger.info(" path :" + file.getAbsolutePath());
		SAXParser parser = SAXParserImpl.newInstance(null);
		TagExtractor extractor = new TagExtractor(prefinedTagDao.getAllPredefinedTag());
		parser.parse(file, extractor);
		List<Node> nodeList = extractor.getNodes();
		StringBuffer buffer = new StringBuffer();
		nodeList.forEach(node -> buffer.append(node.text + "\n"));
		return buffer.toString();
	}

	public String applyRegex(String text, List<Rule> rapierRuleList) throws Exception {
		StringBuffer buffer = new StringBuffer();
		
		return buffer.toString() + "\n";
	}

}
