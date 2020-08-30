package me.tdm.operationTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import me.tdm.config.TestConfig;
import me.tdm.dao.EntityService;
import me.tdm.entity.Rule;
import me.tdm.helper.Utilities;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class RuleLoader {

	@Value("${target-directory}")
	private String basePath;

	@Autowired
	private EntityService entityService;

	private static Logger logger = Logger.getLogger(RuleLoader.class);

	@Test
	public void loadRule() throws FileNotFoundException {
		entityService.save(Rule.create(Utilities.toJSon(toInputStream("author.json"))));
		entityService.save(Rule.create(Utilities.toJSon(toInputStream("isbn.json"))));
		entityService.save(Rule.create(Utilities.toJSon(toInputStream("paperback.json"))));
		entityService.save(Rule.create(Utilities.toJSon(toInputStream("price.json"))));
		entityService.save(Rule.create(Utilities.toJSon(toInputStream("title.json"))));
		entityService.save(Rule.create(Utilities.toJSon(toInputStream("publication.json"))));
		entityService.save(Rule.create(Utilities.toJSon(toInputStream("publisher.json"))));
		entityService.save(Rule.create(Utilities.toJSon(toInputStream("edition.json"))));
	}

	private InputStream toInputStream(String name) {
		try {
			return new FileInputStream(new File(basePath + name));
		} catch (FileNotFoundException e) {
			logger.error("Can't load file", e);
			return null;
		}
	}

}
