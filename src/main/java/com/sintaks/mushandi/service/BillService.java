package com.sintaks.mushandi.service;

import com.sintaks.mushandi.exceptions.NotFoundException;
import com.sintaks.mushandi.exceptions.NotSavedException;
import com.sintaks.mushandi.exceptions.UnexpectedException;
import com.sintaks.mushandi.model.Bill;
import com.sintaks.mushandi.model.Member;
import com.sintaks.mushandi.model.StopOrder;
import com.sintaks.mushandi.model.User;
import com.sintaks.mushandi.repository.BillRepository;
import com.sintaks.mushandi.repository.MemberRepository;
import com.sintaks.mushandi.repository.StopOrderRepository;
import com.sintaks.mushandi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class BillService implements BaseService<Bill> {

private final BillRepository billRepository;
private final UserRepository userRepository;
private final StopOrderRepository soRepository;
private final MemberRepository memberRepository;
public BillService(BillRepository billRepository,
		UserRepository userRepository,
		StopOrderRepository soRepository,
		MemberRepository memberRepository) {
	
	this.billRepository = billRepository;
	this.userRepository=userRepository;
	this.soRepository=soRepository;
	this.memberRepository=memberRepository;
}

	@Override
	public Bill  save(Bill entity, Principal principal) {
		entity.setCycleNumber(generateCycleNumber(entity.getBillDate()));
		if(billRepository.findByAmountAndMemberAndNotesAndCycleNumber(entity.getAmount(),
				entity.getMember(),entity.getNotes(),entity.getCycleNumber())!=null) {
			throw new NotSavedException("Bill already exists");
		}
		try {
		return billRepository.save(entity);
		}catch(Exception ex) {
			throw new NotSavedException("Error saving bill: "+ex.getLocalizedMessage());
		}
	}

	@Override
	public Bill saveNew(Bill bill) {
		return null;
	}

	@Override
	public Bill update(Bill bill) {
		return null;
	}

	@Override
	public List<Bill> findAll() {
		return null;
	}

	@Override
	public Optional<Bill> findById(Long id) {
		return billRepository.findById(id);
	}

	@Override
	public Boolean deleteById(Long id) {
	try {
		billRepository.deleteById(id);
		return true;
	}catch(Exception ex){
		throw new UnexpectedException("Delete failed: "+ex.getLocalizedMessage());
	}
	}

	@Transactional
	//@Async
	public Boolean billAll(String username,LocalDate cycleDate){
		//get current User
			User user;
			try {
					user=userRepository.findByUsername(username);
			}catch(Exception ex){
				throw new UnexpectedException("Error fetching user: "+ex.getLocalizedMessage());
			}

				if(user!=null&&cycleDate!=null) {
					
					List<StopOrder> stoporderList;

					try {
						stoporderList = soRepository.selectActive(user.getId());
					}catch(Exception ex){
						ex.printStackTrace();
						throw new UnexpectedException("Error retrieving stop order list: "+ex.getLocalizedMessage());
					}

					for(StopOrder so:stoporderList){
						log.info("fetching bill for member "+so.getMember().getSurname()+"===========================");
						Bill tempBill=null;
						try {
							tempBill = billRepository.findByMemberAndBillDate(so.getMember(), cycleDate);
						}catch(Exception ex){
							ex.printStackTrace();
							log.error("Error on member: "+so.getMember().getId()+" - "+ex.getLocalizedMessage());
						}

						if(tempBill!=null) {
							log.error("Bill already exists: "+tempBill.getId());
							continue;
						}


						try {

							Bill bill = new Bill();
							Double percentage = so.getInstitution().getTuPercentage() != null ?
									so.getInstitution().getTuPercentage() :
									so.getInstitution().getSector().getTradeUnion().getTuPercentage();

							Double basicWage = so.getMember().getGrade().getBasicWage();
							Double amount = basicWage != null ? basicWage : null;
							bill.setCycleNumber(generateCycleNumber(cycleDate));
							bill.setBillDate(cycleDate);
							bill.setAmount(amount * percentage / 100);
							bill.setNotes("Billing - " + bill.getCycleNumber());
							bill.setMember(so.getMember());
							bill.setUser(user);
							bill.setCurrency(so.getInstitution().getSector().getTradeUnion().getBaseCurrency());

							if (bill.getAmount() != null) {
								billRepository.save(bill);
							}

						}catch (Exception ex) {
							log.error("Failed to save bill for: "+so.getMember().getId()+" - "+so.getMember().getSurname()+" - "+so.getMember().getFirstname());
						}
						
					}
				}else{
					throw new UnexpectedException("Billing failed for all members");
				}

		return true;
	}

	public Bill saveBill(Bill bill,Principal principal) {
		
		User user=null;
		try {
			user=userRepository.findByUsername(principal.getName());
		}catch(Exception ex){
			throw new UnexpectedException("Error fetching user: "+ex.getLocalizedMessage());
		}

		if(user!=null) {
		Member member=null;
		try {
			member = memberRepository.findMemberByNationalIdAndTU(bill.getNationalId(), user.getEmployee().getTradeUnion().getId());
		}catch(Exception ex){
			throw new NotFoundException("Member not found: "+ex.getLocalizedMessage());
		}


		if(member!=null) {
			try {
				bill.setUser(user);
				bill.setMember(member);
				return save(bill,principal);
			}catch(Exception ex){
				throw new NotSavedException("Bill not saved: "+ex.getLocalizedMessage());
			}
		}else {
			throw new NotFoundException("National Id not found");
		}
		}else {
			throw new NotSavedException("Bill not saved");
		}
		
	}
	
public Integer generateCycleNumber(LocalDate billDate) {
		
		int cycleNo=0;
		if(billDate!=null) {
			int year=billDate.getYear();
			int month=billDate.getMonthValue();
			String cycle=month+""+year;
			
			try {
				cycleNo=Integer.valueOf(cycle);
				
			}catch(Exception ex){
				throw new IllegalArgumentException("Error processing Cycle Number: "+ex.getLocalizedMessage());
			}
		}
		
		return cycleNo;
	}


}
