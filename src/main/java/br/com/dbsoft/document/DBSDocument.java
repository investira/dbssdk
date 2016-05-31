package br.com.dbsoft.document;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;

public class DBSDocument extends Document {
	

	protected Logger	wLogger = Logger.getLogger(this.getClass());
	private Document 	wDoc;

	public DBSDocument() {
		super("/");
	}

	public boolean read(File pFile){
		try {
			wDoc = Jsoup.parse(pFile, "UTF-8");
			return true;
		} catch (IOException e) {
			wLogger.error(e);
			return false;
		}
	}
	
	public boolean read(URL pUrl){
		try {
			wDoc = Jsoup.parse(pUrl, 3000);
			return true;
		} catch (IOException e) {
			wLogger.error(e);
			return false;
		}
	}
	
	@Override
	public String location() {
		return wDoc.location();
	}

	@Override
	public Element head() {
		return wDoc.head();
	}

	@Override
	public Element body() {
		return wDoc.body();
	}

	@Override
	public String title() {
		return wDoc.title();
	}

	@Override
	public void title(String title) {
		wDoc.title(title);
	}

	@Override
	public Element createElement(String tagName) {
		return wDoc.createElement(tagName);
	}

	@Override
	public Document normalise() {
		return wDoc.normalise();
	}

	@Override
	public String outerHtml() {
		return wDoc.outerHtml();
	}

	@Override
	public Element text(String text) {
		return wDoc.text(text);
	}

	@Override
	public String nodeName() {
		return wDoc.nodeName();
	}

	@Override
	public void charset(Charset charset) {
		wDoc.charset(charset);
	}

	@Override
	public Charset charset() {
		return wDoc.charset();
	}

	@Override
	public void updateMetaCharsetElement(boolean update) {
		wDoc.updateMetaCharsetElement(update);
	}

	@Override
	public boolean updateMetaCharsetElement() {
		return wDoc.updateMetaCharsetElement();
	}

	@Override
	public Document clone() {
		return wDoc.clone();
	}

	@Override
	public OutputSettings outputSettings() {
		return wDoc.outputSettings();
	}

	@Override
	public Document outputSettings(OutputSettings outputSettings) {
		return wDoc.outputSettings(outputSettings);
	}

	@Override
	public QuirksMode quirksMode() {
		return wDoc.quirksMode();
	}

	@Override
	public Document quirksMode(QuirksMode quirksMode) {
		return wDoc.quirksMode(quirksMode);
	}

	@Override
	public String tagName() {
		return wDoc.tagName();
	}

	@Override
	public Element tagName(String tagName) {
		return wDoc.tagName(tagName);
	}

	@Override
	public Tag tag() {
		return wDoc.tag();
	}

	@Override
	public boolean isBlock() {
		return wDoc.isBlock();
	}

	@Override
	public String id() {
		return wDoc.id();
	}

	@Override
	public Element attr(String attributeKey, String attributeValue) {
		return wDoc.attr(attributeKey, attributeValue);
	}

	@Override
	public Element attr(String attributeKey, boolean attributeValue) {
		return wDoc.attr(attributeKey, attributeValue);
	}

	@Override
	public Map<String, String> dataset() {
		return wDoc.dataset();
	}

	@Override
	public Elements parents() {
		return wDoc.parents();
	}

	@Override
	public Element child(int index) {
		return wDoc.child(index);
	}

	@Override
	public Elements children() {
		return wDoc.children();
	}

	@Override
	public List<TextNode> textNodes() {
		return wDoc.textNodes();
	}

	@Override
	public List<DataNode> dataNodes() {
		return wDoc.dataNodes();
	}

	@Override
	public Elements select(String cssQuery) {
		return wDoc.select(cssQuery);
	}

	@Override
	public Element appendChild(Node child) {
		return wDoc.appendChild(child);
	}

	@Override
	public Element prependChild(Node child) {
		return wDoc.prependChild(child);
	}

	@Override
	public Element insertChildren(int index, Collection<? extends Node> children) {
		return wDoc.insertChildren(index, children);
	}

