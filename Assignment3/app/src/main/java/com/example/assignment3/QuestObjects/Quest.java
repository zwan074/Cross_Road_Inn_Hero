package com.example.assignment3.QuestObjects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.assignment3.HeroObjects.Hero;
import com.example.assignment3.HeroObjects.HeroClass;
import com.example.assignment3.HeroObjects.HeroSkill;

import java.io.Serializable;
import java.util.ArrayList;

public class Quest implements Serializable, Parcelable {

    private String QuestName;
    private Long questDuration;
    private Long questStartTime;
    private QuestDifficulty questDifficulty;
    private int numberOfMobs;
    private int numberOfNormalBosses;
    private int numberOfEliteBosses;
    private int numberOfLegendaryBosses;
    private int goldReward;
    private int expReward;
    private int heroDeathRate;
    private int questDifficultyFactor;
    private Hero questHero1;
    private Hero questHero2;
    private Hero questHero3;
    private QuestStatus questStatus;
    private Double questDeathRate;
    private Double questSuccessRate;

    private int teamAttackPower;
    private int teamDefencePower;
    private int enemyPower;
    private int teamDeathRate;
    private double timeDurationReduction;
    private long initQuestDuration;
    private boolean notified;

    static final long THIRTY_MIN = 1000 * 60 * 30 ;
    static final long TWO_HOURS = 1000 * 60 * 60 * 2 ;
    static final long ONE_DAY = 1000 * 60 * 60 * 24 ;

    public Quest() { }

    public String getQuestSummary () {

        switch (this.getQuestDifficulty() ) {
            case NORMAL: initQuestDuration = THIRTY_MIN ;break;
            case ELITE: initQuestDuration = TWO_HOURS ;break;
            case LEGENDARY: initQuestDuration = ONE_DAY ;break;
        }

        int hero1AP = 0, hero1DP=0 ,hero2AP = 0, hero2DP=0,hero3AP = 0, hero3DP=0;
        timeDurationReduction = 0;

        if ( this.getQuestHero1() != null) {
            hero1AP = 10 * this.getQuestHero1().getLevel() + 5 * this.getQuestHero1().getHeroAttackPower() + 10 * this.getQuestHero1().getHeroAttackPowerEnhancement();
            hero1DP = this.getQuestHero1().getHeroDefencePower() + 5 * this.getQuestHero1().getHeroDefencePowerEnhancement();
        }
        if ( this.getQuestHero2() != null) {
            hero2AP = 10 * this.getQuestHero2().getLevel() + 5 * this.getQuestHero2().getHeroAttackPower() + 10 * this.getQuestHero2().getHeroAttackPowerEnhancement();
            hero2DP = this.getQuestHero2().getHeroDefencePower() + 5 * this.getQuestHero2().getHeroDefencePowerEnhancement();
        }
        if ( this.getQuestHero3() != null) {
            hero3AP = 10 * this.getQuestHero3().getLevel() + 5 * this.getQuestHero3().getHeroAttackPower() + 10 * this.getQuestHero3().getHeroAttackPowerEnhancement();
            hero3DP = this.getQuestHero3().getHeroDefencePower() + 5 * this.getQuestHero3().getHeroDefencePowerEnhancement();
        }

        teamAttackPower = hero1AP + hero2AP + hero3AP;
        teamDefencePower = hero1DP + hero2DP + hero3DP;

        enemyPower = this.getNumberOfMobs() + 5 * this.getNumberOfNormalBosses() + 10 * this.getNumberOfEliteBosses() + 50 * this.getNumberOfLegendaryBosses();
        teamDeathRate = this.getHeroDeathRate() - teamDefencePower/10;



        computeHeroSkill( this.getQuestHero1());
        computeHeroSkill( this.getQuestHero2());
        computeHeroSkill( this.getQuestHero3());

        if ( isWarriorMagePriestCombo ( this.getQuestHero1(),this.getQuestHero2(),this.getQuestHero3() ) ) {
            teamAttackPower = teamAttackPower*2;
            teamDeathRate -= 5;
        }

        double questSuccessRate = (double) teamAttackPower/enemyPower;
        Log.i("", "" + teamAttackPower + " " + enemyPower + " " + questSuccessRate);
        this.setQuestDuration((long) (initQuestDuration * ( 1 - timeDurationReduction)));
        this.setQuestDeathRate((double) (teamDeathRate <= 0 ? 0 :  teamDeathRate));
        this.setQuestSuccessRate(questSuccessRate >=1 ? 1 : questSuccessRate);

        return  "Quest Summary : \n" +
                "Team Attack Power : " +  teamAttackPower + "\n" +
                "Team Defence Power : " +  teamDefencePower + "\n" +
                "Enemy Power : " + enemyPower + "\n" +
                "Team Death Rate : " +  teamDeathRate + "%\n" +
                "Quest Success  Rate : " +  String.format("%.2f", questSuccessRate * 100) + "%";
    }

