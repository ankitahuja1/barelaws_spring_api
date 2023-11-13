package com.barelaws.barelaws_api.controller;

import com.barelaws.barelaws_api.service.OCRServices;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController()
@RequestMapping("/post")
public class OCRController {

    private final OCRServices ocrServices;

    public OCRController(OCRServices ocrServices) {
        this.ocrServices = ocrServices;
    }

    @CrossOrigin
    @PostMapping("/ocr/v2/read-file-content")
    public ResponseEntity<Map<String, String>> readFileContent(@RequestParam("file_url") String fileURL) {

        // TODO: Folder location is specific to env, change for prod.
        Map<String, String> downloadFile = ocrServices.downloadFile(fileURL, "/Users/ankitahuja");

        System.out.println(downloadFile.get("filepath"));

        Map<String, String> ocrResults = ocrServices.performOCR(downloadFile.get("filepath"));
        return ResponseEntity.ok(ocrResults);
    }

    @CrossOrigin
    @PostMapping("/ocr/v2/read-file-content-only")
    public ResponseEntity<Map<String, String>> readFileContentOnly(@RequestParam("file_url") String fileURL) {

        Map<String, String> ocrResults = ocrServices.performOCR(fileURL);
        return ResponseEntity.ok(ocrResults);
    }
}
