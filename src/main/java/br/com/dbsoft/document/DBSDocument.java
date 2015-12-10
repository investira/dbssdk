package br.com.dbsoft.document;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;
import org.xml.sax.SAXException;

import br.com.dbsoft.core.DBSSDK;

public class DBSDocument implements Document {
	protected Logger	wLogger = Logger.getLogger(this.getClass());
	
	private DocumentBuilderFactory 	wDocFactory = DocumentBuilderFactory.newInstance();
	private DocumentBuilder 		wDocBuilder;
	private Document 				wDoc;
	
	public DBSDocument() {
		try {
			//Desabilita validações padrão para que a Parse seja executado mais rápido
			wDocFactory.setNamespaceAware(false);
			wDocFactory.setValidating(false);
			wDocFactory.setFeature("http://xml.org/sax/features/namespaces", false);
			wDocFactory.setFeature("http://xml.org/sax/features/validation", false);
			wDocFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
			wDocFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			//------------------------
			wDocBuilder = wDocFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			wLogger.error(e);
		} 
	}
	
	public boolean read(File pFile){
		try {
			wDoc = wDocBuilder.parse(pFile);
			return true;
		} catch (SAXException | IOException e) {
			wLogger.error(e);
			return false;
		}
	}
	
	/**
	 * Procura por um nó recursivamente que inicie com o valor do atributo informado
	 * @param pNodes
	 * @param pAttributeName
	 * @param pAttributeValue
	 * @return
	 */
	public Node findNode(NodeList pNodes, String pAttributeName, String pAttributeValue){
		return DBSSDK.NodeListFindNode(pNodes, pAttributeName, pAttributeValue);
	}

	@Override
	public String getNodeName() {
		return wDoc.getNodeName();
	}

	@Override
	public String getNodeValue() throws DOMException {
		return wDoc.getNodeValue();
	}

	@Override
	public void setNodeValue(String pNodeValue) throws DOMException {
		wDoc.setNodeValue(pNodeValue);
	}

	@Override
	public short getNodeType() {
		return wDoc.getNodeType();
	}

	@Override
	public Node getParentNode() {
		return wDoc.getParentNode();
	}

	@Override
	public NodeList getChildNodes() {
		return wDoc.getChildNodes();
	}

	@Override
	public Node getFirstChild() {
		return wDoc.getFirstChild();
	}

	@Override
	public Node getLastChild() {
		return wDoc.getLastChild();
	}

	@Override
	public Node getPreviousSibling() {
		return wDoc.getPreviousSibling();
	}

	@Override
	public Node getNextSibling() {
		return wDoc.getNextSibling();
	}

	@Override
	public NamedNodeMap getAttributes() {
		return wDoc.getAttributes();
	}

	@Override
	public Document getOwnerDocument() {
		return wDoc.getOwnerDocument();
	}

	@Override
	public Node insertBefore(Node pNewChild, Node pRefChild) throws DOMException {
		return wDoc.insertBefore(pNewChild, pRefChild);
	}

	@Override
	public Node replaceChild(Node pNewChild, Node pOldChild) throws DOMException {
		return wDoc.replaceChild(pNewChild, pOldChild);
	}

	@Override
	public Node removeChild(Node pOldChild) throws DOMException {
		return wDoc.removeChild(pOldChild);
	}

	@Override
	public Node appendChild(Node pNewChild) throws DOMException {
		return wDoc.appendChild(pNewChild);
	}

	@Override
	public boolean hasChildNodes() {
		return wDoc.hasChildNodes();
	}

	@Override
	public Node cloneNode(boolean pDeep) {
		return wDoc.cloneNode(pDeep);
	}

	@Override
	public void normalize() {
		wDoc.normalize();
	}

	@Override
	public boolean isSupported(String pFeature, String pVersion) {
		return wDoc.isSupported(pFeature, pVersion);
	}

	@Override
	public String getNamespaceURI() {
		return wDoc.getNamespaceURI();
	}

	@Override
	public String getPrefix() {
		return wDoc.getPrefix();
	}

	@Override
	public void setPrefix(String pPrefix) throws DOMException {
		wDoc.setPrefix(pPrefix);
	}

	@Override
	public String getLocalName() {
		return wDoc.getLocalName();
	}

	@Override
	public boolean hasAttributes() {
		return wDoc.hasAttributes();
	}

	@Override
	public String getBaseURI() {
		return wDoc.getBaseURI();
	}

	@Override
	public short compareDocumentPosition(Node pOther) throws DOMException {
		return wDoc.compareDocumentPosition(pOther);
	}

	@Override
	public String getTextContent() throws DOMException {
		return wDoc.getTextContent();
	}

	@Override
	public void setTextContent(String pTextContent) throws DOMException {
		wDoc.setTextContent(pTextContent);
	}

	@Override
	public boolean isSameNode(Node pOther) {
		return wDoc.isSameNode(pOther);
	}

