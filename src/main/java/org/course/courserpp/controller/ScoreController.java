package org.course.courserpp.controller;

import lombok.RequiredArgsConstructor;
import org.course.courserpp.model.Score;
import org.course.courserpp.service.ScoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/scores")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("scores", scoreService.findAll());
        return "score/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("score", new Score());
        return "score/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Score score) {
        scoreService.save(score);
        return "redirect:/scores";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute("score", scoreService.findById(id));
        return "score/form";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable String id, @ModelAttribute Score score) {
        score.setId(id);
        scoreService.save(score);
        return "redirect:/scores";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        scoreService.delete(id);
        return "redirect:/scores";
    }
}
