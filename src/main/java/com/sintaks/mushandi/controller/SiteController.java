package com.sintaks.mushandi.controller;

import com.sintaks.mushandi.model.Site;
import com.sintaks.mushandi.service.SiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

@CrossOrigin(origins="*", maxAge=3600)
@RequiredArgsConstructor
@RestController
@RequestMapping("sites")
public class SiteController {
    private final SiteService siteService;

    @PostMapping(path="/",consumes={MediaType.APPLICATION_JSON_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Site> saveSite(@RequestBody final Site site){
        return new ResponseEntity<>(siteService.save(site), HttpStatus.OK);
    }

    @PutMapping(path="/update",consumes={MediaType.APPLICATION_JSON_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Site> updateSite(@RequestBody final Site site){
        return new ResponseEntity<>(siteService.save(site), HttpStatus.OK);
    }

    @GetMapping(path="/find-by-id/{id}",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Site> findById(@PathVariable Integer id){
        return new ResponseEntity<>(siteService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path="/find-all")
    public ResponseEntity<List<Site>> findAll(){
        return new ResponseEntity<>(siteService.findAll(), HttpStatus.OK);
    }

}
