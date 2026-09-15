package com.shivampoonia.reeldock.dto;

import com.shivampoonia.reeldock.model.CutStatus;
import com.shivampoonia.reeldock.model.Kit;
import com.shivampoonia.reeldock.model.KitKind;
import com.shivampoonia.reeldock.model.KitStatus;
import com.shivampoonia.reeldock.model.Loan;
import com.shivampoonia.reeldock.model.LoanStatus;
import com.shivampoonia.reeldock.model.Member;
import com.shivampoonia.reeldock.model.Production;
import com.shivampoonia.reeldock.model.ProductionKind;
import com.shivampoonia.reeldock.model.Review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public final class Views {
    private Views() {}

    public record MemberView(Long id, String campusId, String displayName, String mail, String programme, String cohort) {
        public static MemberView of(Member m) {
            return new MemberView(m.getId(), m.getCampusId(), m.getDisplayName(), m.getMail(), m.getProgramme(), m.getCohort());
        }
    }

    public record KitView(Long id, String tag, String label, KitKind kind, String serial, String bay,
                          int dayRateCents, KitStatus status) {
        public static KitView of(Kit k) {
            return new KitView(k.getId(), k.getTag(), k.getLabel(), k.getKind(), k.getSerial(),
                    k.getBay(), k.getDayRateCents(), k.getStatus());
        }
    }

    public record LoanView(Long id, Long memberId, String campusId, Long kitId, String kitTag,
                           LocalDateTime takenAt, LocalDateTime dueAt, LocalDateTime returnedAt,
                           String lockerPin, LoanStatus status, String note) {
        public static LoanView of(Loan l) {
            return new LoanView(
                    l.getId(),
                    l.getMember().getId(), l.getMember().getCampusId(),
                    l.getKit().getId(), l.getKit().getTag(),
                    l.getTakenAt(), l.getDueAt(), l.getReturnedAt(),
                    l.getLockerPin(), l.getStatus(), l.getNote()
            );
        }
    }

    public record ProductionView(Long id, String slug, String title, ProductionKind kind,
                                 Long ownerId, String campusId, Long kitId, String kitTag,
                                 int runtimeSec, CutStatus cutStatus, Map<String, Object> payload) {
        public static ProductionView of(Production p) {
            return new ProductionView(
                    p.getId(), p.getSlug(), p.getTitle(), p.getKind(),
                    p.getOwner().getId(), p.getOwner().getCampusId(),
                    p.getKit() == null ? null : p.getKit().getId(),
                    p.getKit() == null ? null : p.getKit().getTag(),
                    p.getRuntimeSec(), p.getCutStatus(), p.getPayload()
            );
        }
    }

    public record ReviewView(Long id, Long productionId, String slug, Long memberId, String campusId,
                             int stars, String body, LocalDateTime postedAt) {
        public static ReviewView of(Review r) {
            return new ReviewView(
                    r.getId(),
                    r.getProduction().getId(), r.getProduction().getSlug(),
                    r.getMember().getId(), r.getMember().getCampusId(),
                    r.getStars(), r.getBody(), r.getPostedAt()
            );
        }
    }

    public record DeskView(MemberView member, List<LoanView> loans, List<ProductionView> productions,
                           List<ReviewView> reviews) {}

    public record KindRating(String kind, double avgStars, long reviewCount) {}
    public record CodecCount(String codec, long n) {}
    public record KitLoad(String kind, String status, long n) {}
}
