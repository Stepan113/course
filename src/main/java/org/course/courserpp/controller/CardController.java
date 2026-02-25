package org.course.courserpp.controller;

import lombok.RequiredArgsConstructor;
import org.course.courserpp.model.Card;
import org.course.courserpp.service.CardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("cards", cardService.findAll());
        return "card/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("card", new Card());
        return "card/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Card card) {
        cardService.save(card);
        return "redirect:/cards";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute("card", cardService.findById(id));
        return "card/form";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable String id, @ModelAttribute Card card) {
        card.setId(id);
        cardService.save(card);
        return "redirect:/cards";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        cardService.delete(id);
        return "redirect:/cards";
    }
}
