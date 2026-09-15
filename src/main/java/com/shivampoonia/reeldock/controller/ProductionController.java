package com.shivampoonia.reeldock.controller;

import com.shivampoonia.reeldock.dto.ProductionRequest;
import com.shivampoonia.reeldock.dto.Views.ProductionView;
import com.shivampoonia.reeldock.service.ProductionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productions")
public class ProductionController {

    private final ProductionService productions;

    public ProductionController(ProductionService productions) {
        this.productions = productions;
    }

    @GetMapping
    public List<ProductionView> list() { return productions.list(); }

    @GetMapping("/{id}")
    public ProductionView one(@PathVariable Long id) { return productions.one(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductionView create(@Valid @RequestBody ProductionRequest req) { return productions.create(req); }

    @PutMapping("/{id}")
    public ProductionView rewrite(@PathVariable Long id, @Valid @RequestBody ProductionRequest req) {
        return productions.rewrite(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void erase(@PathVariable Long id) { productions.erase(id); }
}
