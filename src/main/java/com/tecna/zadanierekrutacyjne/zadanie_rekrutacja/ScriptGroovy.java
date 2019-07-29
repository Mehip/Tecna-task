package com.tecna.zadanierekrutacyjne.zadanie_rekrutacja;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.Size;

@Entity
public class ScriptGroovy {

    @Id
    @Size(min = 1, max = 50)
    String name;

    @Size(max = 50)
    String fileType;

    @Lob
    byte[] code;

    public ScriptGroovy() {
    }

    public ScriptGroovy(String name, String fileType, byte[] code) {
        this.name = name;
        this.code = code;
        this.fileType = fileType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileType(){ return fileType;}

    public void setFileType(String fileType){
        this.fileType = fileType;
    }

    public byte[] getCode() {
        return code;
    }

    public void setCode(byte[] code) {
        this.code = code;
    }
}
