package com.shivampoonia.reeldock.controller;

import com.shivampoonia.reeldock.dto.KitRequest;
import com.shivampoonia.reeldock.dto.Views.KitView;
import com.shivampoonia.reeldock.model.KitKind;
import com.shivampoonia.reeldock.service.KitService;
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
@RequestMapping("/api/v1/kits")
public class KitController {

    private final KitService kits;

    public KitController(KitService kits) {
        this.kits = kits;
    }

    @GetMapping
    public List<KitView> list() { return kits.list(); }

    @GetMapping("/{id}")
    public KitView one(@PathVariable Long id) { return kits.one(id); }

    @GetMapping("/ready/{kind}")
    public List<KitView> ready(@PathVariable KitKind kind) { return kits.ready(kind); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public KitView create(@Valid @RequestBody KitRequest req) { return kits.create(req); }

    @PutMapping("/{id}")
    public KitView rewrite(@PathVariable Long id, @Valid @RequestBody KitRequest req) {
        return kits.rewrite(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void erase(@PathVariable Long id) { kits.erase(id); }
}
