
package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Payment;
import org.springframework.samples.petclinic.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payments")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;


	@GetMapping()
	public String mostrarPayments(final ModelMap modelMap) {
		String vista = "appointments/appointmentsList";
		Iterable<Payment> payments = this.paymentService.findAll();
		modelMap.addAttribute("payments", payments);
		return vista;
	}

	@GetMapping("/{appointmentId}")
	public String mostrarPayment(final ModelMap modelMap, @PathVariable("paymentId") final int paymentId) {
		String vista = "appointments/appointmentDetails";
		Payment payment = this.paymentService.findPaymentById(paymentId).get();
		modelMap.addAttribute("payment", payment);
		return vista;
	}

}
