package com.barelaws.barelaws_api.controller;

import com.barelaws.barelaws_api.service.OCRServices;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping()
public class OCRController {

    private final OCRServices ocrServices;

    public OCRController(OCRServices ocrServices) {
        this.ocrServices = ocrServices;
    }

    // ** Acts to text service -- Status: Working.
    @CrossOrigin
    @PostMapping("/ocr/v2/download-and-read-file-content")
    public ResponseEntity<Map<String, String>> readFileContent(@RequestParam("file_url") String fileURL) {

        // TODO: Folder location is specific to env, change for prod.
        Map<String, String> downloadFile = ocrServices.downloadFile(fileURL, System.getProperty("user.home"));

        Map<String, String> ocrResults = ocrServices.performOCR(downloadFile.get("filepath"));
        return ResponseEntity.ok(ocrResults);
    }

    // ** Locally downloaded acts to text service -- Status: Not in use.
    @CrossOrigin
    @PostMapping("/ocr/v2/read-file-content")
    public ResponseEntity<Map<String, String>> readFileContentOnly(@RequestParam("file_url") String fileURL) {

        Map<String, String> ocrResults = ocrServices.performOCR(fileURL);
        return ResponseEntity.ok(ocrResults);
    }

    // ** Acts service -- Status: Fully Ready.
    @CrossOrigin
    @GetMapping("/acts-10-min")
    public ResponseEntity<List<HashMap<String, String>>>  listFiles() {
        List<HashMap<String, String>> ocrResults = OCRServices.getActsJSON();
        return ResponseEntity.ok(ocrResults);
    }
}
