package com.shivampoonia.reeldock.service;

import com.shivampoonia.reeldock.dto.ProductionRequest;
import com.shivampoonia.reeldock.dto.Views.ProductionView;
import com.shivampoonia.reeldock.exception.NotFoundException;
import com.shivampoonia.reeldock.exception.RuleBreachException;
import com.shivampoonia.reeldock.model.CutStatus;
import com.shivampoonia.reeldock.model.Kit;
import com.shivampoonia.reeldock.model.Production;
import com.shivampoonia.reeldock.repository.ProductionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ProductionService {

    private final ProductionRepository productions;
    private final MemberService members;
    private final KitService kits;

    public ProductionService(ProductionRepository productions, MemberService members, KitService kits) {
        this.productions = productions;
        this.members = members;
        this.kits = kits;
    }

    @Transactional(readOnly = true)
    public List<ProductionView> list() {
        return productions.allFetched().stream().map(ProductionView::of).toList();
    }

    @Transactional(readOnly = true)
    public ProductionView one(Long id) {
        return ProductionView.of(productions.findFetched(id)
                .orElseThrow(() -> new NotFoundException("Production " + id + " not found")));
    }

    public ProductionView create(ProductionRequest req) {
        if (productions.findBySlug(req.getSlug()).isPresent()) {
            throw new RuleBreachException("Slug already in the catalog");
        }
        Production p = new Production();
        apply(p, req);
        return ProductionView.of(productions.save(p));
    }

    public ProductionView rewrite(Long id, ProductionRequest req) {
        Production p = entity(id);
        if (!p.getSlug().equals(req.getSlug()) && productions.findBySlug(req.getSlug()).isPresent()) {
            throw new RuleBreachException("Slug already in the catalog");
        }
        apply(p, req);
        return ProductionView.of(productions.save(p));
    }

    public void erase(Long id) {
        productions.delete(entity(id));
    }

    public Production entity(Long id) {
        return productions.findById(id).orElseThrow(() -> new NotFoundException("Production " + id + " not found"));
    }

    private void apply(Production p, ProductionRequest req) {
        p.setSlug(req.getSlug());
        p.setTitle(req.getTitle());
        p.setKind(req.getKind());
        p.setOwner(members.entity(req.getOwnerId()));
        p.setRuntimeSec(req.getRuntimeSec());
        p.setCutStatus(req.getCutStatus() == null ? CutStatus.DRAFT : req.getCutStatus());
        Map<String, Object> payload = req.getPayload() == null ? new LinkedHashMap<>() : req.getPayload();
        p.setPayload(payload);
        if (req.getKitId() == null) {
            p.setKit(null);
        } else {
            Kit kit = kits.entity(req.getKitId());
            p.setKit(kit);
        }
    }
}
