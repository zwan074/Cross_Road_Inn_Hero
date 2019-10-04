package com.example.assignment3;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.HeroObjects.Hero;
import com.example.assignment3.HeroObjects.HeroClass;
import com.example.assignment3.HeroObjects.HeroSkill;
import com.example.assignment3.QuestObjects.Quest;

import java.util.ArrayList;
import java.util.LinkedList;

import pl.droidsonroids.gif.GifImageView;

public class QuestForHeroSelectionAdapter extends RecyclerView.Adapter<QuestForHeroSelectionAdapter.MyViewHolder> {
    private LinkedList<Hero> heroHired;
    private Quest quest;
    private int heroPosition;
    private Activity activity;
    private QuestMenuAdapter questMenuAdapter;
    private QuestForHeroSelectionFragment questForHeroSelectionFragment;

    private int teamAttackPower;
    private int teamDefencePower;
    private int enemyPower;
    private int teamDeathRate;
    private double timeDurationReduction;
    private long initQuestDuration;

    static final long THIRTY_MIN = 1000 * 60 * 30 ;
    static final long TWO_HOURS = 1000 * 60 * 60 * 2 ;
    static final long ONE_DAY = 1000 * 60 * 60 * 24 ;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout layout;
        public MyViewHolder(ConstraintLayout v) {
            super(v);
            layout = v;
        }
    }

    public QuestForHeroSelectionAdapter(LinkedList<Hero> heroHired,  Activity activity,
                                        Quest quest,int heroPosition,QuestMenuAdapter questMenuAdapter,
                                         QuestForHeroSelectionFragment questForHeroSelectionFragment   ) {
        this.activity = activity;
        this.heroHired = heroHired;
        this.quest = quest;
        this.heroPosition = heroPosition;
        this.questMenuAdapter = questMenuAdapter;
        this.questForHeroSelectionFragment = questForHeroSelectionFragment;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public QuestForHeroSelectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quest_for_hero_selection, parent, false);
        QuestForHeroSelectionAdapter.MyViewHolder vh = new QuestForHeroSelectionAdapter.MyViewHolder(v);
        return vh;
    }
    // Replace the contents of a view (invoked by the layout manager)

    @Override
    public void onBindViewHolder(final QuestForHeroSelectionAdapter.MyViewHolder holder, final int position) {
        final Hero heroSelected = heroHired.get(position);
        MainActivity m = (MainActivity) activity;

        switch (heroSelected.getHeroRarity()) {
            case NORMAL: holder.layout.setBackgroundColor(Color.parseColor("#CCDDD1C6"));;break;
            case ELITE : holder.layout.setBackgroundColor(Color.parseColor("#CC00E5FF"));break;
            case LEGENDARY : holder.layout.setBackgroundColor(Color.parseColor("#CCFF9100"));break;
        }

        GifImageView gifImageView = holder.layout.findViewById(R.id.QuestHeroGIF);
        gifImageView.setImageResource(heroSelected.getHeroGifID());

        final TextView heroInfo = holder.layout.findViewById(R.id.QuestHeroInfo);
        heroInfo.setText(
                "Rarity: " + heroSelected.getHeroRarity()+ "\n" +
                "Name: " + heroSelected.getHeroName()+ "\n" +
                "Class: " + heroSelected.getHeroClass()+ "\n" +
                "Lvl: " + heroSelected.getLevel()+ "\n" +
                "Skill: " + heroSelected.getHeroSkill()+ "\n" +
                "AP: " + heroSelected.getHeroAttackPower()+ "\n" +
                "DP: " + heroSelected.getHeroDefencePower()+ "\n" +
                "EXP: " + heroSelected.getExp()
        );

        Button pickHero = holder.layout.findViewById(R.id.isOnQuest);
        if (heroSelected.isOnQuest()) {

            pickHero.setText("On Quest");
            pickHero.setClickable(false);

        }

        else {

            pickHero.setText("Pick");
            pickHero.setOnClickListener(view -> {


                heroSelected.setOnQuest(true);

                switch (heroPosition) {
                    case 1:
                        if (quest.getQuestHero1() != null) quest.getQuestHero1().setOnQuest(false);
                        quest.setQuestHero1(heroSelected);
                        break;
                    case 2:
                        if (quest.getQuestHero2() != null) quest.getQuestHero2().setOnQuest(false);
                        quest.setQuestHero2(heroSelected);
                        break;
                    case 3:
                        if (quest.getQuestHero3() != null) quest.getQuestHero3().setOnQuest(false);
                        quest.setQuestHero3(heroSelected);
                        break;
                }

                switch (quest.getQuestDifficulty() ) {
                    case NORMAL: initQuestDuration = THIRTY_MIN ;break;
                    case ELITE: initQuestDuration = TWO_HOURS ;break;
                    case LEGENDARY: initQuestDuration = ONE_DAY ;break;
                }
                /*
                computeQuestTimeReduction (quest.getQuestHero1());
                computeQuestTimeReduction (quest.getQuestHero2());
                computeQuestTimeReduction (quest.getQuestHero3());

                quest.setQuestDuration((long) (initQuestDuration * ( 1 - timeDurationReduction)));
                */
                updateQuestData ();
                notifyItemChanged(position);
                questMenuAdapter.notifyDataSetChanged();
                m.getSupportFragmentManager().beginTransaction().remove(questForHeroSelectionFragment).commit();
                m.getSupportFragmentManager().popBackStack();

            });
        }

        Button heroAP = holder.layout.findViewById(R.id.QuestHeroEnhanceAP);
        heroAP.setText("AP+" +heroSelected.getHeroAttackPowerEnhancement());

        Button heroDP = holder.layout.findViewById(R.id.QuestHeroEnhanceDP);
        heroDP.setText("DP+" +heroSelected.getHeroDefencePowerEnhancement());

    }

    private void computeQuestTimeReduction (Hero hero) {
        if (hero != null) {
            if (hero.getHeroSkill() == HeroSkill.FIREBALL) this.timeDurationReduction += 0.15;
        }
    }

    @Override
    public int getItemCount() {
        return heroHired.size();
    }

    private void updateQuestData () {
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

        Log.i("", "AP : " + teamAttackPower);
        Log.i("", "EP : " + enemyPower);
        Log.i("", "Death Rate : " + quest.getQuestDeathRate());
        Log.i("", "success rate : " + quest.getQuestSuccessRate());

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
