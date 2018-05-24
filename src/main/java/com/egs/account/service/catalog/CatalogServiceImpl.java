package com.egs.account.service.catalog;


import com.egs.account.model.Catalog;
import com.egs.account.repository.catalog.CatalogRepository;
import com.egs.account.utils.domainUtils.DomainUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CatalogServiceImpl implements CatalogService {


    @Autowired
    CatalogRepository catalogRepository;

    @Autowired
    DomainUtils domainUtils;

    public Catalog findById(Long id) {
        return catalogRepository.getOne(id);
    }

    public List<Catalog> findAll() {
        return catalogRepository.findAll();
    }

    public List<Catalog> findAllByUserId(Long userId) {
        return catalogRepository.findAllByUserId(userId);
    }

    public void saveDocument(Catalog catalog){ catalogRepository.save(catalog); }

    public void deleteById(Long id){
        catalogRepository.deleteById(id);
    }


}
