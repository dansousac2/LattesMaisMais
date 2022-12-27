package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.model.entity.Receipt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReceiptConverterService {

    public Receipt fileToEntity(MultipartFile file) {
        Receipt entity = new Receipt();

        int extensionIndex = file.getOriginalFilename().indexOf(".");
        String fileName = file.getOriginalFilename();

        entity.setName(fileName.substring(0, extensionIndex));
        entity.setExtension(fileName.substring(extensionIndex));

        return entity;
    }
}
