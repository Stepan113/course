package org.course.courserpp.controller;

import lombok.RequiredArgsConstructor;
import org.course.courserpp.model.ReplenishmentOperation;
import org.course.courserpp.service.ReplenishmentOperationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/replenishments")
@RequiredArgsConstructor
public class ReplenishmentOperationController {

    private final ReplenishmentOperationService replenishmentOperationService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("replenishments", replenishmentOperationService.findAll());
        return "replenishment/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("replenishment", new ReplenishmentOperation());
        return "replenishment/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute ReplenishmentOperation replenishment) {
        replenishmentOperationService.save(replenishment);
        return "redirect:/replenishments";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute("replenishment", replenishmentOperationService.findById(id));
        return "replenishment/form";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable String id, @ModelAttribute ReplenishmentOperation replenishment) {
        replenishment.setId(id);
        replenishmentOperationService.save(replenishment);
        return "redirect:/replenishments";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        replenishmentOperationService.delete(id);
        return "redirect:/replenishments";
    }
}
