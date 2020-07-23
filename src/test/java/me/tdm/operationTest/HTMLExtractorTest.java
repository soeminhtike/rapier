package me.tdm.operationTest;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import me.tdm.config.TestConfig;
import me.tdm.entity.DataEntry;
import me.tdm.logic.DataEntryService;
import me.tdm.logic.Rapier;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class HTMLExtractorTest {

	private static Logger logger = Logger.getLogger(HTMLExtractorTest.class);

	@Autowired
	private DataEntryService dataEntryService;

	@Autowired
	private Rapier rapier;

	@Test
	public void extract() throws Exception {
		DataEntry entry = dataEntryService.findByName("207320868169400");
		String processedString = rapier.preprocessing(new File(entry.getLocation()));
		logger.info(processedString);
	}
}
