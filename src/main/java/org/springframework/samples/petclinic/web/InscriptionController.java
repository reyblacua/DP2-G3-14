
package org.springframework.samples.petclinic.web;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.StreamSupport;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Course;
import org.springframework.samples.petclinic.model.Inscription;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.CourseService;
import org.springframework.samples.petclinic.service.InscriptionsService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class InscriptionController {

	@Autowired
	private InscriptionsService	inscriptionService;

	@Autowired
	private PetService			petService;

	@Autowired
	private CourseService		courseService;

	@Autowired
	private OwnerService		ownerService;


	@GetMapping("/inscriptions")
	public String mostrarInscriptions(final ModelMap modelMap) {
		String vista = "inscriptions/inscriptionsList";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		Owner owner = this.ownerService.findOwnerByUserName(userName);
		boolean isempty = false;
		try {
			Iterable<Inscription> inscriptions = this.inscriptionService.findInscriptionsByOwner(owner);
			modelMap.addAttribute("inscriptions", inscriptions);
		} catch (NoSuchElementException e) {
			isempty = true;
			modelMap.addAttribute("isempty", isempty);
		}
		return vista;
	}

	@GetMapping("/inscriptions/{inscriptionId}")
	public String mostrarInscription(final ModelMap modelMap, @PathVariable("inscriptionId") final int inscriptionId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<Inscription> inscription = null;

		try {
			inscription = this.inscriptionService.findInscriptionById(inscriptionId);
			if (!authentication.getName().equals(inscription.get().getOwner().getUser().getUsername())) {
				modelMap.addAttribute("message", "You cannot access another user's inscription details");
				return "exception";
			} else {
				String vista = "inscriptions/inscriptionDetails";
				modelMap.addAttribute("inscription", inscription.get());
				return vista;
			}
		} catch (NoSuchElementException e) {
			modelMap.addAttribute("message", "Inscription not found");
			return "exception";
		}
	}

	@GetMapping(path = "courses/{courseId}/inscription/new")
	public String createInscription(final ModelMap modelMap, @PathVariable("courseId") final int courseId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Course course = null;
		Iterable<Inscription> inscriptionsByOwner = null;
		Iterable<Inscription> inscriptionsByCourse = null;
		Iterable<Pet> pets = null;

		try {
			inscriptionsByOwner = this.inscriptionService.findInscriptionsByOwner(this.ownerService.findOwnerByUserName(authentication.getName()));
		} catch (NoSuchElementException e) {
		}

		try {
			pets = this.petService.findPets(authentication.getName());
		} catch (NoSuchElementException e) {
		}

		try {
			course = this.courseService.findCourseById(courseId).get();
		} catch (NoSuchElementException e) {
			modelMap.addAttribute("message", "There are errors validating data");
			return "exception";
		}

		try {
			inscriptionsByCourse = this.inscriptionService.findInscriptionsByCourse(course);
		} catch (NoSuchElementException e) {
		}

		// --------- Validación de reglas de negocio ---------

		//Owner no puede crear una inscripción si no tiene mascotas
		int numeroPets = 0;
		if (pets != null) {
			numeroPets = (int) StreamSupport.stream(pets.spliterator(), false).count();
		}
		if (numeroPets == 0) {
			modelMap.addAttribute("message", "You have no pets to sign up in a course");
			return "exception";
		}

		//Owner no puede crear una inscripción si no ha pagado las anteriores
		if (inscriptionsByOwner != null && StreamSupport.stream(inscriptionsByOwner.spliterator(), false).count() != 0) {
			for (Inscription inscriptionit : inscriptionsByOwner) {
				if (inscriptionit.getIsPaid() != true) {
					modelMap.addAttribute("message", "You have to pay previous courses inscriptions");
					return "exception";
				}
			}
		}

		//No puede apuntarse a una mascota a un curso lleno
		int suma = 0;
		if (inscriptionsByCourse != null) {
			suma = (int) StreamSupport.stream(inscriptionsByCourse.spliterator(), false).count();
		}
		if (suma >= course.getCapacity()) {
			modelMap.addAttribute("message", "The course is full");
			return "exception";
		}

		String view = "inscriptions/editInscription";
		Inscription inscription = new Inscription();
		inscription.setCourse(course);
		modelMap.addAttribute("inscription", inscription);
		modelMap.addAttribute("course", course);
		return view;
	}

	@PostMapping(path = "courses/{courseId}/inscription/new")
	public String saveInscription(@PathVariable("courseId") final int courseId, @Valid final Inscription inscription, final BindingResult result, final ModelMap modelMap) {

		String view = "redirect:/inscriptions";
		if (result.hasErrors()) {
			modelMap.addAttribute("course", this.courseService.findCourseById(courseId).get());
			modelMap.put("inscription", inscription);
			return "inscriptions/editInscription";
		} else {
			inscription.setCourse(this.courseService.findCourseById(courseId).get());
			inscription.setOwner(inscription.getPet().getOwner());
			try {
				this.inscriptionService.saveInscription(inscription);
			} catch (Exception e) {
				modelMap.addAttribute("message", "Error: " + e.getMessage());
				return "exception";
			}
			modelMap.addAttribute("message", "Inscription successfully saved!");
		}
		return view;
	}

	@ModelAttribute("pets")
	public Iterable<Pet> populatePet() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		return this.petService.findPets(userName);
	}

	@GetMapping(path = "/inscriptions/delete/{inscriptionId}")
	public String deleteInscription(@PathVariable("inscriptionId") final Integer inscriptionId, final ModelMap modelMap) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String view = "redirect:/inscriptions";
		Optional<Inscription> inscription = null;
		try {
			inscription = this.inscriptionService.findInscriptionById(inscriptionId);
			if (!authentication.getName().equals(this.inscriptionService.findInscriptionById(inscriptionId).get().getOwner().getUser().getUsername())) {
				modelMap.addAttribute("message", "You cannot delete another user's inscription details");
				return "exception";
			} else {
				this.inscriptionService.deleteInscription(inscription.get());
				modelMap.addAttribute("message", "Inscription successfully deleted");
			}
		} catch (NoSuchElementException e) {
			modelMap.addAttribute("message", "Inscription not found");
			return "exception";
		}
		return view;
	}

}