	@Override
	public String lookupPrefix(String pNamespaceURI) {
		return wDoc.lookupPrefix(pNamespaceURI);
	}

	@Override
	public boolean isDefaultNamespace(String pNamespaceURI) {
		return wDoc.isDefaultNamespace(pNamespaceURI);
	}

	@Override
	public String lookupNamespaceURI(String pPrefix) {
		return wDoc.lookupNamespaceURI(pPrefix);
	}

	@Override
	public boolean isEqualNode(Node pArg) {
		return wDoc.isEqualNode(pArg);
	}

	@Override
	public Object getFeature(String pFeature, String pVersion) {
		return wDoc.getFeature(pFeature, pVersion);
	}

	@Override
	public Object setUserData(String pKey, Object pData, UserDataHandler pHandler) {
		return wDoc.setUserData(pKey, pData, pHandler);
	}

	@Override
	public Object getUserData(String pKey) {
		return wDoc.getUserData(pKey);
	}

	@Override
	public DocumentType getDoctype() {
		return wDoc.getDoctype();
	}

	@Override
	public DOMImplementation getImplementation() {
		return wDoc.getImplementation();
	}

	@Override
	public Element getDocumentElement() {
		return wDoc.getDocumentElement();
	}

	@Override
	public Element createElement(String pTagName) throws DOMException {
		return wDoc.createElement(pTagName);
	}

	@Override
	public DocumentFragment createDocumentFragment() {
		return wDoc.createDocumentFragment();
	}

	@Override
	public Text createTextNode(String pData) {
		return wDoc.createTextNode(pData);
	}

	@Override
	public Comment createComment(String pData) {
		return wDoc.createComment(pData);
	}

	@Override
	public CDATASection createCDATASection(String pData) throws DOMException {
		return wDoc.createCDATASection(pData);
	}

	@Override
	public ProcessingInstruction createProcessingInstruction(String pTarget, String pData) throws DOMException {
		return wDoc.createProcessingInstruction(pTarget, pData);
	}

	@Override
	public Attr createAttribute(String pName) throws DOMException {
		return wDoc.createAttribute(pName);
	}

	@Override
	public EntityReference createEntityReference(String pName) throws DOMException {
		return wDoc.createEntityReference(pName);
	}

	@Override
	public NodeList getElementsByTagName(String pTagname) {
		return wDoc.getElementsByTagName(pTagname);
	}

	@Override
	public Node importNode(Node pImportedNode, boolean pDeep) throws DOMException {
		return wDoc.importNode(pImportedNode, pDeep);
	}

	@Override
	public Element createElementNS(String pNamespaceURI, String pQualifiedName) throws DOMException {
		return wDoc.createElementNS(pNamespaceURI, pQualifiedName);
	}

	@Override
	public Attr createAttributeNS(String pNamespaceURI, String pQualifiedName) throws DOMException {
		return wDoc.createAttributeNS(pNamespaceURI, pQualifiedName);
	}

	@Override
	public NodeList getElementsByTagNameNS(String pNamespaceURI, String pLocalName) {
		return wDoc.getElementsByTagNameNS(pNamespaceURI, pLocalName);
	}

	@Override
	public Element getElementById(String pElementId) {
		return wDoc.getElementById(pElementId);
	}

	@Override
	public String getInputEncoding() {
		return wDoc.getInputEncoding();
	}

	@Override
	public String getXmlEncoding() {
		return wDoc.getXmlEncoding();
	}

	@Override
	public boolean getXmlStandalone() {
		return wDoc.getXmlStandalone();
	}

	@Override
	public void setXmlStandalone(boolean pXmlStandalone) throws DOMException {
		wDoc.setXmlStandalone(pXmlStandalone);
	}

	@Override
	public String getXmlVersion() {
		return wDoc.getXmlVersion();
	}

	@Override
	public void setXmlVersion(String pXmlVersion) throws DOMException {
		wDoc.setXmlVersion(pXmlVersion);
	}

	@Override
	public boolean getStrictErrorChecking() {
		return wDoc.getStrictErrorChecking();
	}

	@Override
	public void setStrictErrorChecking(boolean pStrictErrorChecking) {
		wDoc.setStrictErrorChecking(pStrictErrorChecking);
	}

	@Override
	public String getDocumentURI() {
		return wDoc.getDocumentURI();
	}

	@Override
	public void setDocumentURI(String pDocumentURI) {
		wDoc.setDocumentURI(pDocumentURI);
	}

	@Override
	public Node adoptNode(Node pSource) throws DOMException {
		return wDoc.adoptNode(pSource);
	}

	@Override
	public DOMConfiguration getDomConfig() {
		return wDoc.getDomConfig();
	}

	@Override
	public void normalizeDocument() {
		wDoc.normalizeDocument();
	}

	@Override
	public Node renameNode(Node pN, String pNamespaceURI, String pQualifiedName) throws DOMException {
		return wDoc.renameNode(pN, pNamespaceURI, pQualifiedName);
	}

}
