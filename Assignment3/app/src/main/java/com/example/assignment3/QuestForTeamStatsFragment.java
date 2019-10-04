package com.example.assignment3;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.assignment3.HeroObjects.Hero;
import com.example.assignment3.HeroObjects.HeroClass;
import com.example.assignment3.HeroObjects.HeroSkill;
import com.example.assignment3.QuestObjects.Quest;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class QuestForTeamStatsFragment extends Fragment{

    private Quest quest;
    private MainActivity activity;
    private QuestMenuAdapter questMenuAdapter;
    private View view;
    private int teamAttackPower;
    private int teamDefencePower;
    private int enemyPower;
    private int teamDeathRate;
    private double timeDurationReduction;
    private long initQuestDuration;

    static final long THIRTY_MIN = 1000 * 60 * 30 ;
    static final long TWO_HOURS = 1000 * 60 * 60 * 2 ;
    static final long ONE_DAY = 1000 * 60 * 60 * 24 ;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.quest_team_stats, container, false);
        activity = (MainActivity) getActivity();
        quest = (Quest) this.getArguments().getSerializable("Quest For Team Stats");
        questMenuAdapter = (QuestMenuAdapter) this.getArguments().getSerializable("Quest Menu Adapter");

        switch (quest.getQuestDifficulty() ) {
            case NORMAL: initQuestDuration = THIRTY_MIN ;break;
            case ELITE: initQuestDuration = TWO_HOURS ;break;
            case LEGENDARY: initQuestDuration = ONE_DAY ;break;
        }

        ConstraintLayout hero1Layout = view.findViewById(R.id.Hero1_layout);
        GifImageView hero1GIF = view.findViewById(R.id.Hero1_GIF);
        TextView hero1Info = view.findViewById(R.id.Hero1_Info);
        Button hero1AP = view.findViewById(R.id.Hero1_enhanceAP);
        Button hero1DP = view.findViewById(R.id.Hero1_enhanceDP);
        Button hero1RemoveThis = view.findViewById(R.id.Remove_Hero1);

        ConstraintLayout hero2Layout = view.findViewById(R.id.Hero2_layout);
        GifImageView hero2GIF = view.findViewById(R.id.Hero2_GIF);
        TextView hero2Info = view.findViewById(R.id.Hero2_Info);
        Button hero2AP = view.findViewById(R.id.Hero2_enhanceAP);
        Button hero2DP = view.findViewById(R.id.Hero2_enhanceDP);
        Button hero2RemoveThis = view.findViewById(R.id.Remove_Hero2);

        ConstraintLayout hero3Layout = view.findViewById(R.id.Hero3_layout);
        GifImageView hero3GIF = view.findViewById(R.id.Hero3_GIF);
        TextView hero3Info = view.findViewById(R.id.Hero3_Info);
        Button hero3AP = view.findViewById(R.id.Hero3_enhanceAP);
        Button hero3DP = view.findViewById(R.id.Hero3_enhanceDP);
        Button hero3RemoveThis = view.findViewById(R.id.Remove_Hero3);

        if (quest.getQuestHero1()!=null) setComponents( quest.getQuestHero1(),hero1Layout,hero1GIF,hero1Info,hero1AP,hero1DP,hero1RemoveThis,1);
        if (quest.getQuestHero2()!=null) setComponents( quest.getQuestHero2(),hero2Layout,hero2GIF,hero2Info,hero2AP,hero2DP,hero2RemoveThis,2);
        if (quest.getQuestHero3()!=null) setComponents( quest.getQuestHero3(),hero3Layout,hero3GIF,hero3Info,hero3AP,hero3DP,hero3RemoveThis,3);

        ImageButton closeTeamStats = view.findViewById(R.id.close_quest_team_stats);
        closeTeamStats.setOnClickListener(v->{
            //activity.getSupportFragmentManager().beginTransaction().replace(R.id.cross_road_inn_layout, new QuestMenuFragment()).commit()
            activity.getSupportFragmentManager().beginTransaction().remove(QuestForTeamStatsFragment.this).commit();
            activity.getSupportFragmentManager().popBackStack();
            }
        );

        TextView questSummary =view.findViewById(R.id.Quest_Summary);
        questSummary.setText(getQuestSummary ());

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setComponents (Hero hero , ConstraintLayout heroLayout, GifImageView heroGIF ,
                                TextView heroInfo, Button AP, Button DP, Button removeHero, int questHero ) {

        switch (hero.getHeroRarity()) {
            case NORMAL: heroLayout.setBackgroundColor(Color.parseColor("#CCDDD1C6"));;break;
            case ELITE : heroLayout.setBackgroundColor(Color.parseColor("#CC00E5FF"));break;
            case LEGENDARY : heroLayout.setBackgroundColor(Color.parseColor("#CCFF9100"));break;
        }

        heroGIF.setImageResource(hero.getHeroGifID());
        heroInfo.setText(
            "Rarity: " + hero.getHeroRarity()+ "\n" +
            "Name: " + hero.getHeroName()+ "\n" +
            "Class: " + hero.getHeroClass()+ "\n" +
            "Lvl: " + hero.getLevel()+ "\n" +
            "Skill: " + hero.getHeroSkill()+ "\n" +
            "AP: " + hero.getHeroAttackPower()+ "\n" +
            "DP: " + hero.getHeroDefencePower()+ "\n" +
            "EXP: " + hero.getExp()
        );
        AP.setText("AP+" + hero.getHeroAttackPowerEnhancement());
        DP.setText("DP+" + hero.getHeroDefencePowerEnhancement());

        if (!hero.isInQuest()) {
            removeHero.setClickable(true);
            removeHero.setOnClickListener(v->
                    {
                        hero.setOnQuest(false);
                        switch (questHero) {
                            case 1 : quest.setQuestHero1(null);break;
                            case 2 : quest.setQuestHero2(null);break;
                            case 3 : quest.setQuestHero3(null);break;
                        }


                        heroLayout.setBackgroundColor(0);
                        heroInfo.setText("");
                        heroGIF.setImageResource(android.R.drawable.star_big_on);
                        AP.setText("AP+");
                        DP.setText("DP+");
                        removeHero.setClickable(false);
                        TextView questSummary =view.findViewById(R.id.Quest_Summary);
                        questSummary.setText(getQuestSummary ());
                        questMenuAdapter.notifyDataSetChanged();
                    });
        }


    }

    private String getQuestSummary () {
        int hero1AP = 0, hero1DP=0 ,hero2AP = 0, hero2DP=0,hero3AP = 0, hero3DP=0;
        timeDurationReduction = 0;

        if ( quest.getQuestHero1() != null) {
            hero1AP = 10 * quest.getQuestHero1().getLevel() + 5 * quest.getQuestHero1().getHeroAttackPower() + 10 * quest.getQuestHero1().getHeroAttackPowerEnhancement();
            hero1DP = quest.getQuestHero1().getHeroDefencePower() + 5 * quest.getQuestHero1().getHeroDefencePowerEnhancement();
        }
        if ( quest.getQuestHero2() != null) {
            hero2AP = 10 * quest.getQuestHero2().getLevel() + 5 * quest.getQuestHero2().getHeroAttackPower() + 10 * quest.getQuestHero2().getHeroAttackPowerEnhancement();
            hero2DP = quest.getQuestHero2().getHeroDefencePower() + 5 * quest.getQuestHero2().getHeroDefencePowerEnhancement();
        }
        if ( quest.getQuestHero3() != null) {
            hero3AP = 10 * quest.getQuestHero3().getLevel() + 5 * quest.getQuestHero3().getHeroAttackPower() + 10 * quest.getQuestHero3().getHeroAttackPowerEnhancement();
            hero3DP = quest.getQuestHero3().getHeroDefencePower() + 5 * quest.getQuestHero3().getHeroDefencePowerEnhancement();
        }

        teamAttackPower = hero1AP + hero2AP + hero3AP;
        teamDefencePower = hero1DP + hero2DP + hero3DP;

        enemyPower = quest.getNumberOfMobs() + 5 * quest.getNumberOfNormalBosses() + 10 * quest.getNumberOfEliteBosses() + 50 * quest.getNumberOfLegendaryBosses();
        teamDeathRate = quest.getHeroDeathRate() - teamDefencePower/10;



        computeHeroSkill( quest.getQuestHero1());
        computeHeroSkill( quest.getQuestHero2());
        computeHeroSkill( quest.getQuestHero3());

        if ( isWarriorMagePriestCombo ( quest.getQuestHero1(),quest.getQuestHero2(),quest.getQuestHero3() ) ) {
            teamAttackPower = teamAttackPower*2;
            teamDeathRate -= 5;
        }

        double questSuccessRate = teamAttackPower/enemyPower;

        quest.setQuestDuration((long) (initQuestDuration * ( 1 - timeDurationReduction)));
        quest.setQuestDeathRate((double) (teamDeathRate <= 0 ? 0 :  teamDeathRate));
        quest.setQuestSuccessRate(questSuccessRate >=1 ? 1 : questSuccessRate);

        //Log.i("", "AP : " + teamAttackPower);
        //Log.i("", "EP : " + enemyPower);
        //Log.i("", "Death Rate : " + quest.getQuestDeathRate());
        //Log.i("", "success rate : " + quest.getQuestSuccessRate());

        return  "Quest Summary : \n" +
                "Team Attack Power : " +  teamAttackPower + "\n" +
                "Team Defence Power : " +  teamDefencePower + "\n" +
                "Enemy Power : " + enemyPower + "\n" +
                "Team Death Rate : " +  teamDeathRate + "%\n" +
                "Quest Success  Rate : " +  questSuccessRate * 100 + "%";
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

}
