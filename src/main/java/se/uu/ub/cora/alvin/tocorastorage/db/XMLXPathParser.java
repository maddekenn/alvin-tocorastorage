package se.uu.ub.cora.alvin.tocorastorage.db;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import se.uu.ub.cora.alvin.tocorastorage.ParseException;

public final class XMLXPathParser {
	private Document document;
	private XPath xpath;

	private XMLXPathParser(String xml)
			throws ParserConfigurationException, SAXException, IOException {
		document = createDocumentFromXML(xml);
		setupXPath();
	}

	public static XMLXPathParser forXML(String xml) {
		try {
			return new XMLXPathParser(xml);
		} catch (Exception e) {
			throw ParseException.withMessageAndException("Can not read xml: " + e.getMessage(), e);
		}
	}

	private void setupXPath() {
		XPathFactory xpathFactory = XPathFactory.newInstance();
		xpath = xpathFactory.newXPath();
	}

	public Document createDocumentFromXML(String xml)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder dBuilder = createDocumentBuilder();
		return readXMLUsingBuilderAndXML(dBuilder, xml);
	}

	private DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		ErrorHandler errorHandlerWithoutSystemOutPrinting = new DefaultHandler();
		dBuilder.setErrorHandler(errorHandlerWithoutSystemOutPrinting);
		return dBuilder;
	}

	private Document readXMLUsingBuilderAndXML(DocumentBuilder dBuilder, String xml)
			throws SAXException, IOException {
		StringReader reader = new StringReader(xml);
		InputSource in = new InputSource(reader);
		Document doc = dBuilder.parse(in);
		doc.getDocumentElement().normalize();
		return doc;
	}

	public String getStringFromDocumentUsingXPath(String xpathString) {
		try {
			XPathExpression expr;
			expr = xpath.compile(xpathString);
			return (String) expr.evaluate(document, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			throw ParseException
					.withMessageAndException("Unable to use xpathString: " + e.getMessage(), e);
		}

	}

	public NodeList getNodeListFromDocumentUsingXPath(String xpathString) {
		try {
			XPathExpression expr = xpath.compile(xpathString);
			return (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw ParseException
					.withMessageAndException("Unable to use xpathString: " + e.getMessage(), e);
		}
	}

}