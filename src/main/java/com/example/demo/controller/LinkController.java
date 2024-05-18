package com.example.demo.controller;

import com.example.demo.dto.LinkDTO;
import com.example.demo.dto.LinkOriginDTO;
import com.example.demo.dto.LinkShortDTO;
import com.example.demo.service.LinkService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/")
public class LinkController {
    public final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping(path = "generate")
    @ResponseStatus(HttpStatus.CREATED)
    public LinkShortDTO generate(@RequestBody LinkOriginDTO origin) {
        return linkService.generate(origin);
    }

    @GetMapping(path = "{shortName}")
    @ResponseStatus(HttpStatus.OK)
    public LinkOriginDTO getOriginByShortName(@PathVariable String shortName) {
        return linkService.getOriginByShortName(shortName);
    }

    @GetMapping(path = "stats/{shortName}")
    @ResponseStatus(HttpStatus.OK)
    public LinkDTO getStatsShow(@PathVariable String shortName) {
        return linkService.getStatsShow(shortName);
    }

    @GetMapping(path = "stats")
    @ResponseStatus(HttpStatus.OK)
    public List<LinkDTO> getStats(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                  @RequestParam(name = "count", required = false, defaultValue = "10") int count) {
        return linkService.getStats(page ,count);
    }
}
