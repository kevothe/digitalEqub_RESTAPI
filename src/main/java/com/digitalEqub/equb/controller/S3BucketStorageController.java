package com.digitalEqub.equb.controller;

import com.digitalEqub.equb.payload.ApiResponse;
import com.digitalEqub.equb.service.S3BucketStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("/file")
public class S3BucketStorageController {
    @Autowired
    S3BucketStorageService service;

/*
    @PostMapping("/file/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("fileName") String fileName,
                                             @RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(service.uploadFile(fileName, file), HttpStatus.OK);
    }*/
@PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam ("fileName") String fileName,
                                        @RequestParam("file") MultipartFile file) {
        //String fileName = UploadRequest.getFileName();
       // MultipartFile file = UploadRequest.getFile();
        service.uploadFile(fileName, file);
            return new ResponseEntity(new ApiResponse(true, "file uploaded succesfully"),HttpStatus.OK);


}
    @GetMapping("/list/files")
    public ResponseEntity<List<String>> getListOfFiles() {

        return new ResponseEntity<>(service.listFiles(), HttpStatus.OK);
    }
    @GetMapping(value = "/download/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) {
        ByteArrayOutputStream downloadInputStream = service.downloadFile(filename);

        return ResponseEntity.ok()
                .contentType(contentType(filename))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(downloadInputStream.toByteArray());
    }


    private MediaType contentType(String filename) {
        String[] fileArrSplit = filename.split("\\.");
        String fileExtension = fileArrSplit[fileArrSplit.length - 1];
        switch (fileExtension) {
            case "txt":
                return MediaType.TEXT_PLAIN;
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
                return MediaType.IMAGE_JPEG;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}

