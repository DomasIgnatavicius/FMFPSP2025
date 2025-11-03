package lt.ignatavicius.fmfpsp.miestomeras.rest.controller;

import lombok.RequiredArgsConstructor;
import lt.ignatavicius.fmfpsp.miestomeras.model.Rule;
import lt.ignatavicius.fmfpsp.miestomeras.model.State;
import lt.ignatavicius.fmfpsp.miestomeras.rest.dto.ActionRequest;
import lt.ignatavicius.fmfpsp.miestomeras.rest.dto.AutoplayRequest;
import lt.ignatavicius.fmfpsp.miestomeras.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class GameController {
    private final GameService gameService;

    @GetMapping("/state")
    public ResponseEntity<State> getState() {
        return ResponseEntity.ok(gameService.state());
    }

    @GetMapping("/actions")
    public ResponseEntity<List<Rule>> getActions() {
        return ResponseEntity.ok(gameService.rules());
    }

    @PostMapping("/act")
    public ResponseEntity<State> applyRule(@RequestBody ActionRequest request) {
        State newState = gameService.applyRule(request.getCityIndex(), request.getRuleId());
        return ResponseEntity.ok(newState);
    }

    @PostMapping("/undo")
    public ResponseEntity<State> undo() {
        return ResponseEntity.ok(gameService.undo());
    }

    @PostMapping("/redo")
    public ResponseEntity<State> redo() {
        return ResponseEntity.ok(gameService.redo());
    }

    @PostMapping("/autoplay")
    public ResponseEntity<State> autoplay(@RequestBody AutoplayRequest request) {
        State newState = gameService.autoplay(request.getTurns());
        return ResponseEntity.ok(newState);
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> reset() {
        gameService.reset();
        return ResponseEntity.ok().build();
    }
}

