package csh.log.fencingreferee.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import csh.log.fencingreferee.api.dto.BoutResponse;
import csh.log.fencingreferee.api.dto.ScoringEventResponse;
import csh.log.fencingreferee.api.dto.VideoUrlResponse;
import csh.log.fencingreferee.domain.Bout;
import csh.log.fencingreferee.domain.User;
import csh.log.fencingreferee.persistence.UserRepository;
import csh.log.fencingreferee.service.BoutService;
import csh.log.fencingreferee.service.VideoStorageService.PresignedUpload;

@RestController
@RequestMapping("/api/bouts")
public class BoutController {

    private final BoutService boutService;
    private final UserRepository userRepo;

    public BoutController(BoutService boutService, UserRepository userRepo) {
        this.boutService = boutService;
        this.userRepo = userRepo;
    }

    private User getCurrentUser() {
        return userRepo.findById(1L).orElseThrow(); // TODO: replace with actual auth
    }

    @PostMapping("/{id}/score")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void scoreBout(@PathVariable Long id) {
        boutService.scoreBout(id);
    }

    @PostMapping("/upload-url")
    public PresignedUpload createUploadUrl(
            @RequestParam String filename) {

        User user = getCurrentUser();

        return boutService.createUploadUrl(user, filename);
    }


    @GetMapping("/{id}")
    public BoutResponse getBout(@PathVariable Long id) {
        Bout bout = boutService.getBout(id);
        return new BoutResponse(bout.getId(), bout.getStatus());
    }

    @GetMapping("/{id}/events")
    public List<ScoringEventResponse> getEvents(@PathVariable Long id) {
        return boutService.getBoutEvents(id);
    }

    @GetMapping("/{id}/video")
    public VideoUrlResponse getVideoUrl(@PathVariable Long id) {
        return new VideoUrlResponse(boutService.getVideoDownloadUrl(id));
    }
}