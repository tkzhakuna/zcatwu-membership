package com.sintaks.mushandi.service;

import com.sintaks.mushandi.exceptions.UnexpectedException;
import com.sintaks.mushandi.model.ExchangeRate;
import com.sintaks.mushandi.model.User;
import com.sintaks.mushandi.repository.ExchangeRateRepository;
import com.sintaks.mushandi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService{
    private final ExchangeRateRepository exchangeRateRepository;
    private final UserRepository userRepository;


    @Override
    public ExchangeRate save(ExchangeRate exchangeRate, Principal principal) {
        try {
            User user=userRepository.findByUsername(principal.getName());
            exchangeRate.setUser(user);
            return exchangeRateRepository.save(exchangeRate);
        }catch (Exception ex){
            throw new UnexpectedException("An unexpected error has occurred while saving exchange rate");
        }

    }

    @Override
    public ExchangeRate saveNew(ExchangeRate exchangeRate) {
        return null;
    }

    @Override
    public ExchangeRate update(ExchangeRate exchangeRate) {
        try {
            return exchangeRateRepository.save(exchangeRate);
        }catch (Exception ex){
            throw new UnexpectedException("An unexpected error has occurred while saving exchange rate");
        }
    }

    @Override
    public List<ExchangeRate> findAll() {
        try {
            return exchangeRateRepository.findAll();
        }catch (Exception ex){
        throw new UnexpectedException("Error fetching exchange rates");
    }

    }

    @Override
    public Optional<ExchangeRate> findById(Long id) {
        try {
            return exchangeRateRepository.findById(id);
        }catch (Exception ex){
            throw new UnexpectedException("Error fetching exchange rate with id "+id);
        }
    }

    @Override
    public Boolean deleteById(Long id) {
        try {
             exchangeRateRepository.deleteById(id);
            return true;
        }catch (Exception ex){
            throw new UnexpectedException("Error deleting exchange rate");
        }
    }
}
