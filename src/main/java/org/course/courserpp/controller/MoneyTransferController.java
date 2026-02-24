package org.course.courserpp.controller;

import lombok.RequiredArgsConstructor;
import org.course.courserpp.model.MoneyTransfer;
import org.course.courserpp.service.MoneyTransferService;
import org.course.courserpp.service.ScoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/money-transfers")
@RequiredArgsConstructor
public class MoneyTransferController {

    private final MoneyTransferService moneyTransferService;
    private final ScoreService scoreService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("moneyTransfers", moneyTransferService.findAll());
        return "money-transfer/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("moneyTransfer", new MoneyTransfer());
        model.addAttribute("scores", scoreService.findAll());
        return "money-transfer/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam Long sum,
                       @RequestParam(required = false) List<String> scoreIds) {
        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setSum(sum);
        moneyTransferService.save(moneyTransfer);

        if (scoreIds != null && !scoreIds.isEmpty()) {
            moneyTransferService.linkToScores(moneyTransfer.getId(), scoreIds);
        }

        return "redirect:/money-transfers";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        moneyTransferService.delete(id);
        return "redirect:/money-transfers";
    }
}
