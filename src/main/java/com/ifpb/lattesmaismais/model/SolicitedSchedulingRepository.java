package com.ifpb.lattesmaismais.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitedSchedulingRepository extends JpaRepository<SolicitedScheduling, Integer>{

	public List<SolicitedScheduling> findAllByDate(LocalDate date);
	public List<SolicitedScheduling> findAllByAddress(String address);
	public List<SolicitedScheduling> findAllByValidatorId(Integer id);
	public List<SolicitedScheduling> findAllByStatus(Status status);
}
