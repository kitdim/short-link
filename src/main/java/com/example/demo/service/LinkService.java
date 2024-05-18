package com.example.demo.service;

import com.example.demo.component.GenerateLink;
import com.example.demo.dto.LinkDTO;
import com.example.demo.dto.LinkOriginDTO;
import com.example.demo.dto.LinkShortDTO;
import com.example.demo.model.Link;
import com.example.demo.repository.LinkRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Service
public class LinkService {
    private final LinkRepository linkRepository;
    private final GenerateLink generateLink;

    public LinkService(LinkRepository linkRepository, GenerateLink generateLink) {
        this.linkRepository = linkRepository;
        this.generateLink = generateLink;
    }

    public LinkShortDTO generate(LinkOriginDTO originDTO) {
        Link link = new Link();
        LinkShortDTO shortDTO = new LinkShortDTO();
        String originLink = originDTO.getOrigin();
        String shortLink = generateLink.encode(originLink);

        link.setOrigin(originLink);
        link.setShortName(shortLink);
        shortDTO.setLink(shortLink);

        linkRepository.save(link);
        return shortDTO;
    }

    public LinkOriginDTO getOriginByShortName(String shortName) {
        LinkOriginDTO originDTO = new LinkOriginDTO();
        Link someOriginLink = linkRepository.findByShortName(shortName)
                .orElseThrow(() -> new RuntimeException("Not found."));

        Long count = someOriginLink.getCount();
        String origin = someOriginLink.getOrigin();
        count++;
        someOriginLink.setCount(count);
        linkRepository.save(someOriginLink);

        originDTO.setOrigin(origin);
        return originDTO;
    }

    public LinkDTO getStatsShow(String shortName) {
        Link someLink = linkRepository.findByShortName(shortName)
                .orElseThrow(() -> new RuntimeException("Not found."));
        List<Link> links = linkRepository.findAll(Sort.by(Sort.Order.desc("count")));
        LinkDTO linkDTO = new LinkDTO();

        int rank = IntStream.range(0, links.size())
                .filter(i -> Objects.equals(links.get(i).getShortName(), shortName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found."));

        linkDTO.setCount(someLink.getCount());
        linkDTO.setOrigin(someLink.getOrigin());
        linkDTO.setShorName(shortName);
        linkDTO.setRank(rank + 1);

        return linkDTO;
    }

    public List<LinkDTO> getStats(int page, int count) {
        List<Link> links = linkRepository.findAll(Sort.by(Sort.Order.desc("count")));
        List<LinkDTO> stats = new ArrayList<>();
        List<LinkDTO> sliceOfStats;
        int offset = (page - 1) * count;

        for (int i = 0; i < links.size(); i++) {
            Link link = links.get(i);
            LinkDTO linkDTO = new LinkDTO();
            linkDTO.setRank(i + 1);
            linkDTO.setCount(link.getCount());
            linkDTO.setShorName(link.getShortName());
            linkDTO.setOrigin(link.getOrigin());

            stats.add(linkDTO);
        }

        try {
            sliceOfStats = stats.subList(offset, offset + count);
        } catch (IndexOutOfBoundsException e) {
            sliceOfStats = count > stats.size() ? stats.subList(offset, offset + stats.size()) : new ArrayList<>();
        }

        return sliceOfStats;
    }
}
