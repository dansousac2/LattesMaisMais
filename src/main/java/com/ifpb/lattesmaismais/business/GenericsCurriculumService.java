package com.ifpb.lattesmaismais.business;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.model.entity.Curriculum;
import com.ifpb.lattesmaismais.model.entity.Entry;
import com.ifpb.lattesmaismais.model.entity.Receipt;
import com.ifpb.lattesmaismais.model.entity.User;
import com.ifpb.lattesmaismais.model.enums.CurriculumStatus;
import com.ifpb.lattesmaismais.model.enums.ReceiptStatus;
import com.ifpb.lattesmaismais.presentation.CurriculumDtoBack;
import com.ifpb.lattesmaismais.presentation.exception.ObjectNotFoundException;

import jakarta.validation.Valid;

@Service
public class GenericsCurriculumService {
	
	public String createVersionName() {
		LocalDateTime ldt = LocalDateTime.now();
		String ldtString = ldt.format(DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss"));
		return "V_" + ldtString;
	}

	public User verifyCurriculumDto(@Valid CurriculumDtoBack dto, CurriculumService service) throws ObjectNotFoundException {
		Curriculum curriculum = service.findById(dto.getId());
		//TODO adicionar mais condições de validação do currículo(?)
		boolean difOwnerId = curriculum.getOwner().getId() != dto.getOwnerId();
		boolean difModificationDate = !dateTimeToString(curriculum.getLastModification()).equals(dto.getLastModification());
		
		if(difOwnerId || difModificationDate) {
			throw new IllegalArgumentException("Currículo não pertencente ao usuário logado!");
		}
		
		return curriculum.getOwner();
	}
	
	public String dateTimeToString(LocalDateTime lastModification) {
		return lastModification.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss"));
	}
	
	/**
	 * Remove os ids das entradas e seus de comprovantes para criar cópias para a nova versão de currículo salva no BD e seta status do
	 * currículo com base nos status dos comprovantes.
	 * Também seta herança (atributo) da entrada caso seja a primeira a referenciar um arquivo salvo. A herança é o nome no arquivo salvo
	 * no BD.
	 * @param entryList
	 * @return CurriculumStatus
	 */
	public CurriculumStatus organizeEntriesReceiptsAndStatus(List<Entry> entryList) {
		CurriculumStatus curriculumStatus = CurriculumStatus.CHECKED;
		boolean next = true;
		
		for(Entry e : entryList) {
			e.setId(null);
			
			if(!e.getReceipts().isEmpty()) {
				
				for(Receipt r : e.getReceipts()) {
					if(r.getHeritage() == null && r.getUrl() == null) {
						r.setHeritage(r.getId() + "." + r.getExtension());
					}
					
					r.setId(null);
					
					if(next && r.getStatus() != ReceiptStatus.CHECKED_BY_VALIDATOR) {
						curriculumStatus = CurriculumStatus.UNCHECKED;
						next = false;
					}
				};
			} else if(next) {
				curriculumStatus = CurriculumStatus.UNCHECKED;
				next = false;
			}
		}
		return curriculumStatus;
	}

	public CurriculumStatus generateStatusCurriculumOnly(List<Entry> entries) {
		for(Entry entry : entries) {
			for(Receipt rec : entry.getReceipts()) {
				if(rec.getStatus() != ReceiptStatus.CHECKED_BY_VALIDATOR) {
					return CurriculumStatus.UNCHECKED;
				}
			}
		}
		return CurriculumStatus.CHECKED;
	}

}
