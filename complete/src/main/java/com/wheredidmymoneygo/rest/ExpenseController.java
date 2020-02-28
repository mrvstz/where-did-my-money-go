package com.wheredidmymoneygo.rest;

import com.wheredidmymoneygo.dto.Expense;
import com.wheredidmymoneygo.dto.ExpenseStatitic;
import com.wheredidmymoneygo.model.ExpenseModel;
import com.wheredidmymoneygo.service.ExpenseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@AllArgsConstructor
@RequestMapping("/expense")
public class ExpenseController {

    private ExpenseService expenseService;

    @PostMapping()
    public ResponseEntity addExpense(@Valid @RequestBody Expense expense) {
        return expenseService.saveExpense(expense);
    }

    @GetMapping("/allExpenses")
    public List<ExpenseModel> getExpenses() {
        return expenseService.getAllExpenses();
    }

    @GetMapping("")
    public List<ExpenseModel> getExpensesInTimespan(@RequestParam Instant from, @RequestParam Instant till) {
        return expenseService.getExpensesModelInTimespan(from, till);
    }

    @GetMapping("/statistic")
    public ExpenseStatitic getExpensesStatistic(@RequestParam Instant from, @RequestParam Instant till) {
        return expenseService.getExpensesStatitic(from, till);
    }

}
