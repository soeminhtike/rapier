package me.tdm.helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class Utitlities {

	private static Logger logger = Logger.getLogger(Utitlities.class);

	public static Iterable<String> fileIteratable(InputStream inputStream) {

		BufferedReader reader = null;

		Iterable<String> iterable = new Iterable<String>() {

			@Override
			public Iterator<String> iterator() {
				return new FileIterator(reader);
			}

		};

		return iterable;
	}

	public static JSONObject toJson(MultipartFile multipartFile) {
		JSONObject json = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			return (JSONObject) parser.parse(new InputStreamReader(multipartFile.getInputStream()));
		} catch (Exception e) {
			logger.error("Can't parse json ", e);
		}
		return json;
	}

	private static class FileIterator implements Iterator<String> {
		String line = null;
		String buffer = null;
		private BufferedReader reader;

		FileIterator(BufferedReader reader) {
			this.reader = reader;
			try {
				line = reader.readLine();
			} catch (Exception e) {
				logger.error("Can't reader line :" + e);
			}
		}

		@Override
		public boolean hasNext() {
			return line != null;
		}

		@Override
		public String next() {
			buffer = line;
			nextLine();
			return buffer;
		}

		private void nextLine() {
			try {
				line = this.reader.readLine();
				if (line == null)
					reader.close();
			} catch (IOException e) {
				logger.error("Can't reader file", e);
			}
		}

	}
}
