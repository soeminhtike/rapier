package me.tdm.operationTest;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import me.tdm.config.TestConfig;
import me.tdm.dao.EntityService;
import me.tdm.entity.Rule;
import me.tdm.logic.DataEntryService;
import me.tdm.logic.Rapier;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class HTMLExtractorTest {

	private static Logger logger = Logger.getLogger(HTMLExtractorTest.class);

	@Value("${target-directory}")
	private String basePath;

	@Autowired
	private DataEntryService dataEntryService;

	@Autowired
	private EntityService entityService;

	@Autowired
	private Rapier rapier;

	@Test
	public void setup() {

	}

	@Test
	public void extract() throws Exception {
		String html = rapier.preprocessing(getFile("hello3.html"));
		logger.info("HTML :" + html);
	}

	private File getFile(String name) {
		return new File(basePath + "/" + name);
	}
}
