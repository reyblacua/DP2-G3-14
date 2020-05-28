
package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "appointments")
public class Appointment extends NamedEntity {

	@Column(name = "description")
	@Size(max = 512)
	private String			description;

	@Column(name = "datetime")
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	@NotNull
	@Future
	private LocalDateTime	date;

	@Column(name = "is_paid")
	@NotNull
	private Boolean			isPaid;

	@JoinColumn(name = "hairdresser_id")
	@ManyToOne
	private Hairdresser		hairdresser;

	@JoinColumn(name = "pet_id")
	@NotNull
	@ManyToOne
	private Pet				pet;

	@JoinColumn(name = "payment_id")
	@OneToOne(optional = true)
	private Payment			payment;

	@JoinColumn(name = "owner_id")
	@ManyToOne
	private Owner			owner;

}
