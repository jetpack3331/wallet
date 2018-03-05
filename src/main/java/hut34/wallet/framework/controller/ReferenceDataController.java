package hut34.wallet.framework.controller;

import hut34.wallet.framework.ref.ReferenceDataDto;
import hut34.wallet.framework.ref.ReferenceDataService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/reference-data")
public class ReferenceDataController {

    private final ReferenceDataService referenceDataService;

    public ReferenceDataController(ReferenceDataService referenceDataService) {
        this.referenceDataService = referenceDataService;
    }

    @RequestMapping
    public Map<String, List<ReferenceDataDto>> getReferenceData() {
        return referenceDataService.getReferenceData();
    }
}
