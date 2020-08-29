package me.tdm.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import me.tdm.entity.DataEntry;

@Service
public class Utilities {

	private static Logger logger = Logger.getLogger(Utilities.class);

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

	public static File loadFileFrom(DataEntry entry) {
		return new File(entry.getLocation());
	}

	public static JSONObject toJson(MultipartFile multipartFile) {
		try {
			return toJSon(multipartFile.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new JSONObject();
	}

	public static JSONObject toJSon(InputStream inputStream) {
		JSONParser parser = new JSONParser();
		try {
			return (JSONObject) parser.parse(new InputStreamReader(inputStream));
		} catch (Exception e) {
			logger.error("Can't parse json ", e);
		}
		return new JSONObject();
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
