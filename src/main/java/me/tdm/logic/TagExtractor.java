package me.tdm.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import me.tdm.entity.Node;
import me.tdm.entity.Attribute;
import me.tdm.entity.Tag;

public class TagExtractor extends DefaultHandler {

	private static Logger logger = Logger.getLogger(TagExtractor.class);

	private Map<String, Tag> allowedTags;

	private List<Node> nodeList;

	private Node node;

	private Stack<String> hierarchy = new Stack<>();

	private Stack<String> ignoreTag = new Stack<>();

	public TagExtractor(List<Tag> tagList) {
		allowedTags = new HashMap<>();
		this.nodeList = new ArrayList<>();
		tagList.forEach(tag -> {
			logger.info(tag);
			allowedTags.put(tag.name, tag);
		});
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		logger.info("----------------------------------------------------------------" + localName);
		if (isValidNode(localName) && node == null) {
			logger.info("valid tag name by node");
			if (!allowedTags.get(localName).isValidAttribute(attributes)) {
				logger.info("invalid attribute ");
				for(int i=0;i<attributes.getLength();i++) {
					logger.info(attributes.getLocalName(i)+"="+attributes.getValue(i));
				}
				logger.info("--------------------------------------------------------------");
				return;
			}
			hierarchy = new Stack<>();
			node = new Node(); // PredefinedTag.create(localName, "");
			node.name = localName;
			hierarchy.push(localName);
			nodeList.add(node);
			node.text += String.format("<%s %s>", localName, extractAttributes(attributes, localName));
			return;
		}
		Tag tag = allowedTags.get("*");
		if (tag != null && tag.isValidAttribute(attributes) && node == null) {
			logger.info("valid by attribute");
			hierarchy = new Stack<>();
			node = new Node(); // PredefinedTag.create(localName, "");
			node.name = localName;
			hierarchy.push(localName);
			nodeList.add(node);
			node.text += String.format("<%s %s>", localName, extractAttributes(attributes, localName));
			return;
		}
		if (node != null) {
			if (isIgnoreNode(localName) || !ignoreTag.empty()) {
				ignoreTag.add(localName);
				logger.info("--------------------------------------------------------------");
				return;
			}
			hierarchy.push(localName);
			node.text += String.format("<%s %s>", localName, extractAttributes(attributes, localName));
			return;
		}
		logger.info(">>>>>>> invalid <<<<<<");

		logger.info("--------------------------------------------------------------");
	}

	private String extractAttributes(Attributes attributes, String localName) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < attributes.getLength(); i++) {
			String attrName = attributes.getLocalName(i);
			buffer.append(String.format("%s='%s' ", attrName, attributes.getValue(attrName)));
		}
		return buffer.toString();
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (node == null || !ignoreTag.empty())
			return;

		String str = String.valueOf(ch).substring(0, length).trim();
		if (str.length() <= 0)
			return;
		node.text += str;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (node == null)
			return;

		if (!ignoreTag.empty()) {
			ignoreTag.pop();
			return;
		}

		hierarchy.pop();
		node.text += String.format("</%s>", localName);

		if (hierarchy.empty()) {
			node = null;
		}
	}

	public List<Node> getNodes() {
		return nodeList;
	}

	private boolean isValidNode(String name) {
		return allowedTags.containsKey(name);
	}

	

//	private boolean isValidAtttribute(Attributes attributes) {
//		int length = attributes.getLength();
//		if (length <= 0)
//			return false;
//		PredefinedTag tag = allowedTags.get("*");
//		if (tag == null)
//			return false;
//		for (NodeAttribute nodeAttribute : tag.getNodeAttributeList()) {
//			for (int i = 0; i < length; i++) {
//				String name = attributes.getLocalName(i);
//				String values = attributes.getValue(i);
//				if (name.equals("*")) {
//					if (values.contains(nodeAttribute.getValue()))
//						return true;
//				} else if (name.equals(nodeAttribute.getName())) {
//					if (nodeAttribute.getValue().equals("*"))
//						return true;
//					if (nodeAttribute.getValue().contains(values))
//						return true;
//				}
//			}
//		}
//
//		return false;
//	}

	private boolean isIgnoreNode(String name) {
		return name.equals("script") || name.equals("noscript") || name.equals("img");
	}

}
