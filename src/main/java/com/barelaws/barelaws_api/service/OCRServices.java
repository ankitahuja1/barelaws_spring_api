package com.barelaws.barelaws_api.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class OCRServices {

    public Map<String, String> performOCR3(String filePath) {

        final Map<String, String> response = new HashMap<>();


        final File inputFile = new File(filePath);

        try {
            if (filePath.toLowerCase().endsWith(".pdf")) {
                return pdfToText(filePath);
            } else if (filePath.toLowerCase().endsWith(".doc") || filePath.toLowerCase().endsWith(".docx")) {
                return docToText(inputFile);
            } else {
                response.put("status", "0");
                response.put("msg", "Unsupported format. Allowed formats are .png, .jpg, .pdf, .doc, .docx");
                return response;
            }
        } catch (Exception e) {
            response.put("status", "0");
            response.put("msg", e.getMessage());
            return response;
        }
    }


    public Map<String, String> pdfToText(String filePath) throws Exception {
        final Map<String, String> response = new HashMap<>();

        PDDocument document = PDDocument.load(new File(filePath)); // Load the PDF document

        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < document.getNumberOfPages(); i++) {
            pdfTextStripper.setStartPage(i + 1);
            pdfTextStripper.setEndPage(i + 1);
            String text = pdfTextStripper.getText(document); // Extract text from each page
            result.append(text);
        }

        document.close(); // Close the document after text extraction

        // Process the extracted text as needed
        // (you can perform OCR-related tasks here if required)

        response.put("status", "1");
        response.put("msg", "Success");
        response.put("results", result.toString());
        return response;
    }

    public static Map<String, String> docToText(File docFile) throws Exception {

        final Map<String, String> response = new HashMap<>();

        FileInputStream fis = new FileInputStream(docFile);

        XWPFDocument file = new XWPFDocument(OPCPackage.open(fis));
        XWPFWordExtractor ext = new XWPFWordExtractor(file);

        response.put("status", "1");
        response.put("msg", "success");
        response.put("results", ext.getText());

        return response;
    }

    public Map<String, String> performOCR(String filePath) {
        final OCRServices ocrServices = new OCRServices();
        return ocrServices.performOCR3(filePath);
    }

    public String getFileNameWithoutQueryParams(String fileURL) {
        try {
            URL url = new URL(fileURL);

            String path = url.getPath();

            // Extract the file name from the path
            String fileName = path.substring(path.lastIndexOf('/') + 1);

            // Remove query parameters if present
            if (fileName.contains("?")) {
                fileName = fileName.substring(0, fileName.indexOf('?'));
            }

            return fileName;
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, String> downloadFile(String downloadURL, String downloadPath) {

        final Map<String, String> response = new HashMap<>();
        final String filename = getFileNameWithoutQueryParams(downloadURL);

        if (filename == null) {
            response.put("status", "0");
            response.put("msg", "Unable to get file name from URL: " + downloadURL);
            return response;
        }


        try {
            URL url = new URL(downloadURL);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();

            // Check if the response code is OK (200)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpConn.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(downloadPath + "/" + filename);

                int bytesRead;
                byte[] buffer = new byte[4096];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                response.put("status", "1");
                response.put("msg", "Success");
                response.put("filepath", downloadPath + "/" + filename);

            } else {
                response.put("status", "0");
                response.put("msg", "Failed to download file: Response Code = " + responseCode);
            }

            httpConn.disconnect();
            return response;
        } catch (IOException e) {
            response.put("status", "0");
            response.put("msg", "Failed to download file: " + e.getMessage());
            return response;
        }

    }

    static Integer count = 1;
    public static Document getHTMLFromURL(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder htmlContent = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    htmlContent.append(line);
                }

                reader.close();

                count++;
                System.out.println("Done: " + count);

                return Jsoup.parse(htmlContent.toString());

            } else {
                throw new Error("Invalid URL - Response Code: " + responseCode);
            }
        } catch (IOException e) {
            throw new Error("Invalid URL - " + e.getMessage());
        }
    }

    private static Elements getIndiaCodeDownloadLinks(Elements elements){

        Elements pdfLinksElements = new Elements();

        for(Element element : elements) {
            Document linksHTML = getHTMLFromURL("https://www.indiacode.nic.in/"+element.attr("href"));

            pdfLinksElements.add(linksHTML.select("#content > div.container-fluid > div > div > div.row > a").first());

        }
        return pdfLinksElements;
    }


    public static List<HashMap<String, String>> getActsJSON() {

        List<HashMap<String, String>> results = new ArrayList<>();

        Document IndiaCode = getHTMLFromURL("https://www.indiacode.nic.in/handle/123456789/1362/browse?nccharset=3995E3B3&type=shorttitle&sort_by=1&order=DESC&rpp=5000&submit_browse=Update");

        Elements IndiaCodeRows = IndiaCode.select("#content table tbody tr:not(:first-child)");

        for(Element IndiaCodeRow : IndiaCodeRows) {
            HashMap<String, String> row = new HashMap<>();

            Elements IndiaCodeActDate = IndiaCodeRow.select("td:nth-child(1)");
            Elements IndiaCodeActNumb = IndiaCodeRow.select("td:nth-child(2) em");
            Elements IndiaCodeActName = IndiaCodeRow.select("td:nth-child(3) strong");
            Elements IndiaCodeActLink = getIndiaCodeDownloadLinks(IndiaCodeRow.select("td:nth-child(4) > a[href]"));

            row.put("enactmentDate", IndiaCodeActDate.text());
            row.put("actNumber", IndiaCodeActNumb.text());
            row.put("actName", IndiaCodeActName.text());
            row.put("actLink", IndiaCodeActLink.attr("href"));

            results.add(row);
            System.out.println(results);
        }

         return results;
    }


}