	@Override
	public Element appendElement(String tagName) {
		return wDoc.appendElement(tagName);
	}

	@Override
	public Element prependElement(String tagName) {
		return wDoc.prependElement(tagName);
	}

	@Override
	public Element appendText(String text) {
		return wDoc.appendText(text);
	}

	@Override
	public Element prependText(String text) {
		return wDoc.prependText(text);
	}

	@Override
	public Element append(String html) {
		return wDoc.append(html);
	}

	@Override
	public Element prepend(String html) {
		return wDoc.prepend(html);
	}

	@Override
	public Element before(String html) {
		return wDoc.before(html);
	}

	@Override
	public Element before(Node node) {
		return wDoc.before(node);
	}

	@Override
	public Element after(String html) {
		return wDoc.after(html);
	}

	@Override
	public Element after(Node node) {
		return wDoc.after(node);
	}

	@Override
	public Element empty() {
		return wDoc.empty();
	}

	@Override
	public Element wrap(String html) {
		return wDoc.wrap(html);
	}

	@Override
	public String cssSelector() {
		return wDoc.cssSelector();
	}

	@Override
	public Elements siblingElements() {
		return wDoc.siblingElements();
	}

	@Override
	public Element nextElementSibling() {
		return wDoc.nextElementSibling();
	}

	@Override
	public Element previousElementSibling() {
		return wDoc.previousElementSibling();
	}

	@Override
	public Element firstElementSibling() {
		return wDoc.firstElementSibling();
	}

	@Override
	public Integer elementSiblingIndex() {
		return wDoc.elementSiblingIndex();
	}

	@Override
	public Element lastElementSibling() {
		return wDoc.lastElementSibling();
	}

	@Override
	public Elements getElementsByTag(String tagName) {
		return wDoc.getElementsByTag(tagName);
	}

	@Override
	public Element getElementById(String id) {
		return wDoc.getElementById(id);
	}

	@Override
	public Elements getElementsByClass(String className) {
		return wDoc.getElementsByClass(className);
	}

	@Override
	public Elements getElementsByAttribute(String key) {
		return wDoc.getElementsByAttribute(key);
	}

	@Override
	public Elements getElementsByAttributeStarting(String keyPrefix) {
		return wDoc.getElementsByAttributeStarting(keyPrefix);
	}

	@Override
	public Elements getElementsByAttributeValue(String key, String value) {
		return wDoc.getElementsByAttributeValue(key, value);
	}

	@Override
	public Elements getElementsByAttributeValueNot(String key, String value) {
		return wDoc.getElementsByAttributeValueNot(key, value);
	}

	@Override
	public Elements getElementsByAttributeValueStarting(String key, String valuePrefix) {
		return wDoc.getElementsByAttributeValueStarting(key, valuePrefix);
	}

	@Override
	public Elements getElementsByAttributeValueEnding(String key, String valueSuffix) {
		return wDoc.getElementsByAttributeValueEnding(key, valueSuffix);
	}

	@Override
	public Elements getElementsByAttributeValueContaining(String key, String match) {
		return wDoc.getElementsByAttributeValueContaining(key, match);
	}

	@Override
	public Elements getElementsByAttributeValueMatching(String key, Pattern pattern) {
		return wDoc.getElementsByAttributeValueMatching(key, pattern);
	}

	@Override
	public Elements getElementsByAttributeValueMatching(String key, String regex) {
		return wDoc.getElementsByAttributeValueMatching(key, regex);
	}

	@Override
	public Elements getElementsByIndexLessThan(int index) {
		return wDoc.getElementsByIndexLessThan(index);
	}

	@Override
	public Elements getElementsByIndexGreaterThan(int index) {
		return wDoc.getElementsByIndexGreaterThan(index);
	}

	@Override
	public Elements getElementsByIndexEquals(int index) {
		return wDoc.getElementsByIndexEquals(index);
	}

	@Override
	public Elements getElementsContainingText(String searchText) {
		return wDoc.getElementsContainingText(searchText);
	}

