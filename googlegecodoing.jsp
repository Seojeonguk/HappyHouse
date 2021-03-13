<%@page import="org.w3c.dom.*"%>
<%@page import="javax.xml.parsers.DocumentBuilder"%>
<%@page import="javax.xml.parsers.DocumentBuilderFactory"%>
<%@page import="java.io.*"%>
<%@page import="java.net.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%!
public String getTagValue(String tag, Element eElement) {
    NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
    Node nValue = (Node) nlList.item(0);
    if(nValue == null) 
        return null;
    return nValue.getNodeValue();
}
%>

<%
	request.setCharacterEncoding("UTF-8");
	String cp = request.getContextPath();
	String si = request.getParameter("si");
	String gun = request.getParameter("gun");
	String gu = request.getParameter("gu");
	String item = request.getParameter("item");

	StringBuilder urlBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/geocode/xml"); /*URL*/
	urlBuilder.append("?" + URLEncoder.encode("address","UTF-8") + "=" + si + "+" + gun + "+" + gu + "+" + item); /*Address*/
	urlBuilder.append("&" + URLEncoder.encode("key","UTF-8") + "=" + URLDecoder.decode("AIzaSyC6Us_KXXGQzpkFbsSJmCWtHOTuR-4mM4M", "UTF-8")); /*Service Key*/
	urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=1000");
	

	String url = urlBuilder.toString();
	//out.write(url);

	DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
	Document doc = dBuilder.parse(url);

	
	doc.getDocumentElement().normalize();
	
	NodeList nList = doc.getElementsByTagName("location");
	
	for(int temp = 0; temp < nList.getLength(); temp++){		
		Node nNode = nList.item(temp);
		if(nNode.getNodeType() == Node.ELEMENT_NODE){
							
			Element eElement = (Element) nNode;
			out.write(getTagValue("lat", eElement) + "|");
			out.write(getTagValue("lng", eElement).replaceAll(" ", "") + "|");
			out.write("\n");
		}	
	}
%>