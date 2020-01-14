package me.tdm.operationTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import me.tdm.config.TestConfig;
import me.tdm.dao.EntityService;
import me.tdm.entity.PredefinedTag;
import me.tdm.logic.Rapier;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class PredefinedExtractTest {

	private static Logger logger = Logger.getLogger(PredefinedExtractTest.class);

	@Value("${target-directory}")
	private String path;
	
	@Autowired
	private Rapier rapier;
	
	@Autowired
	private EntityService entityService;

	@Test
	public void databaseCreateTest() {
		logger.info("created database");
	}
	
	@Test
	@Ignore
	public void predefinedRuleExtractTest() throws Exception {
		List<PredefinedTag> list = rapier.extractPreProcessingRule(new File(path+"predefinedRule.pre"));
		for(PredefinedTag tag : list) {
			logger.info(tag);
			entityService.save(tag);
		}
	}

	@Test
	@Ignore
	public void test1() throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(new File(path + "/result.rpi")));
		StringBuffer buffer = new StringBuffer();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		reader.close();
		Pattern pattern = Pattern.compile("<author>(.+?)</author>", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(buffer.toString());
		while (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();
			logger.info(String.format("%d - %d => %s", start, end, matcher.group(1)));
		}
	}
}
