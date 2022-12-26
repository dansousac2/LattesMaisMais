package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.model.entity.Receipt;
import com.ifpb.lattesmaismais.model.repository.ReceiptRepository;
import com.ifpb.lattesmaismais.presentation.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    public Receipt findById(Integer id) throws ObjectNotFoundException {
        return receiptRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Não foi possível encontrar comprovante com id " + id)
        );
    }

    public Receipt save(Receipt receipt) {
        return receiptRepository.save(receipt);
    }

    public void deleteById(Integer id) {
        receiptRepository.deleteById(id);
    }

}
