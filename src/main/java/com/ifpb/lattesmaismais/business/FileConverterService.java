package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.presentation.exception.FileConversionException;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class FileConverterService {

    public byte[] readFile(String pathFile) throws FileConversionException {
        File file = new File(pathFile);
        byte[] fileData = new byte[(int) file.length()];

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(fileData);
        } catch (Exception e) {
            throw new FileConversionException(e.getMessage());
        }

        return fileData;
    }

    public void writeFile(String pathFile, byte[] data) throws FileConversionException {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(pathFile);
            fileOutputStream.write(data);
        } catch (Exception e) {
            throw new FileConversionException(e.getMessage());
        }
    }
}
