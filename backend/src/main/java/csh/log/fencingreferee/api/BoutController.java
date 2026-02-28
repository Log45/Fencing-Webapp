package csh.log.fencingreferee.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import csh.log.fencingreferee.api.dto.BoutResponse;
import csh.log.fencingreferee.api.dto.CreateBoutRequest;
import csh.log.fencingreferee.domain.Bout;
import csh.log.fencingreferee.service.BoutService;

@RestController
@RequestMapping("/api/bouts")
public class BoutController {

    private final BoutService boutService;

    public BoutController(BoutService boutService) {
        this.boutService = boutService;
    }

    @PostMapping
    public BoutResponse createBout(@RequestBody CreateBoutRequest request) {
        Bout bout = boutService.createBout(request.videoUrl());
        return new BoutResponse(bout.getId(), bout.getStatus().name());
    }

    @PostMapping("/{id}/score")
    public void scoreBout(@PathVariable Long id) {
        boutService.scoreBout(id);
    }
}
