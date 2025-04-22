package andrea_freddi.CAPSTONE_PROJECT.controllers;

import andrea_freddi.CAPSTONE_PROJECT.elasticsearch.WineDocument;
import andrea_freddi.CAPSTONE_PROJECT.services.WinesSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 * This class is a Spring Boot REST controller that handles requests related to wines.
 * It is mapped to the "/wines" URL path.
 */

@RestController
@RequestMapping("/wines")
@RequiredArgsConstructor
public class WinesController {

    private final WinesSearchService winesSearchService;

    @GetMapping("/search")
    public List<WineDocument> searchWines(@RequestParam String query) {
        return winesSearchService.search(query);
    }
}
