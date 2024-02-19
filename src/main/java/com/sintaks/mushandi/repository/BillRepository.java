package com.sintaks.mushandi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sintaks.mushandi.model.Bill;
import com.sintaks.mushandi.model.Member;

import java.time.LocalDate;

public interface BillRepository extends JpaRepository<Bill, Long> {
	Bill findByAmountAndMemberAndNotesAndCycleNumber(Double amount,
			Member member,String notes,Integer cycleNumber);

	Bill findByMemberAndBillDate(Member member, LocalDate date);
}
