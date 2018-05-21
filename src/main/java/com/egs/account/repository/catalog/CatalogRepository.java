package com.egs.account.repository.catalog;


import com.egs.account.model.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("catalogRepository")
public interface CatalogRepository extends JpaRepository<Catalog, Long> {
    List<Catalog> findAllByUserId(Long userId);
}