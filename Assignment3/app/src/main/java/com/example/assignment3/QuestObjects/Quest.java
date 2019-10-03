package com.example.assignment3.QuestObjects;

import com.example.assignment3.HeroObjects.Hero;

import java.io.Serializable;

public class Quest implements Serializable {

    String QuestName;
    Long questDuration;
    Long questStartTime;
    QuestDifficulty questDifficulty;
    int numberOfMobs;
    int numberOfNormalBosses;
    int numberOfEliteBosses;
    int numberOfLegendaryBosses;
    int goldReward;
    int expReward;
    int heroDeathRate;
    int questDifficultyFactor;
    Hero questHero1;
    Hero questHero2;
    Hero questHero3;

    public Quest() { }

    public Hero getQuestHero1() {
        return questHero1;
    }

    public void setQuestHero1(Hero questHero1) {
        this.questHero1 = questHero1;
    }

    public Hero getQuestHero2() {
        return questHero2;
    }

    public void setQuestHero2(Hero questHero2) {
        this.questHero2 = questHero2;
    }

    public Hero getQuestHero3() {
        return questHero3;
    }

    public void setQuestHero3(Hero questHero3) {
        this.questHero3 = questHero3;
    }

    public String getQuestName() {
        return QuestName;
    }

    public void setQuestName(String questName) {
        QuestName = questName;
    }

    public Long getQuestDuration() {
        return questDuration;
    }

    public void setQuestDuration(Long questDuration) {
        this.questDuration = questDuration;
    }

    public Long getQuestStartTime() {
        return questStartTime;
    }

    public void setQuestStartTime(Long questStartTime) {
        this.questStartTime = questStartTime;
    }

    public QuestDifficulty getQuestDifficulty() {
        return questDifficulty;
    }

    public void setQuestDifficulty(QuestDifficulty questDifficulty) {
        this.questDifficulty = questDifficulty;
    }

    public int getNumberOfMobs() {
        return numberOfMobs;
    }

    public void setNumberOfMobs(int numberOfMobs) {
        this.numberOfMobs = numberOfMobs;
    }

    public int getNumberOfNormalBosses() {
        return numberOfNormalBosses;
    }

    public void setNumberOfNormalBosses(int numberOfNormalBosses) {
        this.numberOfNormalBosses = numberOfNormalBosses;
    }

    public int getNumberOfEliteBosses() {
        return numberOfEliteBosses;
    }

    public void setNumberOfEliteBosses(int numberOfEliteBosses) {
        this.numberOfEliteBosses = numberOfEliteBosses;
    }

    public int getNumberOfLegendaryBosses() {
        return numberOfLegendaryBosses;
    }

    public void setNumberOfLegendaryBosses(int numberOfLegendaryBosses) {
        this.numberOfLegendaryBosses = numberOfLegendaryBosses;
    }

    public int getGoldReward() {
        return goldReward;
    }

    public void setGoldReward(int goldReward) {
        this.goldReward = goldReward;
    }

    public int getExpReward() {
        return expReward;
    }

    public void setExpReward(int expReward) {
        this.expReward = expReward;
    }

    public int getHeroDeathRate() {
        return heroDeathRate;
    }

    public void setHeroDeathRate(int heroDeathRate) {
        this.heroDeathRate = heroDeathRate;
    }

    public int getQuestDifficultyFactor() {
        return questDifficultyFactor;
    }

    public void setQuestDifficultyFactor(int questDifficultyFactor) {
        this.questDifficultyFactor = questDifficultyFactor;
    }
}
