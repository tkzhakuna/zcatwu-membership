package com.sintaks.mushandi.repository;


import com.sintaks.mushandi.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate,Long> {
    @Query(value = "SELECT * FROM exchange_rate WHERE currency=:currency ORDER BY id desc LIMIT 1",nativeQuery = true)
    ExchangeRate findByCurrency(String currency);
}
