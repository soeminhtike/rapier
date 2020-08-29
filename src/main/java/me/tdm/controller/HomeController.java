package me.tdm.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import me.tdm.constant.OperationStatus;
import me.tdm.dao.EntityService;
import me.tdm.entity.DataEntry;
import me.tdm.entity.Rule;
import me.tdm.helper.Utilities;
import me.tdm.logic.DataEntryService;
import me.tdm.logic.Rapier;

@Controller
public class HomeController {

	@Value("${preprocessing-file.name}")
	private String preprocessingFileName;

	@Value("${target-directory}")
	private String path;

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
		DataEntry dataEntry = dataEntryService.create(file);
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

	@RequestMapping(value = "/preprocessing-rule", method = RequestMethod.POST)
	public String uploadPreprocessionJsonRule(@RequestParam("file") MultipartFile multipartFile) {
		JSONObject json = Utilities.toJson(multipartFile);
		Rule rule = Rule.create(json);
		entityService.save(rule);
		return OperationStatus.SUCCESS.name();
	}

	@ResponseBody
	@RequestMapping(value = "extract/{name}", method = RequestMethod.POST)
	public void extractFile(@PathVariable("name") String name, HttpServletResponse response) throws Exception {
		DataEntry entry = dataEntryService.findByName(name);
		FileOutputStream outputStream = new FileOutputStream(new File(entry.getLocation()));
		String processedString = "";
		IOUtils.copy(toInputStream(processedString), response.getOutputStream());
	}

	private InputStream toInputStream(String str) {
		return new ByteArrayInputStream(str.getBytes());
	}
}
