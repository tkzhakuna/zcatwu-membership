package com.sintaks.mushandi.controller;

import com.sintaks.mushandi.model.ExchangeRate;
import com.sintaks.mushandi.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/rates")
public class ExchangeRateController {
   private final ExchangeRateService exchangeRateService;

    @PostMapping("/new")
    public ResponseEntity<ExchangeRate> saveNewRate(@RequestBody ExchangeRate exchangeRate, Principal principal){
        return new ResponseEntity<>(exchangeRateService.save(exchangeRate,principal), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ExchangeRate> updateRate(@RequestBody ExchangeRate exchangeRate){
        return new ResponseEntity<>(exchangeRateService.update(exchangeRate), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ExchangeRate>> findAll(){
        return new ResponseEntity<>(exchangeRateService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<Optional<ExchangeRate>> findById(@PathVariable Long id){
        return new ResponseEntity<>(exchangeRateService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id){
        return new ResponseEntity<>(exchangeRateService.deleteById(id), HttpStatus.OK);
    }

}
