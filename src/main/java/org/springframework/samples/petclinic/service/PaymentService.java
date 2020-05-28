package org.springframework.samples.petclinic.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Payment;
import org.springframework.samples.petclinic.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {
	
	private PaymentRepository paymentRepo;
	
	@Autowired
	public PaymentService(final PaymentRepository paymentRepository) {
		this.paymentRepo = paymentRepository;
	}
	
	@Transactional(readOnly = true)
	public Iterable<Payment> findAll() throws NoSuchElementException{
		Iterable<Payment> result =  this.paymentRepo.findAll();
		if (StreamSupport.stream(result.spliterator(), false).count()==0) {
			throw new NoSuchElementException();
		}
		return result;
	}

	@Transactional(readOnly = true)
	public Optional<Payment> findPaymentById(final int paymentId) throws NoSuchElementException{
		Optional<Payment> result = this.paymentRepo.findById(paymentId);
		result.get();
		return result;
	}

}
