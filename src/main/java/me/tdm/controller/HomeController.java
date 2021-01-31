package me.tdm.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import me.tdm.constant.OperationStatus;
import me.tdm.dao.EntityService;
import me.tdm.entity.DataEntry;
import me.tdm.entity.ExtractedData;
import me.tdm.entity.Rule;
import me.tdm.helper.Utilities;
import me.tdm.logic.DataEntryService;
import me.tdm.logic.Rapier;

@Controller
public class HomeController {
	
	private static Logger logger = Logger.getLogger(HomeController.class);

	@Value("${preprocessing-file.name}")
	private String preprocessingFileName;

	@Value("${target-directory}")
	private String path;

	@Value("${extracted-directory}")
	private String extractedDirectory;

	@Autowired
	private Rapier rapier;

	@Autowired
	private DataEntryService dataEntryService;

	@Autowired
	private EntityService entityService;

	@RequestMapping(value = "/")
	public String home() {
		return "index";
	}

	@ResponseBody
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String upload(@RequestParam("file") MultipartFile multipartFile) throws FileNotFoundException, IOException {
		String temporaryFileName = System.nanoTime() + ".html";
		File file = new File(path + temporaryFileName);
		IOUtils.copy(multipartFile.getInputStream(), new FileOutputStream(file));
		DataEntry dataEntry = dataEntryService.create(file, multipartFile.getOriginalFilename());
		dataEntryService.save(dataEntry);
		return dataEntry.getInternalName();
	}

	@ResponseBody
	@RequestMapping(value = "/remove-tag/{name}", method = RequestMethod.POST)
	public void removeTag(@PathVariable("name") String name, HttpServletResponse response) throws Exception {
		DataEntry entry = dataEntryService.findByName(name);
		String processedString = rapier.preprocessing(new File(entry.getLocation()));
		IOUtils.copy(toInputStream(processedString), response.getOutputStream());
	}

	@ResponseBody
	@RequestMapping(value = "store-file/origin-content/{name}", method = RequestMethod.GET)
	public void getOriginalContent(@PathVariable("name") String name, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		DataEntry dataEntry = dataEntryService.findByName(name);
		IOUtils.copy(new FileInputStream(new File(dataEntry.getLocation())), response.getOutputStream());
	}

	@ResponseBody
	@RequestMapping(value = "store-file/extracted-content/{name}", method = RequestMethod.GET)
	public void getExtractedContent(@PathVariable("name") String name, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		DataEntry dataEntry = dataEntryService.findByName(name);
		if (dataEntry.getExtractedPath() == null)
			IOUtils.copy(new ByteArrayInputStream("".getBytes()), response.getOutputStream());
		else
			IOUtils.copy(new FileInputStream(new File(dataEntry.getExtractedPath())), response.getOutputStream());
	}

	@ResponseBody
	@RequestMapping(value = "store/{name}", method = RequestMethod.POST)
	public String storeExtraction(@PathVariable("name") String name) throws Exception {
		DataEntry entry = dataEntryService.findByName(name);
		String filePath = extractedDirectory + "/" + name;
		String processedString = rapier.preprocessing(new File(entry.getLocation()));
		IOUtils.copy(toInputStream(processedString), new FileOutputStream(new File(filePath)));
		entry.setExtractedPath(filePath);
		dataEntryService.addRule(entry);
		dataEntryService.save(entry);
		return OperationStatus.SUCCESS.name();
	}

	@RequestMapping(value = "stored-files", method = RequestMethod.GET)
	public String getStoreFiles(Model model) {
		List<DataEntry> dataEntryList = dataEntryService.getAll();
		model.addAttribute("dataEntries", dataEntryList);
		return "data-entry-list";
	}

	@RequestMapping(value = "/preprocessing-rule", method = RequestMethod.POST)
	public String uploadPreprocessionJsonRule(@RequestParam("file") MultipartFile multipartFile) {
		JSONObject json = Utilities.toJson(multipartFile);
		Rule rule = Rule.create(json);
		entityService.save(rule);
		return OperationStatus.SUCCESS.name();
	}

	@ResponseBody
	@RequestMapping(value = "extract/{name}", method = RequestMethod.POST)
	public ExtractedData extractFile(@PathVariable("name") String name, HttpServletResponse response) throws Exception {
		DataEntry entry = dataEntryService.findByName(name);
		//FileOutputStream outputStream = new FileOutputStream(new File(entry.getLocation()));
		// rapier.extract(entry);
		//String processedString = "";
		// IOUtils.copy(toInputStream(processedString), response.getOutputStream());
		return rapier.extract(entry);
	}

	private InputStream toInputStream(String str) {
		return new ByteArrayInputStream(str.getBytes());
	}
}
