package com.ifpb.lattesmaismais.business;

import com.ifpb.lattesmaismais.model.entity.Receipt;
import com.ifpb.lattesmaismais.model.repository.ReceiptRepository;
import com.ifpb.lattesmaismais.presentation.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    public Receipt findById(Integer id) throws ObjectNotFoundException {
        if (!receiptRepository.existsById(id)) {
            throw new ObjectNotFoundException("Não foi possível encontrar comprovante com id " + id);
        }

        return receiptRepository.findById(id).get();
    }

    public Receipt save(Receipt receipt) {
        return receiptRepository.save(receipt);
    }

    public void deleteById(Integer id) throws ObjectNotFoundException {
        if (!receiptRepository.existsById(id)) {
            throw new ObjectNotFoundException("Não foi possível encontrar comprovante com id " + id);
        }

        receiptRepository.deleteById(id);
    }
}
