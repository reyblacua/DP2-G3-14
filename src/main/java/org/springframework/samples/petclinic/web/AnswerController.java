
package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Announcement;
import org.springframework.samples.petclinic.model.Answer;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.AnnouncementService;
import org.springframework.samples.petclinic.service.AnswerService;
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
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/announcements/{announcementId}")
public class AnswerController {

	private AnswerService		answerService;

	private AnnouncementService	announcementService;

	private OwnerService		ownerService;

	private PetService			petService;


	@Autowired
	private AnswerController(final AnswerService answerService, final AnnouncementService announcementService, final OwnerService ownerService, final PetService petService) {
		this.announcementService = announcementService;
		this.answerService = answerService;
		this.ownerService = ownerService;
		this.petService = petService;
	}

	@ModelAttribute("announcement")
	public Announcement findAnnouncement(@PathVariable("announcementId") final int announcementId) {
		return this.announcementService.findAnnouncementById(announcementId).get();
	}

	@GetMapping(path = "/answer/new")
	public String createAnswer(@PathVariable("announcementId") final int announcementId, final ModelMap modelMap) {
		Announcement announcement = null;
		Owner owner = null;
		List<Answer> answers = new ArrayList<Answer>();
		List<Pet> pets = new ArrayList<Pet>();
		String view = "/exception";

		try {
			Optional<Announcement> opt = this.announcementService.findAnnouncementById(announcementId);
			announcement = opt.get();
		} catch (NoSuchElementException e) {
			modelMap.addAttribute("message", "There are errors validating data");
			return view;
		}

		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			owner = this.ownerService.findOwnerByUserName(auth.getName());
		} catch (NoSuchElementException e) {
		}

		try {
			answers = this.answerService.findAnswerByOwner(owner);
		} catch (NoSuchElementException e) {
		}

		try {
			pets = (List<Pet>) this.petService.findPets(owner.getUser().getUsername());
		} catch (NoSuchElementException e) {
		}

		if (announcement.getOwner().equals(owner)) {
			modelMap.addAttribute("message", "You can't answer your own announcement");
			return view;
		}

		if (!announcement.getCanBeAdopted()) {
			modelMap.addAttribute("message", "You can't adopt this pet because it can't be adopted");
			return view;
		}

		if (!owner.getPositiveHistory()) {
			modelMap.addAttribute("message", "You can't adopt a pet if you have a bad history");
			return view;
		}

		for (Answer answerIni : answers) {
			if (answerIni.getAnnouncement().equals(announcement)) {
				modelMap.addAttribute("message", "You can't send more than one answer to the same announcement");
				return view;
			}
		}

		for (int i = 0; i < pets.size(); i++) {
			if (pets.get(i).getIsVaccinated() != true) {
				modelMap.addAttribute("message", "You can't send an answer if any of your pets aren't vaccinated");
				return view;
			}
		}

		view = "answers/editAnswer";
		Answer answer = new Answer();
		answer.setAnnouncement(announcement);
		modelMap.addAttribute("answer", answer);
		return view;

	}

	@PostMapping(value = "/answer/new")
	public String processCreationForm(@PathVariable("announcementId") final int announcementId, @Valid final Answer answer, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("answer", answer);
			return "answers/editAnswer";
		} else {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			try {
				Announcement announcement = this.announcementService.findAnnouncementById(announcementId).get();
				answer.setAnnouncement(announcement);
				answer.setOwner(this.ownerService.findOwnerByUserName(auth.getName()));
				this.answerService.saveAnswer(answer);
			} catch (Exception e) {
				model.addAttribute("message", e.getMessage());
				return "/exception";
			}
			return "redirect:/announcements/{announcementId}";
		}
	}

	@GetMapping("/answers")
	public String mostrarAnwers(final ModelMap modelMap, @PathVariable("announcementId") final Integer announcementId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String vista = "answers/answersList";
		boolean isempty = false;
		try {
			if (!authentication.getName().equals(this.announcementService.findAnnouncementById(announcementId).get().getOwner().getUser().getUsername())) {
				modelMap.addAttribute("message", "You cannot access another user's announcement answers");
				return "exception";
			} else {
				Optional<Announcement> announcement = this.announcementService.findAnnouncementById(announcementId);
				Iterable<Answer> answers = this.answerService.findAnswerByAnnouncement(announcement.get());
				modelMap.addAttribute("answers", answers);
			}
		} catch (NoSuchElementException e) {
			isempty = true;
			modelMap.addAttribute("isempty", isempty);
		}
		return vista;

	}
}
