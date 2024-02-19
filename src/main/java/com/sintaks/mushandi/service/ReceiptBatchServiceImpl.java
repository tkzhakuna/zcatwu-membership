package com.sintaks.mushandi.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import com.sintaks.mushandi.exceptions.NotDeletedException;
import com.sintaks.mushandi.exceptions.NotFoundException;
import com.sintaks.mushandi.exceptions.NotSavedException;
import com.sintaks.mushandi.exceptions.UnexpectedException;
import com.sintaks.mushandi.model.*;
import com.sintaks.mushandi.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReceiptBatchServiceImpl implements ReceiptBatchService {

	private final ReceiptBatchRepository rbr;
	private final ReceiptRepository receiptRepository;
	private final MemberRepository memberRepository;
	private final UserRepository userRepository;
	private final ExchangeRateRepository exchangeRateRepository;

	public ReceiptBatchServiceImpl(ReceiptBatchRepository rbr, ReceiptRepository receiptRepository,
								   MemberRepository memberRepository, UserRepository userRepository,
								   ExchangeRateRepository exchangeRateRepository) {
		this.rbr = rbr;
		this.receiptRepository = receiptRepository;
		this.memberRepository = memberRepository;
		this.userRepository = userRepository;
		this.exchangeRateRepository=exchangeRateRepository;
	}

	public static final String DIRECTORY_MARKER = "/";
    @Value("${tempDirectory}")
    private String tempFolder;

	Object target;
	Logger logger=LoggerFactory.getLogger(ReceiptBatchServiceImpl.class);

	@Override
	public ReceiptBatch saveBatch(ReceiptBatch batch,String username) {
		User user;

		try {
		user=userRepository.findByUsername(username);
		}catch(Exception ex){
			throw new UnexpectedException("An Error occurred while retrieving user: "+ex.getLocalizedMessage());
		}

		ExchangeRate exchangeRate=exchangeRateRepository.findByCurrency(batch.getCurrency().toString());
		if(exchangeRate!=null){
			batch.setRate(exchangeRate);
		}else{
			throw new NotFoundException("Currency not found");
		}

		if(user!=null){
			batch.setUser(user);
			batch.setMembers(batch.getMale()+batch.getFemale());
			ReceiptBatch bt;
			try{
				bt=rbr.save(batch);
			}catch (Exception ex){
				throw new NotSavedException("Failed to save batch: "+ex.getLocalizedMessage());
			}

			return bt;
		}else{
			throw new NotFoundException("User not found");
		}

	}

	@Override
	public Boolean deleteBatch(Long batchId) {
		boolean bool;
		try {
			bool=rbr.existsById(batchId);
		}catch (Exception ex){
			throw new NotFoundException("Batch does not exists");
		}

		if(bool) {
			try{
				rbr.deleteById(batchId);
				return true;
			}catch (Exception ex){
				throw new NotDeletedException("Batch deletion failed: "+ex.getLocalizedMessage());
			}

		}
		return false;
	}

	@Override
	public ReceiptBatch updateBatch(ReceiptBatch batch) {

		ReceiptBatch btch;
		try {
			btch=rbr.getOne(batch.getId());
		}catch (Exception ex){
			throw new UnexpectedException("Error fetching batch: "+ex.getLocalizedMessage());
		}


		if(btch!=null) {
			try {
				batch.setMembers(batch.getMale()+batch.getFemale());
				batch.setCurrency(btch.getCurrency());
				batch.setRate(btch.getRate());
				return rbr.save(batch);
			}catch (Exception ex){
				throw new NotSavedException("Batch saving failed ");
			}
			
		}else{
			throw new NotFoundException("Batch does not exists");
		}

	}

	

	@Override
	public ReceiptBatch findById(Long id) {
		ReceiptBatch batch;
		try{
			batch=rbr.getOne(id);
		}catch(Exception ex){
			throw new UnexpectedException("Error finding batch: "+ex.getLocalizedMessage());
		}
		if(batch!=null) {
			return batch;
		}else{
			throw new NotFoundException("Batch not found");
		}
	}

	@Override
	@Async
	public CompletableFuture<List<Receipt>>saveReceipt(MultipartFile file,String username,Long batchId){
		//get current User
		User user=userRepository.findByUsername(username);
		ReceiptBatch batch=rbr.getOne(batchId);
		
		if(batch==null){
			throw new NotFoundException("Batch with given id not found");
		}


		long start=System.currentTimeMillis();
		List<Receipt>receipts;
		try {
		receipts=parseCSVFile(file, user, batch);
		}catch (Exception ex){
			if(ex instanceof NotFoundException){
				logger.info("Not found error {}", ex.getLocalizedMessage());
				throw new NotFoundException(ex.getLocalizedMessage());
			}else{
				throw new UnexpectedException("Error saving receipts");
			}
		}

		logger.info("saving list of receipts of size {} on thread {}",receipts.size(),Thread.currentThread().getName());
		try {

			receipts = receiptRepository.saveAll(receipts);
			receipts.forEach(receipt -> {
				Member member=receipt.getMember();
				member.setStatus("Active");
				memberRepository.save(member);
			});
			long end = System.currentTimeMillis();
			logger.info("Total time {}", (end - start));

			return CompletableFuture.completedFuture(receipts);
		}catch(Exception ex){
			ex.printStackTrace();
			return CompletableFuture.completedFuture(null);
		}
	}
	
	private List<Receipt>parseCSVFile(final MultipartFile file,User user,ReceiptBatch batch) throws Exception {
		final List<Receipt>receipts=new ArrayList<>();
		ExchangeRate exchangeRate=exchangeRateRepository.findByCurrency(batch.getCurrency().toString());

		File file1=new File(tempFolder+"/"+file.getOriginalFilename());
			try(InputStream inputStream=new FileInputStream(file1);
					final BufferedReader br=new BufferedReader(new InputStreamReader(inputStream))){
				
				String line;
				while((line=br.readLine())!=null) {
					final String[] data=line.split(",");
					final Receipt receipt=new Receipt();
					Member member;
					try {
						member = memberRepository.findByIdAndInstitution(Long.valueOf(data[0].replace("\"", "")),batch.getInstitution().getId());
					if(member==null){
						throw new NotFoundException("");
					}
					}catch(NotFoundException ex){
						logger.error("Member not found: " + Long.valueOf(data[0].replace("\"", "")));
						throw new NotFoundException("A member in the list does not belong to this institution");

					}
					catch(Exception ex){
						logger.error("Member not found: {}", ex.getLocalizedMessage());
						continue;
					}

					try {
					receipt.setMember(member);
					receipt.setCycleNumber(batch.getCycleNumber());
					receipt.setAmount(Double.parseDouble(data[1].replace("\"", "")));
					receipt.setUser(user);
					receipt.setReceiptNumber(batch.getReceiptNumber());
					receipt.setReceiptDate(batch.getCycleDate());
					receipt.setNotes("Subscriptions");
					receipt.setReceiptBatch(batch);
					receipt.setRate(batch.getRate());
					receipt.setRate(exchangeRate);
					receipt.setCurrency(batch.getCurrency());
					
					receipts.add(receipt);
					}catch(Exception ex) {
						
						logger.error("Parse error {}",line);
					}
				}
				file1.deleteOnExit();
				return receipts;
			
		}catch(final IOException ex) {
			file1.delete();

			logger.error("Failed to parse CSV File {}",ex.getLocalizedMessage());
			throw new Exception("Failed to parse CSV File {}",ex);
		}catch(Exception ex) {
			file1.delete();

			throw new Exception("Failed to parse CSV File {}",ex);
		}
	}
	
	
	@Override
	@Async
	public CompletableFuture<List<Receipt>>findByReceiptBatch(Long batchId){
		logger.info("get list of receipts by "+Thread.currentThread().getName());
		ReceiptBatch batch=rbr.getOne(batchId);
		List<Receipt>receipts=receiptRepository.findByReceiptBatchId(batch.getId());
		return CompletableFuture.completedFuture(receipts);
	}

	@Override
	public List<ReceiptBatch> findAll() {
		List<ReceiptBatch>batchList;
		try{
			batchList=rbr.findAll();
		}catch (Exception ex){
			throw new UnexpectedException("Error retrieving batch list: "+ex.getLocalizedMessage());
		}

		if(batchList!=null) {
			return batchList;
		}else{
			throw new NotFoundException("No batches Found");
		}
	}
	
	
}
