
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
public class Course extends NamedEntity {

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	startDate;

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	finishDate;

	@ManyToOne
	private PetType		petType;

	private Boolean		dangerousAllowed;

	private Integer		capacity;

	@ManyToOne
	private Trainer		trainer;

	private Integer		cost;


}
