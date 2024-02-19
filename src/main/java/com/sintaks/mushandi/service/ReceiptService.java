package com.sintaks.mushandi.service;

import com.sintaks.mushandi.exceptions.NotFoundException;
import com.sintaks.mushandi.exceptions.NotSavedException;
import com.sintaks.mushandi.exceptions.UnexpectedException;
import com.sintaks.mushandi.model.ExchangeRate;
import com.sintaks.mushandi.model.Member;
import com.sintaks.mushandi.model.Receipt;
import com.sintaks.mushandi.model.User;
import com.sintaks.mushandi.repository.ExchangeRateRepository;
import com.sintaks.mushandi.repository.MemberRepository;
import com.sintaks.mushandi.repository.ReceiptRepository;
import com.sintaks.mushandi.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReceiptService implements BaseService<Receipt> {

    private final ReceiptRepository receiptRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final ExchangeRateRepository exchangeRateRepository;


    public ReceiptService(ReceiptRepository receiptRepository,
                          MemberRepository memberRepository,
                          UserRepository userRepository,
                          ExchangeRateRepository exchangeRateRepository) {
        this.receiptRepository = receiptRepository;
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
        this.exchangeRateRepository=exchangeRateRepository;
    }

    @Override
    public Receipt save(Receipt entity, Principal principal) {
        Receipt receipt;
        try {
            ExchangeRate exchangeRate = exchangeRateRepository.findByCurrency(entity.getCurrency().toString());
            entity.setCycleNumber(generateCycleNumber(entity.getReceiptDate()));
            entity.setRate(exchangeRate);
            receipt = receiptRepository.save(entity);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new NotSavedException("Receipt not saved, please try again: " + ex.getLocalizedMessage());
        }

        if (receipt != null) {
            return receipt;
        } else {
            throw new NotSavedException("Receipt not saved, please try again");
        }
    }

    @Override
    public Receipt saveNew(Receipt receipt) {
        return null;
    }

    @Override
    public Receipt update(Receipt receipt) {
        return null;
    }

    @Override
    public List<Receipt> findAll() {
        return new ArrayList<>();
    }

    public Receipt saveReceipt(Receipt receipt, Principal principal) {
        User user;
        try {
            user = userRepository.findByUsername(principal.getName());
        } catch (Exception ex) {
            throw new NotFoundException("Error fetching user: " + ex.getLocalizedMessage());
        }

        if (user != null) {
            Member member;
            try {
                member = memberRepository.findMemberByNationalIdAndTU(receipt.getNationalId(), user.getEmployee().getTradeUnion().getId());
            } catch (Exception ex) {
                throw new NotFoundException("Error fetching member: " + ex.getLocalizedMessage());
            }
            if (member != null) {
                receipt.setUser(user);
                receipt.setMember(member);
                member.setStatus("Active");
                memberRepository.save(member);
                return save(receipt, principal);
            } else {
                throw new NotFoundException("Member with provided National Id not found");
            }
        } else {
            throw new NotSavedException("Receipt not saved");
        }

    }

    @Override
    public Optional<Receipt> findById(Long id) {
        Optional<Receipt> receipt;
        try {
            receipt = receiptRepository.findById(id);
        } catch (Exception ex) {
            throw new NotFoundException("Error getting receipt: " + ex.getLocalizedMessage());
        }

        if (receipt.isPresent()) {
            return receipt;
        } else {
            throw new NotFoundException("No receipt found");
        }
    }

    @Override
    public Boolean deleteById(Long id) {
        return false;
    }


    public List<Receipt> findByReceiptBatchId(Long batchId) {
        List<Receipt> receipts;
        try {
            receipts = receiptRepository.findByReceiptBatchId(batchId);
        } catch (Exception ex) {
            throw new UnexpectedException("Error finding receipt by Batch: " + ex.getLocalizedMessage());
        }

        if (receipts != null) {
            return receipts;
        } else {
            throw new NotFoundException("Receipt not found");
        }
    }

    public Integer generateCycleNumber(LocalDate receiptDate) {

        int cycleNo = 0;
        if (receiptDate != null) {
            int year = receiptDate.getYear();
            int month = receiptDate.getMonthValue();
            String cycle = month + "" + year;

            try {
                cycleNo = Integer.parseInt(cycle);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return cycleNo;
    }


}
