package me.tdm.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import me.tdm.entity.Node;
import me.tdm.entity.Tag;

public class TagExtractor extends DefaultHandler {

	private static Logger logger = Logger.getLogger(TagExtractor.class);

	private Map<String, Tag> allowedTags;

	private List<Node> nodeList;

	private Node activeNode;

	private Stack<String> hierarchy = new Stack<>();

	private Stack<String> ignoreTag = new Stack<>();

	public TagExtractor(List<Tag> tagList) {
		allowedTags = new HashMap<>();
		this.nodeList = new ArrayList<>();
		tagList.forEach(tag -> {
			allowedTags.put(tag.name, tag);
		});
	}

	@Override
	public void startElement(String uri, String tagName, String qName, Attributes attributes) throws SAXException {
		if (isValidTag(tagName) && activeNode == null) {
			Tag tag = allowedTags.get(tagName);
			if (!tag.isValidAttribute(attributes)) {
				return;
			}

			hierarchy = new Stack<>();
			activeNode = new Node(); // PredefinedTag.create(localName, "");
			activeNode.name = tagName;
			hierarchy.push(tagName);
			nodeList.add(activeNode);
			logger.info("tag name :" + tag.getName());
			activeNode.text += String.format("<%s %s>", tagName, extractAttributes(attributes, tag));
			return;
		}

		Tag tag = allowedTags.get("*");
		if (tag != null && tag.isValidAttribute(attributes) && activeNode == null) {
			hierarchy = new Stack<>();
			activeNode = new Node(); // PredefinedTag.create(localName, "");
			activeNode.name = tagName;
			hierarchy.push(tagName);
			nodeList.add(activeNode);
			activeNode.text += String.format("<%s %s>", tagName, extractAttributes(attributes, tag));
			return;
		}

		if (activeNode != null)
			addInnerTag(tagName, attributes, tag);
	}

	private void addInnerTag(String tagName, Attributes attributes, Tag tag) {
		if (isIgnoreNode(tagName) || !ignoreTag.empty()) {
			ignoreTag.add(tagName);
			return;
		}
		hierarchy.push(tagName);
		activeNode.text += String.format("<%s %s>", tagName, extractAttributes(attributes, tag));
	}

	private String extractAttributes(Attributes attributes, Tag tag) {
		if (tag == null) {
			logger.info("null tag");
			return "";
		}
		Map<String, String> matchAttributes = tag.getMatchAttributes(attributes);
		StringBuffer buffer = new StringBuffer();
		matchAttributes.entrySet().stream().map(entry -> String.format("%s='%s' ", entry.getKey(), entry.getValue())).forEach(buffer::append);
		return buffer.toString();
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (activeNode == null || !ignoreTag.empty())
			return;

		String str = String.valueOf(ch).substring(0, length).trim();
		if (str.length() <= 0)
			return;
		activeNode.text += str;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (activeNode == null)
			return;

		if (!ignoreTag.empty()) {
			ignoreTag.pop();
			return;
		}

		hierarchy.pop();
		activeNode.text += String.format("</%s>", localName);

		if (hierarchy.empty()) {
			activeNode = null;
		}
	}

	public List<Node> getNodes() {
		return nodeList;
	}

	private boolean isValidTag(String name) {
		return allowedTags.containsKey(name);
	}

	private boolean isIgnoreNode(String name) {
		return name.equals("script") || name.equals("noscript") || name.equals("img");
	}

}
