package com.barelaws.barelaws_api.controller;

import com.barelaws.barelaws_api.entity.ActEntity;
import com.barelaws.barelaws_api.service.OCRServices;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
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
    @PostMapping("/ocr/download-and-read-file-content")
    public ResponseEntity<Map<String, String>> readFileContent(@RequestParam("file_url") String fileURL) {

        // TODO: Folder location is specific to env, change for prod.
        Map<String, String> downloadFile = ocrServices.downloadFile(fileURL, System.getProperty("user.home"));

        Map<String, String> ocrResults = ocrServices.performOCR(downloadFile.get("filepath"));
        return ResponseEntity.ok(ocrResults);
    }

    // ** List of acts -- Status: Under testing.
    @CrossOrigin
    @GetMapping("/act-services/acts-list")
    public ResponseEntity<Iterable<ActEntity>> listActs() {
        return ResponseEntity.ok(ocrServices.getActsData());
    }

    // ** Locally downloaded acts to Text service -- Status: Under-testing.
    @CrossOrigin
    @PostMapping("/act-services/pdf-path-to-text")
    public ResponseEntity<Map<String, String>> readFileContentOnly(@RequestParam("file_path") String fileURL) {

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

    /* 1A. Act Services
    * Upload act to multipart documents (as HTML) saved in vector/full-text engine
    * Update act to multipart documents (as HTML) saved in vector/full-text engine
    * Delete act data
    * Auto upload and update act to multipart documents (as HTML) saved in vector/full-text engine
    */

    /* 1B. Case Law Services
     * Upload act to multipart documents saved in vector/full-text engine
     * Update act to multipart documents saved in vector/full-text engine
     * Delete case law
     * Auto upload and update act saved in vector/full-text engine
     */

    /* 1. Bare law services
     * Act service updated
     * Case law service updated
     * RAG interpretation of each line and word of all statutes
     * Auto update barelaws when either act or case law changes
     */
}
