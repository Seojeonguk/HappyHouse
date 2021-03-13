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
	String code = request.getParameter("code");
	String dong = request.getParameter("dong");
	String name = request.getParameter("name");

	String[] arr = {"202001","202002","202003","202004","202005","202006","202007","202008","202009","202010","202011","202012","202101"};
	int length = 1;
	if(!name.equals("")) {
		length = arr.length;
	}
	
	for(int i=0;i<length;i++) {
		StringBuilder urlBuilder = new StringBuilder("http://openapi.molit.go.kr:8081/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcRHTrade"); /*URL*/
		urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLDecoder.decode("c%2FQ%2FWGhWs5NTNfSpvWGw7Xqp55J7XYR8YqwNORhLoY99x1twg%2FLGAaN8VK9WKsZCEcEclHkMKftaMwzMlNajWA%3D%3D", "UTF-8")); /*Service Key*/
		urlBuilder.append("&" + URLEncoder.encode("LAWD_CD","UTF-8") + "=" + URLEncoder.encode(code.substring(0,5), "UTF-8")); /*지역코드*/
		urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
		urlBuilder.append("&" + URLEncoder.encode("DEAL_YMD","UTF-8") + "=" + URLEncoder.encode(arr[i], "UTF-8")); /*계약월*/
		String url = urlBuilder.toString();
		//out.write(url); 
	
		DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
		Document doc = dBuilder.parse(url);
	
		
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("item");
		
		for(int temp = 0; temp < nList.getLength(); temp++){		
			Node nNode = nList.item(temp);
			if(nNode.getNodeType() == Node.ELEMENT_NODE){
								
				Element eElement = (Element) nNode;
				String str = getTagValue("법정동",eElement).replaceAll(" ","");
				
				if(str.equals(dong)) {
					if(length==1 || (length!=1 && getTagValue("연립다세대", eElement).equals(name))) {
						out.write(getTagValue("지번", eElement) + "|");
						out.write(getTagValue("거래금액", eElement).replaceAll(" ", "") + "|");
						out.write(getTagValue("전용면적", eElement) + "|");
						out.write(getTagValue("연립다세대", eElement) + "|");
						out.write(getTagValue("년", eElement) + "|");
						out.write(getTagValue("월", eElement) + "|");
						out.write(getTagValue("일", eElement) + "|");
						out.write("\n");
					}
				}
			}	
		} 
	}
%>