package izanami.example.me;

import izanami.javadsl.FeatureClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

@RestController
@RequestMapping("/api/me")
public class MeController {

    private final MeService meService;

    public MeController(MeService meService) {
        this.meService = meService;
    }

    @GetMapping(path = "")
    ResponseEntity<Me> get(@CookieValue(value = "userId", required=false) String userId) {
        return checkSecurity(userId, () ->
                meService.get(userId)
        );
    }

    @PostMapping(path = "/{serieId}")
    ResponseEntity<Me>  addSerie(@CookieValue(value = "userId") String userId, @PathVariable("serieId") String serieId) {
        return checkSecurity(userId, () ->
                meService.addTvShow(userId, serieId)
        );
    }

    @DeleteMapping(path = "/{serieId}")
    ResponseEntity<Me>  removeSerie(@CookieValue(value = "userId") String userId, @PathVariable("serieId") String serieId) {
        return checkSecurity(userId, () ->
                meService.removeTvShow(userId, serieId)
        );
    }

    @PostMapping(path = "/{serieId}/episodes/{episodeId}")
    ResponseEntity<Me>  markEpisode(
            @CookieValue(value = "userId") String userId,
            @PathVariable("serieId") String serieId,
            @PathVariable("episodeId") String episodeId,
            @RequestParam("watched") Boolean watched
    ) {

        return checkSecurity(userId, () ->
                meService.markEpisode(userId, serieId, episodeId, watched)
        );
    }

    @PostMapping(path = "/{serieId}/seasons/{seasonNumber}")
    ResponseEntity<Me> markSeason(
            @CookieValue(value = "userId") String userId,
            @PathVariable("serieId") String serieId,
            @PathVariable("seasonNumber") Long seasonNumber,
            @RequestParam("watched") Boolean watched
    ) {
        return checkSecurityResponse(userId, () -> ResponseEntity.badRequest().<Me>body(null));
    }

    private <T> ResponseEntity<T> checkSecurity(String userId, Supplier<T> func) {
        return checkSecurityResponse(userId, () -> ResponseEntity.ok(func.get()));
    }

    private <T> ResponseEntity<T> checkSecurityResponse(String userId, Supplier<ResponseEntity<T>> func) {
        if (userId == null) {
            return new ResponseEntity<T>(HttpStatus.UNAUTHORIZED);
        } else {
            return func.get();
        }
    }

}
