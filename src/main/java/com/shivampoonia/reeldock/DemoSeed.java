package com.shivampoonia.reeldock;

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
import com.shivampoonia.reeldock.repository.KitRepository;
import com.shivampoonia.reeldock.repository.LoanRepository;
import com.shivampoonia.reeldock.repository.MemberRepository;
import com.shivampoonia.reeldock.repository.ProductionRepository;
import com.shivampoonia.reeldock.repository.ReviewRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Component
public class DemoSeed implements CommandLineRunner {

    private static final String[] FIRST = {
            "Mira", "Jonas", "Leila", "Omar", "Nina", "Pavel", "Asha", "Luca", "Greta", "Farid",
            "Ines", "Yusuf", "Sofia", "Mateo", "Hana", "Ravi", "Elena", "Nico", "Priya", "Tomas"
    };
    private static final String[] LAST = {
            "Okonkwo", "Keller", "Rahman", "Varga", "Berg", "Novak", "Diallo", "Costa", "Hoffmann", "Said",
            "Petrov", "Nakamura", "Silva", "Kaur", "Nowak", "Ibrahim", "Larsen", "Moreau", "Singh", "Weber"
    };
    private static final String[] PROGRAMMES = {
            "MSc Management AI", "MSc Digital Media", "BA Film", "MSc Data", "MA Journalism"
    };
    private static final String[] COHORTS = {"2025.1", "2025.3", "2026.1", "2026.3"};
    private static final String[] BAYS = {
            "Colour Bay", "Grip Cage", "Sound Closet", "Drone Cage", "Edit Bay A", "Lighting Grid"
    };
    private static final String[] CODECS = {"ProRes", "DNxHR", "H.265", "WAV", "EXR"};
    private static final String[] TITLES = {
            "Potsdam Night Cut", "Havel Fog Study", "Media Wing Loop", "Tram Window", "Bay 4 Hum",
            "VR Lobby Test", "Podcast Desk", "Rain on Glass", "Campus Drone Pass", "Quiet Hours"
    };

    private final MemberRepository members;
    private final KitRepository kits;
    private final LoanRepository loans;
    private final ProductionRepository productions;
    private final ReviewRepository reviews;

    public DemoSeed(MemberRepository members, KitRepository kits, LoanRepository loans,
                    ProductionRepository productions, ReviewRepository reviews) {
        this.members = members;
        this.kits = kits;
        this.loans = loans;
        this.productions = productions;
        this.reviews = reviews;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (members.count() >= 100) {
            return;
        }
        Random rng = new Random(605);
        List<Member> people = seedMembers();
        List<Kit> cage = seedKits();
        seedLoans(people, cage, rng);
        List<Production> cuts = seedProductions(people, cage, rng);
        seedReviews(people, cuts, rng);
    }

    private List<Member> seedMembers() {
        List<Member> batch = new ArrayList<>();
        batch.add(member("GH1061529", "Shivam", "Shivam2026.3@gisma-student.com",
                "MSc Management AI", "2026.3"));
        for (int i = 1; i < 120; i++) {
            String campus = String.format("GH%07d", 2000000 + i);
            String first = FIRST[i % FIRST.length];
            String last = LAST[(i * 3) % LAST.length];
            String mail = (first + "." + last + i + "@student.gisma.edu").toLowerCase();
            batch.add(member(campus, first + " " + last, mail,
                    PROGRAMMES[i % PROGRAMMES.length], COHORTS[i % COHORTS.length]));
        }
        return members.saveAll(batch);
    }

    private List<Kit> seedKits() {
        List<Kit> batch = new ArrayList<>();
        KitKind[] kinds = KitKind.values();
        String[] labels = {
                "Sony FX3 body", "Sennheiser boom", "Aputure 60x", "C-stand kit", "DJI Mini cage",
                "Canon R6 body", "Zoom F6", "Nanlite FS-150", "Shoulder rig", "Insta360 rig"
        };
        for (int i = 0; i < 120; i++) {
            Kit k = new Kit();
            KitKind kind = kinds[i % kinds.length];
            k.setTag(String.format("MW-%s-%03d", kind.name().substring(0, 3), i + 1));
            k.setLabel(labels[i % labels.length] + " #" + (i + 1));
            k.setKind(kind);
            k.setSerial("SN-MW-" + (88000 + i));
            k.setBay(BAYS[i % BAYS.length]);
            k.setDayRateCents(1200 + (i % 9) * 250);
            k.setStatus(KitStatus.READY);
            batch.add(k);
        }
        return kits.saveAll(batch);
    }

