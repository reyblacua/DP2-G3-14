
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "payments")
public class Payment extends NamedEntity {

	@Column(name = "amount")
	@Range(min = 1, max = 9999)
	@NotNull
	private Double		amount;

	@Column(name = "pay_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	date;

}
