package org.course.courserpp.controller;

import lombok.RequiredArgsConstructor;
import org.course.courserpp.model.CashWithdrawal;
import org.course.courserpp.service.ApplicationForCashService;
import org.course.courserpp.service.CashWithdrawalService;
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
@RequestMapping("/cash-withdrawals")
@RequiredArgsConstructor
public class CashWithdrawalController {

    private final CashWithdrawalService cashWithdrawalService;
    private final ScoreService scoreService;
    private final ApplicationForCashService applicationForCashService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("cashWithdrawals", cashWithdrawalService.findAll());
        return "cash-withdrawal/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("cashWithdrawal", new CashWithdrawal());
        model.addAttribute("scores", scoreService.findAll());
        model.addAttribute("applications", applicationForCashService.findAll());
        return "cash-withdrawal/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam Long withdrawalAmount,
                       @RequestParam(required = false) List<String> scoreIds,
                       @RequestParam(required = false) String applicationId) {
        CashWithdrawal cashWithdrawal = new CashWithdrawal();
        cashWithdrawal.setWithdrawalAmount(withdrawalAmount);

        cashWithdrawalService.save(cashWithdrawal);

        if (applicationId != null && !applicationId.isBlank()) {
            cashWithdrawalService.linkToApplication(cashWithdrawal.getId(), applicationId);
        }

        if (scoreIds != null && !scoreIds.isEmpty()) {
            cashWithdrawalService.linkToScores(cashWithdrawal.getId(), scoreIds);
        }

        return "redirect:/cash-withdrawals";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute("cashWithdrawal", cashWithdrawalService.findById(id));
        model.addAttribute("scores", scoreService.findAll());
        model.addAttribute("applications", applicationForCashService.findAll());
        return "cash-withdrawal/form";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable String id,
                         @RequestParam Long withdrawalAmount,
                         @RequestParam(required = false) List<String> scoreIds,
                         @RequestParam(required = false) String applicationId) {
        CashWithdrawal cw = cashWithdrawalService.findById(id);
        cw.setWithdrawalAmount(withdrawalAmount);
        cashWithdrawalService.save(cw);
        if (applicationId != null && !applicationId.isBlank()) {
            cashWithdrawalService.linkToApplication(id, applicationId);
        }
        if (scoreIds != null && !scoreIds.isEmpty()) {
            cashWithdrawalService.linkToScores(id, scoreIds);
        }
        return "redirect:/cash-withdrawals";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        cashWithdrawalService.delete(id);
        return "redirect:/cash-withdrawals";
    }
}
