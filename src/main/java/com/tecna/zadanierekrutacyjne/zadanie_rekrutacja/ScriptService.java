package com.tecna.zadanierekrutacyjne.zadanie_rekrutacja;

import groovy.lang.GroovyShell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class ScriptService {

    @Autowired
    private ScriptRepository scriptRepository;

    public void addScript(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        ScriptGroovy script = new ScriptGroovy(fileName, file.getContentType(), file.getBytes());
        scriptRepository.save(script);
    }

    public ScriptGroovy getScript(String fileId) {
        return scriptRepository.findById(fileId).orElse(null);
    }


    public void updateScript(String fileName, MultipartFile file) throws IOException {
        if (scriptRepository.existsById(fileName)) {
            ScriptGroovy script = new ScriptGroovy(fileName, file.getContentType(), file.getBytes());
            scriptRepository.save(script);
        }
    }

    public String runScript(String fileName) throws IOException {
        ScriptGroovy script = scriptRepository.findById(fileName).orElse(null);

        //Files
        File f = new File("/Pulpit/" + fileName);
        File result = new File("/Pulpit/Result.txt");

        //save the byte code to a file f
        try (FileOutputStream fos = new FileOutputStream(f)) {
            fos.write(script.getCode());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Change of stream
        PrintStream o = new PrintStream(result);
        System.setOut(o);

        //run the script
        GroovyShell shell = new GroovyShell();
        shell.evaluate(f);

        //reading the result of the script
        FileReader fileReader = new FileReader(result);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String text = "";
        int counter = 0;

        try {
            String textLine = bufferedReader.readLine();
            do {
                text = (counter > 0) ? text + "\n" + textLine : text + textLine;
                counter++;

                textLine = bufferedReader.readLine();
            } while (textLine != null);
        } finally {
            bufferedReader.close();
        }

        return text;
    }

    public void deleteScript(String fileName) {
        scriptRepository.deleteById(fileName);
    }
}
