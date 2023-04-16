package com.ifpb.lattesmaismais.business;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.model.entity.SolicitedScheduling;
import com.ifpb.lattesmaismais.model.repository.SolicitedSchedulingRepository;
import com.ifpb.lattesmaismais.model.enums.SchedulingStatus;
import com.ifpb.lattesmaismais.presentation.exception.ObjectNotFoundException;

@Service
public class SolicitedSchedulingService {
	
	@Autowired
	private SolicitedSchedulingRepository schedulingRepository;
	
	public List<SolicitedScheduling> findAll() {
		List<SolicitedScheduling> list = schedulingRepository.findAll();
		return list;
	}
	
	public List<SolicitedScheduling> findAll(SolicitedScheduling filter) {
		Example<SolicitedScheduling> exp = Example.of(filter,
				ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		
		List<SolicitedScheduling> list = schedulingRepository.findAll(exp);
		return list;
	}
	
	public List<SolicitedScheduling> findAllByDate(LocalDate date){
		return schedulingRepository.findAllByDate(date);
	}
	
	public List<SolicitedScheduling> findAllByAddress(String address){
		return schedulingRepository.findAllByAddress(address);
	}
	
	public List<SolicitedScheduling> findAllByValidatorId(Integer id){
		return schedulingRepository.findAllByValidatorId(id);
	}
	
	public List<SolicitedScheduling> findAllByRequesterId(Integer id){
		return schedulingRepository.findAllByRequesterId(id);
	}
	public List<SolicitedScheduling> findAllByStatus(SchedulingStatus status){
		return schedulingRepository.findAllByStatus(status);
	}
	//TODO retonar NULO ao invés de lançar exceção?
	public SolicitedScheduling findById(Integer id) throws ObjectNotFoundException {
		if (!schedulingRepository.existsById(id)) {
			throw new ObjectNotFoundException("Não foi possível encontrar SolicitedScheduling com id " + id);
		}

		return schedulingRepository.findById(id).get();
	}

	public SolicitedScheduling save(SolicitedScheduling entity) {
		return schedulingRepository.save(entity);
	}

	public void deleteById(Integer id) throws ObjectNotFoundException {
		if (!schedulingRepository.existsById(id)) {
			throw new ObjectNotFoundException("Não foi possível encontrar SolicitedScheduling com id " + id);
		}

		schedulingRepository.deleteById(id);
	}
}
