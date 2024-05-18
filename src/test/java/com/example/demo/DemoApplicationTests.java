package com.example.demo;

import com.example.demo.dto.LinkDTO;
import com.example.demo.dto.LinkOriginDTO;
import com.example.demo.model.Link;
import com.example.demo.repository.LinkRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static LinkOriginDTO saveDto;

    @BeforeAll()
    public static void setUp() {
        saveDto = new LinkOriginDTO();
        saveDto.setOrigin("https://www.lamoda.ru/c/832/default-sports-men/?labels=41719&display_locations=all&ad_id=924238");
    }

    @AfterEach
    public void cleanUpEach() {
        linkRepository.deleteAll();
    }

    @Test
    @DisplayName("Generate link")
    void generateTest() throws Exception {
        mockMvc.perform(post("/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Get a origin link")
    void getOriginByShortNameTest() throws Exception {
        mockMvc.perform(post("/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveDto)));
        String shortLink = linkRepository.findById(8L)
                .get()
                .getShortName();

        LinkOriginDTO originDTO = objectMapper.readValue(mockMvc.perform(get("/{shortName}", shortLink))
                .andReturn()
                .getResponse()
                .getContentAsString(), LinkOriginDTO.class);

        assertThat(saveDto.getOrigin()).isEqualTo(originDTO.getOrigin());
    }

    @Test
    @DisplayName("Get a stat of link")
    void getStatsShowTest() throws Exception {
        mockMvc.perform(post("/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveDto)));
        Link link = linkRepository.findById(6L).get();

        LinkDTO linkDTO = objectMapper.readValue(mockMvc.perform(get("/stats/{shortName}", link.getShortName()))
                .andReturn()
                .getResponse()
                .getContentAsString(), LinkDTO.class);


        assertThat(linkDTO.getOrigin()).isEqualTo(link.getOrigin());
        assertThat(linkDTO.getCount()).isEqualTo(link.getCount());
    }

    @Test
    @DisplayName("Get stats")
    void getStatsTest() throws Exception {
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(saveDto)));
        }

        int size = objectMapper.readValue(mockMvc.perform(get("/stats"))
                .andReturn()
                .getResponse()
                .getContentAsString(), new TypeReference<List<LinkDTO>>() {})
                .size();
        int actualSize = linkRepository
                .findAll()
                .size();
        assertEquals(size, actualSize);
    }
}
