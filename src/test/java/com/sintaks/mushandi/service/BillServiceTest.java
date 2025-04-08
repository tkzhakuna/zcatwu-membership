package com.sintaks.mushandi.service;

import com.sintaks.mushandi.model.Bill;
import com.sintaks.mushandi.model.Member;
import com.sintaks.mushandi.repository.BillRepository;
import com.sintaks.mushandi.repository.MemberRepository;
import com.sintaks.mushandi.repository.StopOrderRepository;
import com.sintaks.mushandi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class BillServiceTest {
@Mock
private  BillRepository billRepository;
@Mock
private  UserRepository userRepository;
@Mock
private  StopOrderRepository soRepository;
@Mock
private  MemberRepository memberRepository;

@InjectMocks
private BillService billService;

    @Test
    void save() {
//        Bill bill=buildBill();
//        when(billRepository.findByAmountAndMemberAndNotesAndCycleNumber(bill.getAmount(),
//                bill.getMember(),bill.getNotes(),bill.getCycleNumber())).thenReturn(bill);
//
//        Bill savedBill=billService.save(bill,null);
//
//        assertEquals(1L,savedBill.getId());
    }

    @Test
    void update() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void billAll() {
    }

    @Test
    void saveBill() {
    }

    @Test
    void generateCycleNumber() {
    }

    Bill buildBill(){
        Bill bill=new Bill();
        Member member=new Member();
        bill.setId(1L);
        bill.setAmount(10.0);
        bill.setBillDate(LocalDate.now());
        bill.setNationalId("12111111T12");
        bill.setNotes("Bill");
        bill.setMember(member);
        return bill;
    }
}