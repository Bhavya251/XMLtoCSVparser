package com.xmlparser.xmlparser;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/xml")
public class xmlController {

    @GetMapping
    public ResponseEntity<String> getXML() {
        return new ResponseEntity<>("API is working", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> xmlFileUpload(@RequestParam("file") MultipartFile[] file,
                                   RedirectAttributes redirectAttributes) {

        if(file.length > 1) {
            return new ResponseEntity<>("Only 1 file allowed", HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
        }

        System.out.println(FilenameUtils.getExtension(file[0].getOriginalFilename()));

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file[0].getOriginalFilename() + "!");

        String responceData = parseXML(file[0]);

        if (responceData.contains("Invalid")){
            return new ResponseEntity<>(responceData, HttpStatus.NOT_ACCEPTABLE);
        } else if (responceData.contains("No")) {
            return new ResponseEntity<>(responceData, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("File uploaded\n" + responceData + "\n", HttpStatus.OK);
    }

    public String parseXML(MultipartFile file){
        try {
            Document document = checkXMLDocumentFromFile(file);

            if (document != null){
                //Here comes the root node
                Element root = document.getDocumentElement();
                System.out.println(root.getNodeName());

                //Get all elements
                NodeList nodeList = document.getElementsByTagName("AdminContractId");

                //Get elements' values
                System.out.println(nodeList.getLength());
                if(nodeList.getLength() > 0){
                    List<String> contractIds = new ArrayList<>();

                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);
                        //System.out.println("'"+ node.getTextContent() + "'");
                        contractIds.add("'"+ node.getTextContent() + "'");
                    }

                    String csvData = contractIds.stream().collect(Collectors.joining(","));

                    return csvData;
                }
                else {
                    return "No element found with tag 'AdminContractId'";
                }
            }
            else {
                return "Invalid XML file";
            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return e.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Document checkXMLDocumentFromFile(MultipartFile file) throws Exception {
        try {
            //Get Document Builder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Convert MultipartFile to File
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(convFile);

            //Build Document
            Document document = builder.parse(convFile);

            //Normalize the XML Structure; It's just too important !!
            document.getDocumentElement().normalize();

            return document;
        }catch (ParserConfigurationException | IOException | SAXException e){
            e.printStackTrace();
            return null;
        }
    }
}
