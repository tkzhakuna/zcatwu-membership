package com.sintaks.mushandi.repository;

import com.sintaks.mushandi.model.projections.BatchProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sintaks.mushandi.model.ReceiptBatch;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReceiptBatchRepository extends JpaRepository<ReceiptBatch, Long> {

    @Query("select r.institution.institutionName as institution,MAX(DATE(r.cycleDate)) as cycleDate,r.male as male,r.female as female FROM ReceiptBatch r where MONTH(r.cycleDate)=MONTH(:batchDate)" +
            " AND YEAR(r.cycleDate)=YEAR(:batchDate)"+
            " GROUP BY r.institution.institutionName,r.male,r.female " +
            " order by r.institution.institutionName")
    List<BatchProjection> findActive(LocalDate batchDate);
}
