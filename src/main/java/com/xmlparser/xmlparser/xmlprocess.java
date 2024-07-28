package com.xmlparser.xmlparser;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class xmlprocess {
    public static void parser() throws Exception {

        Document document = checkXMLDocumentFromFile("src\\main\\resources\\xmlfiles\\before.xml");

        //Here comes the root node
        Element root = document.getDocumentElement();
        System.out.println(root.getNodeName());

        //Get all elements
        NodeList nodeList = document.getElementsByTagName("AdminContractId");

        //Get element value
        //String value = element.getTextContent();
        //System.out.println(nodeList.getLength());

        List<String> contractIds = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            //System.out.println("'"+ node.getTextContent() + "'");
            contractIds.add("'"+ node.getTextContent() + "'");
        }

        csvWriter(contractIds);
    }

    public static Document checkXMLDocumentFromFile(String fileNameWithPath) throws Exception {

        //Get Document Builder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        //Build Document
        Document document = builder.parse(new File(fileNameWithPath));

        //Normalize the XML Structure; It's just too important !!
        document.getDocumentElement().normalize();

        return document;
    }

    public static void csvWriter(List<String> contractIds) {
        String collect = contractIds.stream().collect(Collectors.joining(","));
        try (FileWriter writer = new FileWriter("src\\main\\resources\\csvfiles\\test.csv")) {
            writer.write(collect);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
