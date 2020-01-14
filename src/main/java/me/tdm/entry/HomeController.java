package me.tdm.entry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import me.tdm.constant.OperationStatus;
import me.tdm.dao.EntityService;
import me.tdm.entity.PredefinedTag;
import me.tdm.entity.RapierRule;
import me.tdm.logic.Rapier;

@Controller
public class HomeController {

	private static Logger logger = Logger.getLogger(HomeController.class);

	@Value("${preprocessing-file.name}")
	private String preprocessingFileName;

	@Value("${target-directory}")
	private String path;

	@Autowired
	private Rapier rapier;

	@Autowired
	private EntityService entityService;

	@RequestMapping(value = "/")
	public String home() {
		return "index";
	}

	@RequestMapping(value = "/preprocessing-rule", method = RequestMethod.POST)
	public String uploadPreprocessingRule(@RequestParam("file") MultipartFile multipartFile) throws Exception {
		String temporaryFileName = System.nanoTime() + ".tmp";
		IOUtils.copy(multipartFile.getInputStream(), new FileOutputStream(new File(path + temporaryFileName)));
		File uploadFile = new File(path + temporaryFileName);
		List<PredefinedTag> extractedPredefinedTagList = rapier.extractPreProcessingRule(uploadFile);
		logger.info("total tag :" + extractedPredefinedTagList.size());
		extractedPredefinedTagList.forEach(entityService::save);
		return OperationStatus.SUCCESS.name();
	}

	@RequestMapping(value = "/rule", method = RequestMethod.POST)
	public String uploadRegex(@RequestParam("file") MultipartFile multipartFile) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));
		String ch = reader.readLine();
		while (ch != null) {
			RapierRule rule = new RapierRule();
			int index = ch.lastIndexOf(":");
			if (index == -1)
				continue;
			// String stringRule = ;
			rule.setRegex(ch.substring(0, index));
			rule.setGroups(ch.substring(index + 1));
			entityService.save(rule);
			ch = reader.readLine();
		}
		return OperationStatus.SUCCESS.name();
	}

	@ResponseBody
	@RequestMapping(value = "extract", method = RequestMethod.POST)
	public String extractFile(@RequestParam("file") MultipartFile multipartFile) throws Exception {
		String fileName = String.format("%s/%d", path, System.nanoTime());
		FileOutputStream outputStream = new FileOutputStream(new File(fileName));
		IOUtils.copy(multipartFile.getInputStream(), outputStream);
		outputStream.flush();
		outputStream.close();
		String processedString = rapier.preprocessing(new File(fileName));
		logger.info("Preprocessing :" + processedString);
		// String result = rapier.applyRegex(processedString,
		// entityService.getAllRapierRule());
		return processedString; // OperationStatus.SUCCESS.name();
	}
}
