
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "hairdressers")
public class Hairdresser extends Person {

	@Column(name = "specialties")
	@NotNull
	private HairdresserSpecialty	specialties;

	@Column(name = "active")
	@NotNull
	private Boolean					active;

}
