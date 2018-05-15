package com.egs.account.service.catalog;

import com.egs.account.model.Catalog;

import java.util.List;


public interface CatalogService {

    Catalog findById(Long id);

    List<Catalog> findAll();

    List<Catalog> findAllByUserId(Long id);

    void saveDocument(Catalog catalog);

    void deleteById(Long id);
}
