
package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Hairdresser;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class HairdresserServiceTests {

	@Autowired
	protected HairdresserService hairdresserService;


	@Test
	void shouldFindHairdressers() {
		Collection<Hairdresser> hairdressers = (Collection<Hairdresser>) this.hairdresserService.findAll();
		int count = hairdressers.size();
		org.assertj.core.api.Assertions.assertThat(hairdressers).hasSize(count);
	}

	@ParameterizedTest

	@CsvSource({
		"1,Primero,true", "2,Segundo,true", "4,Cuarto,false"
	})
	void shouldFindHairdresser(final int id, final String lastName, final Boolean active) {
		Optional<Hairdresser> hairdresser = this.hairdresserService.findHairdresserById(id);
		org.assertj.core.api.Assertions.assertThat(hairdresser).isPresent();
		org.assertj.core.api.Assertions.assertThat(hairdresser.get().getLastName()).isEqualTo(lastName);
		org.assertj.core.api.Assertions.assertThat(hairdresser.get().getActive()).isEqualTo(active);

	}

	@ParameterizedTest

	@CsvSource({
		"200", "-1", "0"
	})
	void shouldNotFindHairdressers(final int id) {
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.hairdresserService.findHairdresserById(id);
		});

	}

}
