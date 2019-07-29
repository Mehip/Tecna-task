package com.tecna.zadanierekrutacyjne.zadanie_rekrutacja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
public class ScriptController {

    @Autowired
    private ScriptService scriptService;

    @PostMapping(value = "/scripts")
    public void addScript(@RequestParam("file") MultipartFile file) throws IOException {
        scriptService.addScript(file);
    }

    @GetMapping(value = "/scripts/{fileName}")
    @ResponseBody
    public ResponseEntity<Resource> getScript(@PathVariable String fileName) {
        ScriptGroovy script = scriptService.getScript(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(script.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + script.getName() + "\"")
                .body(new ByteArrayResource(script.getCode()));
    }

    @PutMapping(value = "/scripts/{fileName}")
    public void updateScript(@PathVariable String fileName, @RequestParam("file") MultipartFile file) throws IOException {
        scriptService.updateScript(fileName, file);
    }

    @GetMapping(value = "/run/{fileName}")
    public String StartScript(@PathVariable String fileName) throws IOException {
        //pobranie skryptu z bazy danych
        String text = scriptService.runScript(fileName);

        return text;
    }

    @DeleteMapping(value = "/scripts/{fileName}")
    public void deleteScript(@PathVariable String fileName) {
        scriptService.deleteScript(fileName);
    }
}
