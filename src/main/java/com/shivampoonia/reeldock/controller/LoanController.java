package com.shivampoonia.reeldock.controller;

import com.shivampoonia.reeldock.dto.LoanRequest;
import com.shivampoonia.reeldock.dto.Views.LoanView;
import com.shivampoonia.reeldock.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {

    private final LoanService loans;

    public LoanController(LoanService loans) {
        this.loans = loans;
    }

    @GetMapping
    public List<LoanView> list() { return loans.list(); }

    @GetMapping("/{id}")
    public LoanView one(@PathVariable Long id) { return loans.one(id); }

    @GetMapping("/overdue")
    public List<LoanView> overdue() { return loans.overdue(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanView checkout(@Valid @RequestBody LoanRequest req) { return loans.checkout(req); }

    @PostMapping("/{id}/return")
    public LoanView giveBack(@PathVariable Long id) { return loans.giveBack(id); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void erase(@PathVariable Long id) { loans.erase(id); }
}
