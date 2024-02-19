package com.sintaks.mushandi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sintaks.mushandi.model.Receipt;
import com.sintaks.mushandi.model.ReceiptBatch;
@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
	List<Receipt>findByReceiptBatchId(Long id);
}
