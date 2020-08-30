package me.tdm.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import me.tdm.dao.EntityService;
import me.tdm.entity.DataEntry;
import me.tdm.entity.ExtractedData;
import me.tdm.entity.Rule;
import me.tdm.helper.Utilities;

@Service
@Transactional
public class Rapier {

	private static Logger logger = Logger.getLogger(Rapier.class);

	@Value("${target-directory}")
	private String path;

	@Autowired
	private EntityService entityService;

	public ExtractedData extract(DataEntry dataEntry) {
		File inputFile = Utilities.loadFileFrom(dataEntry);
		if (!inputFile.exists())
			return ExtractedData.create();

		return null;
	}

	public List<Elements> extractPreProcessingRule(File file) throws Exception {
		Document document = Jsoup.parse(file, "UTF-8");
		List<Rule> ruleList = entityService.getAllRapierRule();

		return applyPrefiller(ruleList, document);

	}

	public List<Elements> applyPrefiller(List<Rule> ruleList, Document document) {
		List<Elements> resultList = new ArrayList<>();
		ruleList.forEach(rule -> applyPrefiller(rule, document, resultList));
		logger.info("prefiller result : " + resultList.size());

		return resultList;
	}

	private void applyPrefiller(Rule rule, Document document, List<Elements> resultList) {
		Stream.of(rule.getPrefiller().getCssRule()).map(cssSelector -> {
			return document.select(cssSelector);
		}).filter(element -> element != null && !element.isEmpty()).forEach(data -> {
			resultList.add(data);
		});
	}

	public String preprocessing(File file) throws Exception {
		StringBuffer buffer = new StringBuffer();
		extractPreProcessingRule(file).stream().forEach(element -> buffer.append(element.outerHtml()));
		return buffer.toString();
	}

	public String applyRegex(String text, List<Rule> rapierRuleList) throws Exception {
		StringBuffer buffer = new StringBuffer();

		return buffer.toString() + "\n";
	}

}
