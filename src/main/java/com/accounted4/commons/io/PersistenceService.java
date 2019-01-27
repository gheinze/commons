package com.accounted4.commons.io;

import java.io.IOException;
import java.util.List;

/**
 * Marshalling and unmarshalling utilities for data files.
 * 
 * @author glenn
 * @param <T> POJO
 */
public interface PersistenceService<T> {

    /**
     * Serialize a list of POJOs to a file in json format.
     * 
     * @param dataObjects
     * @param fileName
     * @throws IOException 
     */
    void persistAsJson(List<T> dataObjects, String fileName) throws IOException;
    
    
    /**
     * Unmarshal a json file from disk into a list of POJOs.
     * @param fileName
     * @return
     * @throws IOException 
     */
    List<T> loadFromJson(String fileName) throws IOException;
    
    
    /**
     * Make a copy of a file.
     * 
     * @param sourceFile
     * @throws IOException 
     */
    void backup(String sourceFile) throws IOException;
    
    
}