    private void computeHeroSkill (Hero hero) {
        if (hero != null) {

            if (hero.getHeroSkill() == HeroSkill.CLEAVE) {
                this.teamAttackPower += hero.getHeroAttackPower() + hero.getHeroDefencePowerEnhancement() * 2 ;
            } else if (hero.getHeroSkill() == HeroSkill.FIREBALL) {
                this.timeDurationReduction += 0.15;
            } else if (hero.getHeroSkill() == HeroSkill.HEAL) {
                this.teamDeathRate -= 5;
            }
        }
    }

    private boolean isWarriorMagePriestCombo (Hero hero1, Hero hero2, Hero hero3) {

        ArrayList<HeroClass> heroCombo = new ArrayList<>();
        if (hero1 != null) heroCombo.add(hero1.getHeroClass());
        if (hero2 != null) heroCombo.add(hero2.getHeroClass());
        if (hero3 != null)heroCombo.add(hero3.getHeroClass());

        int numberOfWarrior = (int) heroCombo.stream().filter(e -> e == HeroClass.WARRIOR).count();
        int numberOfMage = (int) heroCombo.stream().filter(e -> e == HeroClass.MAGE).count();
        int numberOfPriest = (int) heroCombo.stream().filter(e -> e == HeroClass.PRIEST).count();

        return numberOfWarrior == 1 && numberOfMage == 1 && numberOfPriest == 1 ;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public QuestStatus getQuestStatus() {
        return questStatus;
    }

    public void setQuestStatus(QuestStatus questStatus) {
        this.questStatus = questStatus;
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

    public Double getQuestDeathRate() {
        return questDeathRate;
    }

    public void setQuestDeathRate(Double questDeathRate) {
        this.questDeathRate = questDeathRate;
    }

    public Double getQuestSuccessRate() {
        return questSuccessRate;
    }

    public void setQuestSuccessRate(Double questSuccessRate) {
        this.questSuccessRate = questSuccessRate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(QuestName);
        if (questDuration == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(questDuration);
        }
        if (questStartTime == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(questStartTime);
        }
        parcel.writeInt(numberOfMobs);
        parcel.writeInt(numberOfNormalBosses);
        parcel.writeInt(numberOfEliteBosses);
        parcel.writeInt(numberOfLegendaryBosses);
        parcel.writeInt(goldReward);
        parcel.writeInt(expReward);
        parcel.writeInt(heroDeathRate);
        parcel.writeInt(questDifficultyFactor);
        parcel.writeParcelable(questHero1, i);
        parcel.writeParcelable(questHero2, i);
        parcel.writeParcelable(questHero3, i);
        if (questDeathRate == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(questDeathRate);
        }
        if (questSuccessRate == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(questSuccessRate);
        }
    }
    protected Quest(Parcel in) {
        QuestName = in.readString();
        if (in.readByte() == 0) {
            questDuration = null;
        } else {
            questDuration = in.readLong();
        }
        if (in.readByte() == 0) {
            questStartTime = null;
        } else {
            questStartTime = in.readLong();
        }
        numberOfMobs = in.readInt();
        numberOfNormalBosses = in.readInt();
        numberOfEliteBosses = in.readInt();
        numberOfLegendaryBosses = in.readInt();
        goldReward = in.readInt();
        expReward = in.readInt();
        heroDeathRate = in.readInt();
        questDifficultyFactor = in.readInt();
        questHero1 = in.readParcelable(Hero.class.getClassLoader());
        questHero2 = in.readParcelable(Hero.class.getClassLoader());
        questHero3 = in.readParcelable(Hero.class.getClassLoader());
        if (in.readByte() == 0) {
            questDeathRate = null;
        } else {
            questDeathRate = in.readDouble();
        }
        if (in.readByte() == 0) {
            questSuccessRate = null;
        } else {
            questSuccessRate = in.readDouble();
        }
    }

    public static final Creator<Quest> CREATOR = new Creator<Quest>() {
        @Override
        public Quest createFromParcel(Parcel in) {
            return new Quest(in);
        }

        @Override
        public Quest[] newArray(int size) {
            return new Quest[size];
        }
    };
}
