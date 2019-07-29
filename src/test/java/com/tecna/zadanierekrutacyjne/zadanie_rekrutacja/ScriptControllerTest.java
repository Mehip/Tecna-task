package com.tecna.zadanierekrutacyjne.zadanie_rekrutacja;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ScriptController.class)
public class ScriptControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScriptService scriptService;

    @Test
    public void addScript() throws Exception {
        Path path = Paths.get("/Pulpit/Script1.groovy");
        String originalFileName = "Script1.groovy";
        byte[] content = Files.readAllBytes(path);

        MockMultipartFile file = new MockMultipartFile("file",
                originalFileName, MediaType.MULTIPART_FORM_DATA_VALUE, content);

        this.mockMvc.perform(multipart("/scripts")
                .file(file))
                .andExpect(status().isOk());
    }

    @Test
    public void getScript() throws Exception {
        Path path = Paths.get("/Pulpit/Script1.groovy");
        String name = "Script1.groovy";
        String contentType = "application/octet-stream";
        byte[] content = Files.readAllBytes(path);

        ScriptGroovy scriptGroovy = new ScriptGroovy(name, contentType, content);

        given(scriptService.getScript(scriptGroovy.getName())).willReturn(scriptGroovy);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/scripts/Script1.groovy")
                .accept(MediaType.APPLICATION_OCTET_STREAM)).andReturn();

        ScriptGroovy scriptGroovy1 = new ScriptGroovy();
        scriptGroovy1.setCode(result.getResponse().getContentAsByteArray());
        assertThat(scriptGroovy1.getCode()).isEqualTo(scriptGroovy.getCode());
    }

    @Test
    public void updateScript() throws Exception {
        Path path = Paths.get("/Pulpit/Script1.groovy");
        String originalFileName = "Script1.groovy";
        byte[] content = Files.readAllBytes(path);

        MockMultipartFile file = new MockMultipartFile("file",
                originalFileName, MediaType.MULTIPART_FORM_DATA_VALUE, content);

        this.mockMvc.perform(multipart("/scripts")
                .file(file))
                .andExpect(status().isOk());
    }

    @Test
    public void startScript() throws IOException {
        Path path = Paths.get("/Pulpit/Script1.groovy");
        String name = "Script1.groovy";
        String originalFileName = "Script1.groovy";
        String contentType = "application/octet-stream";
        byte[] content = Files.readAllBytes(path);

        given(scriptService.runScript(originalFileName)).willReturn("Hello World!");

        MultipartFile file = new MockMultipartFile(name,
                originalFileName, contentType, content);

        scriptService.addScript(file);

        String txt = "Hello World!";

        assertThat(txt).isEqualTo(scriptService.runScript(originalFileName));
    }

    @Test
    public void deleteScript() throws Exception {
        Path path = Paths.get("/Pulpit/Script1.groovy");
        String name = "Script1.groovy";
        String originalFileName = "Script1.groovy";
        String contentType = "application/octet-stream";
        byte[] content = Files.readAllBytes(path);

        MultipartFile file = new MockMultipartFile(name,
                originalFileName, contentType, content);

        scriptService.addScript(file);

        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/scripts/{filename}", "Script1.groovy")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}