	@Override
	public Elements getElementsContainingOwnText(String searchText) {
		return wDoc.getElementsContainingOwnText(searchText);
	}

	@Override
	public Elements getElementsMatchingText(Pattern pattern) {
		return wDoc.getElementsMatchingText(pattern);
	}

	@Override
	public Elements getElementsMatchingText(String regex) {
		return wDoc.getElementsMatchingText(regex);
	}

	@Override
	public Elements getElementsMatchingOwnText(Pattern pattern) {
		return wDoc.getElementsMatchingOwnText(pattern);
	}

	@Override
	public Elements getElementsMatchingOwnText(String regex) {
		return wDoc.getElementsMatchingOwnText(regex);
	}

	@Override
	public Elements getAllElements() {
		return wDoc.getAllElements();
	}

	@Override
	public String text() {
		return wDoc.text();
	}

	@Override
	public String ownText() {
		return wDoc.ownText();
	}

	@Override
	public boolean hasText() {
		return wDoc.hasText();
	}

	@Override
	public String data() {
		return wDoc.data();
	}

	@Override
	public String className() {
		return wDoc.className();
	}

	@Override
	public Set<String> classNames() {
		return wDoc.classNames();
	}

	@Override
	public Element classNames(Set<String> classNames) {
		return wDoc.classNames(classNames);
	}

	@Override
	public boolean hasClass(String className) {
		return wDoc.hasClass(className);
	}

	@Override
	public Element addClass(String className) {
		return wDoc.addClass(className);
	}

	@Override
	public Element removeClass(String className) {
		return wDoc.removeClass(className);
	}

	@Override
	public Element toggleClass(String className) {
		return wDoc.toggleClass(className);
	}

	@Override
	public String val() {
		return wDoc.val();
	}

	@Override
	public Element val(String value) {
		return wDoc.val(value);
	}

	@Override
	public String html() {
		return wDoc.html();
	}

	@Override
	public Element html(String html) {
		return wDoc.html(html);
	}

	@Override
	public String toString() {
		return wDoc.toString();
	}

	@Override
	public boolean equals(Object o) {
		return wDoc.equals(o);
	}

	@Override
	public int hashCode() {
		return wDoc.hashCode();
	}

	@Override
	public String attr(String attributeKey) {
		return wDoc.attr(attributeKey);
	}

	@Override
	public Attributes attributes() {
		return wDoc.attributes();
	}

	@Override
	public boolean hasAttr(String attributeKey) {
		return wDoc.hasAttr(attributeKey);
	}

	@Override
	public Node removeAttr(String attributeKey) {
		return wDoc.removeAttr(attributeKey);
	}

	@Override
	public String baseUri() {
		return wDoc.baseUri();
	}

	@Override
	public void setBaseUri(String baseUri) {
		wDoc.setBaseUri(baseUri);
	}

	@Override
	public String absUrl(String attributeKey) {
		return wDoc.absUrl(attributeKey);
	}

	@Override
	public Node childNode(int index) {
		return wDoc.childNode(index);
	}

	@Override
	public List<Node> childNodes() {
		return wDoc.childNodes();
	}

	@Override
	public List<Node> childNodesCopy() {
		return wDoc.childNodesCopy();
	}

	@Override
	public Document ownerDocument() {
		return wDoc.ownerDocument();
	}

	@Override
	public void remove() {
		wDoc.remove();
	}

	@Override
	public Node unwrap() {
		return wDoc.unwrap();
	}

	@Override
	public void replaceWith(Node in) {
		wDoc.replaceWith(in);
	}

	@Override
	public List<Node> siblingNodes() {
		return wDoc.siblingNodes();
	}

	@Override
	public Node nextSibling() {
		return wDoc.nextSibling();
	}

	@Override
	public Node previousSibling() {
		return wDoc.previousSibling();
	}

	@Override
	public int siblingIndex() {
		return wDoc.siblingIndex();
	}

	@Override
	public Node traverse(NodeVisitor nodeVisitor) {
		return wDoc.traverse(nodeVisitor);
	}


}
