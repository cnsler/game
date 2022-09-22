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

import java.sql.Date;
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
        Date afterDate = null;
        Date beforeDate = null;
        if (after != null) afterDate = new Date(after);
        if (before != null) beforeDate = new Date(before);

        return playerRepository.findPlayersByParams(
                name, title, race, profession,
                afterDate, beforeDate, banned,
                minExperience, maxExperience, minLevel, maxLevel,
                PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()))).getContent();
    }

    public Player getPlayerOrNullById(Long id) {
        return playerRepository.findById(id).orElse(null);
    }


    public void deletePlayerById(Long id) {
        playerRepository.deleteById(id);
    }

    public void addPlayer(Player player) {
        playerRepository.saveAndFlush(player);
    }

    public void updatePlayer(Long id, Player player) {
        player.setId(id);
        playerRepository.saveAndFlush(player);
    }

    public Long getCountPlayersByParams(String name, String title, Race race, Profession profession,
                                        Long after, Long before, Boolean banned,
                                        Integer minExperience, Integer maxExperience,
                                        Integer minLevel, Integer maxLevel,
                                        Integer pageNumber, Integer pageSize, PlayerOrder order) {
        Date afterDate = null;
        Date beforeDate = null;
        if (after != null) afterDate = new Date(after);
        if (before != null) beforeDate = new Date(before);

        return playerRepository.findPlayersByParams(
                name, title, race, profession,
                afterDate, beforeDate, banned,
                minExperience, maxExperience,
                minLevel, maxLevel,
                PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()))).getTotalElements();
    }

    public Long getCountAllPlayers() {
        return playerRepository.count();
    }
}
