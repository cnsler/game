package com.game.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String title;
    @Column
    @Enumerated(EnumType.STRING)
    private Race race;
    @Column
    @Enumerated(EnumType.STRING)
    private Profession profession;
    @Column
    private Date birthday;
    @Column
    private Boolean banned;
    @Column
    private Integer experience;
    @Column
    private Integer level;
    @Column
    private Integer untilNextLevel;

    public Player() {
    }

    private void levelCalculate() {
        level = (int) (Math.sqrt(2500 + 200 * experience) - 50) / 100;
    }

    private void untilNextLevelCalculate() {
        untilNextLevel = 50 * (level + 1) * (level + 2) - experience;
    }

    public boolean isEmpty() {
        return name == null
                && title == null
                && race == null
                && profession == null
                && birthday == null
                && banned == null
                && experience == null;
    }

    private boolean isFull() {
        return name != null
                && title != null
                && race != null
                && profession != null
                && birthday != null
                && banned != null
                && experience != null;
    }

    private boolean isValidName() {
        return name == null || (!name.isEmpty() && name.length() <= 12);
    }

    private boolean isValidTitle() {
        return title == null || (!title.isEmpty() && title.length() <= 30);
    }

    private boolean isValidBirthday() {
        return birthday == null || (birthday.getYear() >= (2000 - 1900) && birthday.getYear() <= (3000 - 1900));
    }

    private boolean isValidExperience() {
        return experience == null || (experience >= 0 && experience <= 10_000_000);
    }

    public boolean isValid() {
        return isFull()
                && isValidName()
                && isValidTitle()
                && isValidBirthday()
                && isValidExperience();
    }

    public boolean isValidFields() {
        return isValidName() && isValidTitle() && isValidBirthday() && isValidExperience();
    }

    public Player fill() {
        levelCalculate();
        untilNextLevelCalculate();
        return this;
    }

    public Player update(Player player) {
        if (player.name != null) name = player.name;
        if (player.title != null) title = player.title;
        if (player.race != null) race = player.race;
        if (player.profession != null) profession = player.profession;
        if (player.birthday != null) birthday = player.birthday;
        if (player.banned != null) banned = player.banned;
        if (player.experience != null) experience = player.experience;
        return this.fill();
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name=" + name +
                ", title=" + title +
                ", race=" + race +
                ", profession=" + profession +
                ", birthday=" + birthday +
                ", banned=" + banned +
                ", experience=" + experience +
                ", level=" + level +
                ", untilNextLevel=" + untilNextLevel +
                '}';
    }
}
