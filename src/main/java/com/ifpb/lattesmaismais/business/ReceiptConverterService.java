package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.model.entity.Receipt;
import com.ifpb.lattesmaismais.presentation.exception.FileWithoutNameException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReceiptConverterService {

    public Receipt fileToEntity(MultipartFile file, String commentary) throws FileWithoutNameException {
        Receipt entity = new Receipt();

        if (file.getOriginalFilename().isBlank() || file.getOriginalFilename().isEmpty()) {
            throw new FileWithoutNameException("O arquivo enviado não possui nome!");
        }

        int extensionIndex = file.getOriginalFilename().indexOf(".");
        String fileName = file.getOriginalFilename();

        entity.setName(fileName.substring(0, extensionIndex));
        entity.setExtension(fileName.substring(extensionIndex));
        entity.setCommentary(commentary);

        return entity;
    }
}
