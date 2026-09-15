package com.shivampoonia.reeldock.controller;

import com.shivampoonia.reeldock.dto.MemberRequest;
import com.shivampoonia.reeldock.dto.Views.MemberView;
import com.shivampoonia.reeldock.service.MemberService;
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
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService members;

    public MemberController(MemberService members) {
        this.members = members;
    }

    @GetMapping
    public List<MemberView> list() { return members.list(); }

    @GetMapping("/{id}")
    public MemberView one(@PathVariable Long id) { return members.one(id); }

    @GetMapping("/campus/{campusId}")
    public MemberView byCampus(@PathVariable String campusId) { return members.byCampus(campusId); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberView create(@Valid @RequestBody MemberRequest req) { return members.create(req); }

    @PutMapping("/{id}")
    public MemberView rewrite(@PathVariable Long id, @Valid @RequestBody MemberRequest req) {
        return members.rewrite(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void erase(@PathVariable Long id) { members.erase(id); }
}
