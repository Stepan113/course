package org.course.courserpp.controller;

import lombok.RequiredArgsConstructor;
import org.course.courserpp.model.ApplicationForCash;
import org.course.courserpp.service.ApplicationForCashService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationForCashController {

    private final ApplicationForCashService applicationForCashService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("applications", applicationForCashService.findAll());
        return "application/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("application", new ApplicationForCash());
        return "application/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute ApplicationForCash application) {
        applicationForCashService.save(application);
        return "redirect:/applications";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        applicationForCashService.delete(id);
        return "redirect:/applications";
    }
}
