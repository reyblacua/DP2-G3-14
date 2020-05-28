
package org.springframework.samples.petclinic.web;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Hairdresser;
import org.springframework.samples.petclinic.service.HairdresserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hairdressers")
public class HairdresserController {

	@Autowired
	private HairdresserService hairdresserService;


	@GetMapping()
	public String mostrarHairdressers(final ModelMap modelMap) {
		String vista = "hairdressers/hairdressersList";
		boolean isempty = false;

		try {
			Iterable<Hairdresser> hairdressers = this.hairdresserService.findAll();
			modelMap.addAttribute("hairdressers", hairdressers);
		} catch (NoSuchElementException e) {
			isempty = true;
			modelMap.addAttribute("isempty", isempty);
		}
		return vista;
	}

	@GetMapping("/{hairdresserId}")
	public String mostrarHairdresser(final ModelMap modelMap, @PathVariable("hairdresserId") final int hairdresserId) {
		Hairdresser hairdresser = null;

		try {
			String vista = "hairdressers/hairdresserDetails";
			hairdresser = this.hairdresserService.findHairdresserById(hairdresserId).get();
			modelMap.addAttribute("hairdresser", hairdresser);
			return vista;
		} catch (NoSuchElementException e) {
			modelMap.addAttribute("message", "Hairdresser not found");
			return "exception";
		}

	}

}
