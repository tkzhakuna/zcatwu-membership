package com.sintaks.mushandi.service;

import com.sintaks.mushandi.model.Site;
import com.sintaks.mushandi.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SiteService {
    private final SiteRepository siteRepository;

    public Site save(@NotNull final Site site){
        return siteRepository.save(site);

    }

    public Site findById(Integer id){
        return siteRepository.getOne(id);
    }

    public List<Site> findAll(){
        return siteRepository.findAll();
    }
}
