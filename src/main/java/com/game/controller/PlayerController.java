package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/rest")
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/players/count")
    public ResponseEntity<Long> count(@RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "title", required = false) String title,
                                      @RequestParam(value = "race", required = false) Race race,
                                      @RequestParam(value = "profession", required = false) Profession profession,
                                      @RequestParam(value = "after", required = false) Long after,
                                      @RequestParam(value = "before", required = false) Long before,
                                      @RequestParam(value = "banned", required = false) Boolean banned,
                                      @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                      @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                      @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                      @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                      @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize,
                                      @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder order) {

        Long count;
        if (name != null || title != null || race != null || profession != null
                || after != null || before != null || banned != null
                || minExperience != null || maxExperience != null || minLevel != null || maxLevel != null) {
            count = playerService.getCountPlayersByParams(
                    name, title, race, profession,
                    after, before, banned,
                    minExperience, maxExperience, minLevel, maxLevel,
                    pageNumber, pageSize, order);
        } else count = playerService.getCountAllPlayers();

        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @PostMapping(value = "/players")
    public ResponseEntity<Player> create(@RequestBody Player player) {

        try {
            return new ResponseEntity<>(playerService.createPlayer(player), HttpStatus.OK);
        } catch (IllegalArgumentException ignored) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/players")
    public ResponseEntity<List<Player>> read(@RequestParam(value = "name", required = false) String name,
                                             @RequestParam(value = "title", required = false) String title,
                                             @RequestParam(value = "race", required = false) Race race,
                                             @RequestParam(value = "profession", required = false) Profession profession,
                                             @RequestParam(value = "after", required = false) Long after,
                                             @RequestParam(value = "before", required = false) Long before,
                                             @RequestParam(value = "banned", required = false) Boolean banned,
                                             @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                             @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                             @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                             @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                             @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize,
                                             @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder order) {

        final List<Player> players = playerService.findPlayersByParams(
                name, title, race, profession,
                after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel,
                pageNumber, pageSize, order);

        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping(value = "/players/{id}")
    public ResponseEntity<Player> read(@PathVariable(name = "id") Long id) {

        try {
            return new ResponseEntity<>(playerService.getPlayerById(id), HttpStatus.OK);
        } catch (IllegalArgumentException ignored) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException ignored) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/players/{id}")
    public ResponseEntity<Player> update(@PathVariable(name = "id") Long id, @RequestBody Player player) {

        try {
            return new ResponseEntity<>(playerService.updatePlayer(id, player), HttpStatus.OK);
        } catch (IllegalArgumentException ignored) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException ignored) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/players/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {

        try {
            playerService.deletePlayerById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException ignored) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ignored) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
