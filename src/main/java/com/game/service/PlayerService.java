package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> findPlayersByParams(String name, String title, Race race, Profession profession,
                                            Long after, Long before, Boolean banned,
                                            Integer minExperience, Integer maxExperience,
                                            Integer minLevel, Integer maxLevel,
                                            Integer pageNumber, Integer pageSize, PlayerOrder order) {
        return playerRepository.findPlayersByParams(
                name, title, race, profession,
                getDateOrNull(after), getDateOrNull(before), banned,
                minExperience, maxExperience, minLevel, maxLevel,
                PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()))).getContent();
    }

    public Player getPlayerById(Long id) {
        if (id < 1) throw new IllegalArgumentException();
        Player playerById = playerRepository.findById(id).orElse(null);
        if (playerById == null) throw new EntityNotFoundException();
        return playerById;
    }


    public void deletePlayerById(Long id) {
        playerRepository.delete(getPlayerById(id));
    }

    public Player createPlayer(Player player) {
        if (player.isEmpty() || !player.isValid()) throw new IllegalArgumentException();

        return playerRepository.saveAndFlush(player.fill());
    }

    public Player updatePlayer(Long id, Player player) {
        Player playerById = getPlayerById(id);

        if (player.isEmpty()) return playerById;

        if (player.isValidFields()) return playerRepository.saveAndFlush(playerById.update(player));
        else throw new IllegalArgumentException();
    }

    public Long getCountPlayersByParams(String name, String title, Race race, Profession profession,
                                        Long after, Long before, Boolean banned,
                                        Integer minExperience, Integer maxExperience,
                                        Integer minLevel, Integer maxLevel,
                                        Integer pageNumber, Integer pageSize, PlayerOrder order) {
        return playerRepository.findPlayersByParams(
                name, title, race, profession,
                getDateOrNull(after), getDateOrNull(before), banned,
                minExperience, maxExperience,
                minLevel, maxLevel,
                PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()))).getTotalElements();
    }

    public Long getCountAllPlayers() {
        return playerRepository.count();
    }

    private Date getDateOrNull(Long unixDate) {
        return unixDate != null ? new Date(unixDate) : null;
    }
}
