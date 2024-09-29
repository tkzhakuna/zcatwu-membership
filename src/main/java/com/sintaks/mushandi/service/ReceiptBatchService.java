package com.sintaks.mushandi.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import com.sintaks.mushandi.model.Receipt;
import com.sintaks.mushandi.model.ReceiptBatch;

public interface ReceiptBatchService {

	List<ReceiptBatch>findAll(Pageable pageable);
	ReceiptBatch saveBatch(ReceiptBatch batch,String username);
	Boolean deleteBatch(Long batchId);
	ReceiptBatch updateBatch(ReceiptBatch batch);
	@Async
	CompletableFuture<List<Receipt>>findByReceiptBatch(Long batchId);
	ReceiptBatch findById(Long id);
	@Async
	CompletableFuture<List<Receipt>>saveReceipt(MultipartFile file,String username,Long batchId)throws Exception;
}
