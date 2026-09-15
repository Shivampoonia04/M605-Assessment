package com.shivampoonia.reeldock.controller;

import com.shivampoonia.reeldock.dto.Views.CodecCount;
import com.shivampoonia.reeldock.dto.Views.DeskView;
import com.shivampoonia.reeldock.dto.Views.KindRating;
import com.shivampoonia.reeldock.dto.Views.KitLoad;
import com.shivampoonia.reeldock.service.DeskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class DeskController {

    private final DeskService desk;

    public DeskController(DeskService desk) {
        this.desk = desk;
    }

    @GetMapping("/desk/{campusId}")
    public DeskView desk(@PathVariable String campusId) {
        return desk.desk(campusId);
    }

    @GetMapping("/stats/counts")
    public Map<String, Long> counts() { return desk.counts(); }

    @GetMapping("/stats/ratings")
    public List<KindRating> ratings() { return desk.ratings(); }

    @GetMapping("/stats/codecs")
    public List<CodecCount> codecs() { return desk.codecs(); }

    @GetMapping("/stats/kits")
    public List<KitLoad> kitLoad() { return desk.kitLoad(); }
}