    private void seedLoans(List<Member> people, List<Kit> cage, Random rng) {
        List<Loan> batch = new ArrayList<>();
        LocalDateTime now = LocalDateTime.of(2026, 9, 11, 10, 0);

        int openCount = 18;
        for (int i = 0; i < 150; i++) {
            Member member = people.get(i % people.size());
            Kit kit = cage.get(i % cage.size());
            Loan loan = new Loan();
            loan.setMember(member);
            loan.setKit(kit);
            LocalDateTime taken = now.minusDays(8 - (i % 7)).minusHours(i % 11);
            LocalDateTime due = taken.plusHours(24 + (i % 36));
            loan.setTakenAt(taken);
            loan.setDueAt(due);
            loan.setLockerPin(String.format("%06d", 100000 + i));
            loan.setNote(i % 5 == 0 ? "Night exterior, Media Wing yard" : "Block-week shoot");

            if (i < openCount) {
                loan.setStatus(LoanStatus.OPEN);
                kit.setStatus(KitStatus.OUT);
            } else if (rng.nextBoolean() && due.isBefore(now.minusHours(2))) {
                loan.setStatus(LoanStatus.LATE);
                loan.setReturnedAt(due.plusHours(3 + rng.nextInt(8)));
            } else {
                loan.setStatus(LoanStatus.RETURNED);
                loan.setReturnedAt(due.minusHours(1));
            }
            batch.add(loan);
        }
        kits.saveAll(cage);
        loans.saveAll(batch);
    }

    private List<Production> seedProductions(List<Member> people, List<Kit> cage, Random rng) {
        List<Production> batch = new ArrayList<>();
        ProductionKind[] kinds = ProductionKind.values();
        CutStatus[] cuts = CutStatus.values();
        for (int i = 0; i < 120; i++) {
            Production p = new Production();
            p.setSlug((i == 0 ? "potsdam-night-cut" : "cut-" + (i + 1)));
            p.setTitle(i == 0 ? "Potsdam Night Cut" : TITLES[i % TITLES.length] + " " + (i + 1));
            p.setKind(kinds[i % kinds.length]);
            p.setOwner(people.get(i % people.size()));
            p.setKit(cage.get((i * 2) % cage.size()));
            p.setRuntimeSec(90 + rng.nextInt(1200));
            p.setCutStatus(cuts[i % cuts.length]);
            p.setCreatedAt(LocalDateTime.of(2026, 4, 1, 9, 0).plusDays(i));
            p.setPayload(card(i, people));
            batch.add(p);
        }
        return productions.saveAll(batch);
    }

    private void seedReviews(List<Member> people, List<Production> cuts, Random rng) {
        List<Review> batch = new ArrayList<>();
        String[] lines = {
                "Grade holds on the tram windows.",
                "Boom is clean; room tone is a bit thin.",
                "Cut is tight after the Havel insert.",
                "Titles sit too long on black.",
                "VR loop made me dizzy on the lobby pass.",
                "Podcast levels jump on the second guest."
        };
        Set<String> seen = new HashSet<>();
        int i = 0;
        while (batch.size() < 200) {
            Production p = cuts.get(i % cuts.size());
            Member reviewer = people.get((i * 7 + 3) % people.size());
            String key = p.getId() + ":" + reviewer.getId();
            if (!reviewer.getId().equals(p.getOwner().getId()) && seen.add(key)) {
                Review r = new Review();
                r.setProduction(p);
                r.setMember(reviewer);
                r.setStars(2 + rng.nextInt(4));
                r.setBody(lines[i % lines.length]);
                r.setPostedAt(p.getCreatedAt().plusDays(2 + (i % 9)));
                batch.add(r);
            }
            i++;
            if (i > 4000) {
                break;
            }
        }
        reviews.saveAll(batch);
    }

    private Map<String, Object> card(int i, List<Member> people) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("codec", CODECS[i % CODECS.length]);
        payload.put("container", i % 2 == 0 ? "MOV" : "MXF");
        payload.put("colourSpace", i % 3 == 0 ? "S-Log3" : "Rec.709");
        List<Map<String, String>> crew = new ArrayList<>();
        crew.add(Map.of("role", "Director", "campusId", people.get(i % people.size()).getCampusId()));
        crew.add(Map.of("role", "AC", "campusId", people.get((i + 11) % people.size()).getCampusId()));
        payload.put("crew", crew);
        payload.put("chapters", List.of(
                Map.of("title", "Teaser", "sec", 12 + (i % 20)),
                Map.of("title", "Main", "sec", 40 + (i % 80))
        ));
        return payload;
    }

    private Member member(String campusId, String name, String mail, String programme, String cohort) {
        Member m = new Member();
        m.setCampusId(campusId);
        m.setDisplayName(name);
        m.setMail(mail);
        m.setProgramme(programme);
        m.setCohort(cohort);
        m.setCreatedAt(LocalDateTime.of(2026, 2, 1, 8, 0));
        return m;
    }
}
