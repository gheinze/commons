package com.accounted4.commons.io;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utilities for Persisting json data to the files system and load them back in.
 * 
 * @author glenn
 * @param <T>
 */
public class FilePersistenceService<T> implements PersistenceService<T> {

    private Class<T> clazz;
    
    
    public FilePersistenceService(Class<T> clazz) {
        this.clazz = clazz;
    }
    
    
    @Override
    public void persistAsJson(List<T> dataObjects, String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(fileName), dataObjects);
    }
    

    @Override
    public List<T> loadFromJson(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
        return mapper.readValue(new File(fileName), type);
        //return mapper.readValue(new File(fileName), new TypeReference<List<T>>() {});
    }


    @Override
    public void backup(String sourceFile) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.now();
        String formattedDateTime = dateTime.format(formatter);
        File source = new File(sourceFile);
        File dest = new File(sourceFile + ".bak_" + formattedDateTime);
        Files.copy(source.toPath(), dest.toPath());
    }

    
}
