package me.tdm.logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

@Service
@Transactional
public class Rapier {

	private static Logger logger = Logger.getLogger(Rapier.class);

	@Value("${target-directory}")
	private String path;
	
	@Value("${tag.remove}")
	private List<String> tagToRemove; 

	@Autowired
	private EntityService entityService;
	
	private static final Pattern matcher = Pattern.compile("(?s)<!--.*?-->");

	public ExtractedData extract(DataEntry dataEntry) {
		// File inputFile = Utilities.loadFileFrom(dataEntry);
		String text = "";
		Path path = Paths.get(dataEntry.getExtractedPath());
		try {
			text = Files.lines(path).collect(Collectors.joining());
		} catch (IOException e) {
		}
		Map<String, Rule> rules = entityService.getAllRapierRule().stream().collect(Collectors.toMap(Rule::getName, rule -> rule));
		String expression = rules.get("publisher").buildFirstLevelRule();
		//expression ="<dd.*?</dd>";
		logger.info("regular expresion :" + expression);
		logger.info("Input text :" + text);
		Pattern p = Pattern.compile(expression);
		Matcher matcher = p.matcher(text);
		ExtractedData extractedData =  ExtractedData.create();
		while(matcher.find()) {
			//String data =  matcher.group(1);
			//Matcher inner = Pattern.compile(rules.get("publisher").buildSecondLevelRule()).matcher(data);
			//logger.info("second level rule :" + rules.get("publisher").buildSecondLevelRule());
			//while(inner.find()) {
			extractedData.setPublication(matcher.group(1));
			//}
		}
		return extractedData;
	}

	public List<Elements> extractPreProcessingRule(File file) throws Exception {
		Document document = Jsoup.parse(file, "UTF-8");
		List<Rule> ruleList = entityService.getAllRapierRule();
		
		return applyPrefiller(ruleList, document);

	}

	public List<Elements> applyPrefiller(List<Rule> ruleList, Document document) {
		List<Elements> resultList = new ArrayList<>();
		//ruleList.forEach(rule -> applyPrefiller(rule, document, resultList));
		logger.info("prefiller result : " + resultList.size());

		return resultList;
	}

	public String preprocessing(File file) throws Exception {
		StringBuffer buffer = new StringBuffer();
		// extractPreProcessingRule(file).stream().forEach(element -> buffer.append(element.outerHtml()));
		Document document = Jsoup.parse(file, "UTF-8");
		tagToRemove.stream().forEach(tag -> removeEelmentByTag(tag, document));
		document.getAllElements().forEach(element -> buffer.append(element.outerHtml()));
		return removeComment(buffer.toString());
	}

	
	private void removeEelmentByTag(String selector, Document document) {
		document.select(selector).forEach(element -> element.remove());
	}
	
	private String removeComment(String text) {
		matcher.matcher(text);
		return matcher.matcher(text).replaceAll("");
	}
	public String applyRegex(String text, List<Rule> rapierRuleList) throws Exception {
		StringBuffer buffer = new StringBuffer();
		
		return buffer.toString() + "\n";
	}

}
