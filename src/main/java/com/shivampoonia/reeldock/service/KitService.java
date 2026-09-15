package com.shivampoonia.reeldock.service;

import com.shivampoonia.reeldock.dto.KitRequest;
import com.shivampoonia.reeldock.dto.Views.KitView;
import com.shivampoonia.reeldock.exception.NotFoundException;
import com.shivampoonia.reeldock.exception.RuleBreachException;
import com.shivampoonia.reeldock.model.Kit;
import com.shivampoonia.reeldock.model.KitKind;
import com.shivampoonia.reeldock.model.KitStatus;
import com.shivampoonia.reeldock.repository.KitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class KitService {

    private final KitRepository kits;

    public KitService(KitRepository kits) {
        this.kits = kits;
    }

    @Transactional(readOnly = true)
    public List<KitView> list() {
        return kits.findAll().stream().map(KitView::of).toList();
    }

    @Transactional(readOnly = true)
    public KitView one(Long id) {
        return KitView.of(entity(id));
    }

    @Transactional(readOnly = true)
    public List<KitView> ready(KitKind kind) {
        return kits.findByKindAndStatus(kind, KitStatus.READY).stream().map(KitView::of).toList();
    }

    public KitView create(KitRequest req) {
        Kit k = new Kit();
        apply(k, req);
        try {
            return KitView.of(kits.save(k));
        } catch (RuntimeException ex) {
            throw new RuleBreachException("Kit tag or serial already on the cage wall");
        }
    }

    public KitView rewrite(Long id, KitRequest req) {
        Kit k = entity(id);
        apply(k, req);
        return KitView.of(kits.save(k));
    }

    public void erase(Long id) {
        kits.delete(entity(id));
    }

    public Kit entity(Long id) {
        return kits.findById(id).orElseThrow(() -> new NotFoundException("Kit " + id + " not found"));
    }

    private void apply(Kit k, KitRequest req) {
        k.setTag(req.getTag());
        k.setLabel(req.getLabel());
        k.setKind(req.getKind());
        k.setSerial(req.getSerial());
        k.setBay(req.getBay());
        k.setDayRateCents(req.getDayRateCents());
        k.setStatus(req.getStatus() == null ? KitStatus.READY : req.getStatus());
    }
}
