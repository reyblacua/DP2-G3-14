
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class Answer extends NamedEntity {

	@Past
	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate		date;

	@Size(min = 3, max = 200)
	private String			description;

	@ManyToOne
	private Announcement	announcement;

	@ManyToOne
	private Owner			owner;
}
