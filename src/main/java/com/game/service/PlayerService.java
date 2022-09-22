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

    public List<Player> findAllWithFilter(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel, Integer pageNumber, Integer pageSize, PlayerOrder order) {
        Date afterDate = null;
        Date beforeDate = null;
        if (after != null) afterDate = new Date(after);
        if (before != null) beforeDate = new Date(before);
        Integer pageNumberDefault = 0;
        Integer pageSizeDefault = 3;
        if (pageNumber != null) pageNumberDefault = pageNumber;
        if (pageSize != null) pageSizeDefault = pageSize;

        if (order == null) return playerRepository.findPlayersByParams(name, title, race, profession, afterDate, beforeDate, banned, minExperience, maxExperience, minLevel, maxLevel, PageRequest.of(pageNumberDefault, pageSizeDefault, Sort.by("id"))).getContent();
        return playerRepository.findPlayersByParams(name, title, race, profession, afterDate, beforeDate, banned, minExperience, maxExperience, minLevel, maxLevel, PageRequest.of(pageNumberDefault, pageSizeDefault, Sort.by(order.getFieldName()))).getContent();
    }

    public Player getById(Long id) {
        Player player = playerRepository.findPlayerById(id);
        return player;
    }


    public void deleteById(Long id) {
        playerRepository.deleteById(id);
    }

    public Player addPlayer(Player player) {
        Player savedPlayer = playerRepository.saveAndFlush(player);
        return savedPlayer;
    }

    public Player update(Long id, Player player) {
        player.setId(id);
        return playerRepository.saveAndFlush(player);
    }

    public Long countPlayersByParams(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel/*, Integer pageNumber, Integer pageSize*/) {
        Date afterDate = null;
        Date beforeDate = null;
        if (after != null) afterDate = new Date(after);
        if (before != null) beforeDate = new Date(before);
        /*Integer pageNumberDefault = 0;
        Integer pageSizeDefault = 3;
        if (pageNumber != null) pageNumberDefault = pageNumber;
        if (pageSize != null) pageSizeDefault = pageSize;*/
        Long result = playerRepository.findPlayersByParams(name, title, race, profession, afterDate, beforeDate, banned, minExperience, maxExperience, minLevel, maxLevel, PageRequest.of(0, 3, Sort.by("id"))).getTotalElements();
        return result;
    }

    public Long count() {
        Long result = playerRepository.count();
        return result;
    }
}
