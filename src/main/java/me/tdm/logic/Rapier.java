package me.tdm.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;
import javax.xml.parsers.SAXParser;

import org.apache.log4j.Logger;
import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import me.tdm.dao.EntityService;
import me.tdm.entity.Node;
import me.tdm.entity.NodeAttribute;
import me.tdm.entity.PredefinedTag;
import me.tdm.entity.RapierRule;

@Service
@Transactional
public class Rapier {

	private static Logger logger = Logger.getLogger(Rapier.class);

	@Value("${target-directory}")
	private String path;

	@Autowired
	private EntityService prefinedTagDao;

	public List<PredefinedTag> extractPreProcessingRule(File file) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		List<PredefinedTag> predefinedTagList = new ArrayList<>();
		while ((line = reader.readLine()) != null) {
			String[] data = line.split(",");
			if (data.length <= 0)
				continue;
			PredefinedTag predefinedTag = PredefinedTag.create(data[0]);
			for (int i = 1; i < data.length; i++) {
				NodeAttribute attribute = new NodeAttribute();
				String[] attr = data[i].split(":");
				attribute.setName(attr[0]);
				attribute.setValue(attr[1]);
				attribute.setPredefinedTag(predefinedTag);
				predefinedTag.getNodeAttributeList().add(attribute);
			}
			predefinedTagList.add(predefinedTag);
		}
		reader.close();
		return predefinedTagList;
	}

	public String preprocessing(File file) throws Exception {
		SAXParser parser = SAXParserImpl.newInstance(null);
		TagExtractor extractor = new TagExtractor(prefinedTagDao.getAllPredefinedTag());
		parser.parse(file, extractor);
		List<Node> nodeList = extractor.getNodes();
		StringBuffer buffer = new StringBuffer();
		nodeList.forEach(node -> buffer.append(node.text + "\n"));
		return buffer.toString();
	}

	public String applyRegex(String text, List<RapierRule> rapierRuleList) throws Exception {
		StringBuffer buffer = new StringBuffer();
		for (RapierRule rapierRule : rapierRuleList) {
			Pattern pattern = Pattern.compile(rapierRule.getRegex());
			Matcher matcher = pattern.matcher(text);
			while (matcher.find()) {
				for (String group : rapierRule.getGroups().split(",")) {
					buffer.append(matcher.group(Integer.parseInt(group)));
				}
			}
		}
		return buffer.toString() + "\n";
	}

}
