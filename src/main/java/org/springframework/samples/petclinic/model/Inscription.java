
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class Inscription extends NamedEntity {

	@Past
	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	date;

	@NotNull
	private Boolean		isPaid;

	@OneToOne
	private Payment		payment;

	@NotNull
	@ManyToOne
	private Pet			pet;

	@ManyToOne
	private Owner		owner;

	@OneToOne
	private Course		course;
}
