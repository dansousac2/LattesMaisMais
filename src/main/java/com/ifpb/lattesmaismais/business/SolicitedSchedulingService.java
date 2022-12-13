package com.ifpb.lattesmaismais.business;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import com.ifpb.lattesmaismais.model.SolicitedScheduling;
import com.ifpb.lattesmaismais.model.SolicitedSchedulingRepository;
import com.ifpb.lattesmaismais.model.StatusScheduling;

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
	public List<SolicitedScheduling> findAllByStatus(StatusScheduling status){
		return schedulingRepository.findAllByStatus(status);
	}
}
