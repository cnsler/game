package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                                      @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder order,
                                      @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {

        Long count;
        if (name == null && title == null && race == null && profession == null
                && after == null && before == null && banned == null
                && minExperience == null && maxExperience == null && minLevel == null && maxLevel == null) {
            count = playerService.getCountAllPlayers();
        } else {
            count = playerService.getCountPlayersByParams(
                    name, title, race, profession,
                    after, before, banned,
                    minExperience, maxExperience, minLevel, maxLevel,
                    pageNumber, pageSize, order);
        }
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @PostMapping(value = "/players")
    public ResponseEntity<Player> create(@RequestBody Player player) {

        if (player.getName() == null || player.getName().isEmpty() || player.getName().length() > 12) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (player.getTitle() == null || player.getTitle().isEmpty() || player.getTitle().length() > 30) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (player.getExperience() == null || player.getExperience() < 0 || player.getExperience() > 10_000_000) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (player.getBirthday() == null || player.getBirthday().getYear() < (2000 - 1900)
                || player.getBirthday().getYear() > (3000 - 1900)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Player createdPlayer = new Player(player.getName(), player.getTitle(), player.getRace(), player.getProfession(),
                player.getExperience(), player.getBirthday(), player.isBanned());
        playerService.addPlayer(createdPlayer);

        return new ResponseEntity<>(createdPlayer, HttpStatus.OK);
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
                                             @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder order,
                                             @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {

        final List<Player> players = playerService.findPlayersByParams(
                name, title, race, profession,
                after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel,
                pageNumber, pageSize, order);

        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping(value = "/players/{id}")
    public ResponseEntity<Player> read(@PathVariable(name = "id") Long id) {
        if (id < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        final Player player = playerService.getPlayerOrNullById(id);

        return player != null
                ? new ResponseEntity<>(player, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/players/{id}")
    public ResponseEntity<Player> update(@PathVariable(name = "id") Long id, @RequestBody Player player) {

        if (id < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Player updatedPlayer = playerService.getPlayerOrNullById(id);
        if (updatedPlayer == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        int count = 0;
        if (player.getName() != null) {
            if (!player.getName().isEmpty() && player.getName().length() < 12) {
                updatedPlayer.setName(player.getName());
            } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else count++;
        if (player.getTitle() != null) {
            if (!player.getTitle().isEmpty() && player.getTitle().length() <= 30) {
                updatedPlayer.setTitle(player.getTitle());
            } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else count++;
        if (player.getRace() != null) {
            updatedPlayer.setRace(player.getRace());
        } else count++;
        if (player.getProfession() != null) {
            updatedPlayer.setProfession(player.getProfession());
        } else count++;
        if (player.getBirthday() != null) {
            if (player.getBirthday().getYear() >= (2000 - 1900) && player.getBirthday().getYear() <= (3000 - 1900)) {
                updatedPlayer.setBirthday(player.getBirthday());
            } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else count++;
        if (player.isBanned() != null) {
            updatedPlayer.setBanned(player.isBanned());
        } else count ++;
        if (player.getExperience() != null) {
            if (player.getExperience() >= 0 && player.getExperience() <= 10_000_000) {
                updatedPlayer.setExperience(player.getExperience());
            } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else count++;
        if (count == 7) return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);

        updatedPlayer = new Player(
                updatedPlayer.getName(), updatedPlayer.getTitle(), updatedPlayer.getRace(), updatedPlayer.getProfession(),
                updatedPlayer.getExperience(), updatedPlayer.getBirthday(), updatedPlayer.isBanned());
        playerService.updatePlayer(id, updatedPlayer);

        return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);
    }

    @DeleteMapping(value = "/players/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {

        if (id < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Player player = playerService.getPlayerOrNullById(id);
        if (player == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        playerService.deletePlayerById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
