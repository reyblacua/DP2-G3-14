
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
public class Announcement extends NamedEntity {

	@NotBlank
	private String	petName;

	@Size(min = 3, max = 200)
	private String	description;

	@NotNull
	private Boolean	canBeAdopted;

	@NotNull
	@ManyToOne
	private PetType	type;

	@ManyToOne
	private Owner	owner;

}
