package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.model.entity.Receipt;
import com.ifpb.lattesmaismais.presentation.dto.ReceiptDtoLink;
import com.ifpb.lattesmaismais.presentation.exception.FileWithoutNameException;

import jakarta.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReceiptConverterService {

    public Receipt fileToEntity(MultipartFile file, String commentary, String url) throws FileWithoutNameException {
        Receipt entity = new Receipt();

        if (file.getOriginalFilename().isBlank() || file.getOriginalFilename().isEmpty()) {
            throw new FileWithoutNameException("O arquivo enviado não possui nome!");
        }

        int extensionIndex = file.getOriginalFilename().indexOf(".");
        String fileName = file.getOriginalFilename();

        entity.setName(fileName.substring(0, extensionIndex));
        entity.setExtension(fileName.substring(extensionIndex));
        entity.setCommentary(commentary);
        entity.setUrl(url);

        return entity;
    }

	public Receipt dtoToReceiptWithUrl(@Valid ReceiptDtoLink dto) {
		// status setado no construtor
		Receipt entity = new Receipt();
		entity.setCommentary(dto.getCommentary());
		entity.setUrl(dto.getUrl());
		
		return entity;
	}
}
