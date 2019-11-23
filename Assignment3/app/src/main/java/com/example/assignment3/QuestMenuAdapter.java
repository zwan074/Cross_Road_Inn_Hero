package com.example.assignment3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.HeroObjects.Hero;
import com.example.assignment3.QuestObjects.Quest;
import com.example.assignment3.QuestObjects.QuestDifficulty;
import com.example.assignment3.QuestObjects.QuestStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import pl.droidsonroids.gif.GifImageView;

public class QuestMenuAdapter extends RecyclerView.Adapter<QuestMenuAdapter.MyViewHolder> implements Serializable {

    private LinkedList<Quest> quests;
    private Activity activity;

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
    public QuestMenuAdapter(LinkedList<Quest> quests,  Activity activity) {
        this.activity = activity;
        this.quests = quests;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public QuestMenuAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_quest, parent, false);
        QuestMenuAdapter.MyViewHolder vh = new QuestMenuAdapter.MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final QuestMenuAdapter.MyViewHolder holder, final int position) {

        //initialise components in this item
        final Quest questSelected = quests.get(position);
        MainActivity m = (MainActivity) activity;

        TextView questInfo = holder.layout.findViewById(R.id.QuestInfo);
        questInfo.setText(
            "Quest Name: " + questSelected.getQuestName()+ "\n" +
            "Quest Difficulty: " + questSelected.getQuestDifficulty()+ "\n" +
            "Quest Duration: " + getDayMinSecDuration(questSelected.getQuestDuration())+ "\n" +
            "Number of Mobs: " + questSelected.getNumberOfMobs()+ "\n" +
            "Number of Normal Bosses: " + questSelected.getNumberOfNormalBosses()+ "\n" +
            "Number of Elite Bosses: " + questSelected.getNumberOfEliteBosses()+ "\n" +
            "Number of Legendary Bosses: " + questSelected.getNumberOfLegendaryBosses()+ "\n" +
            "Gold Reward: " + questSelected.getGoldReward() + "\n" +
            "Exp Reward: " + questSelected.getExpReward() + "\n"
        );

        switch (questSelected.getQuestDifficulty()) {
            case NORMAL: holder.layout.setBackgroundColor(Color.parseColor("#CCDDD1C6"));break;
            case ELITE : holder.layout.setBackgroundColor(Color.parseColor("#CC00E5FF"));break;
            case LEGENDARY : holder.layout.setBackgroundColor(Color.parseColor("#CCFF9100"));
                questInfo.append ( "Difficulty factor: " + questSelected.getQuestDifficultyFactor()) ;break;
        }

        GifImageView hero1 = holder.layout.findViewById(R.id.questHero1);
        GifImageView hero2 = holder.layout.findViewById(R.id.questHero2);
        GifImageView hero3 = holder.layout.findViewById(R.id.questHero3);
        hero1.setImageResource(android.R.drawable.star_big_on);
        hero2.setImageResource(android.R.drawable.star_big_on);
        hero3.setImageResource(android.R.drawable.star_big_on);

        if (questSelected.getQuestHero1() != null ) {
            hero1.setImageResource(questSelected.getQuestHero1().getHeroGifID());
        }
        if (questSelected.getQuestHero2() != null ) {
            hero2.setImageResource(questSelected.getQuestHero2().getHeroGifID());
        }
        if (questSelected.getQuestHero3() != null ) {
            hero3.setImageResource(questSelected.getQuestHero3().getHeroGifID());
        }


        Button viewTeamStats = holder.layout.findViewById(R.id.ViewTeamStats);
        viewTeamStats.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putSerializable("Quest For Team Stats", questSelected);
            bundle.putSerializable("Quest Menu Adapter", this);
            QuestForTeamStatsFragment fragment = new QuestForTeamStatsFragment();
            fragment.setArguments(bundle);
            m.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.quest_menu_layout, fragment).commit();
        });



        long timeNow = new Date().getTime();
        long finishTime = 0;
        if (questSelected.getQuestStartTime() != null)  finishTime = questSelected.getQuestStartTime() + questSelected.getQuestDuration();
        Hero questHero1 = questSelected.getQuestHero1();
        Hero questHero2 = questSelected.getQuestHero2();
        Hero questHero3 = questSelected.getQuestHero3();

        Log.i("", " quest Number : " + position);
        Log.i("", " quest status : " + questSelected.getQuestStatus());
        Log.i("", " quest hero : " + questHero1 + " " + questHero2 + " " + questHero3);

        // Start and complete quest logic
        Button startQuest = holder.layout.findViewById(R.id.Start);
        startQuest.setClickable(false);
        if ( questSelected.getQuestStatus() == QuestStatus.IDLE  ) {

            hero1.setOnClickListener(v -> openQuestForHeroSelectionFragment(m,questSelected,1));
            hero2.setOnClickListener(v -> openQuestForHeroSelectionFragment(m,questSelected,2));
            hero3.setOnClickListener(v -> openQuestForHeroSelectionFragment(m,questSelected,3));


            startQuest.setText( "Start Quest" );
            if (questHero1 != null ||  questHero2 !=null || questHero3 != null) {
                startQuest.setOnClickListener(v-> {
                    questSelected.setQuestStartTime(new Date().getTime());
                    questSelected.setQuestStatus(QuestStatus.ONGOING);
                    if (questHero1 != null) questHero1.setInQuest(true);
                    if (questHero2 != null) questHero2.setInQuest(true);
                    if (questHero3 != null) questHero3.setInQuest(true);
                    hero1.setClickable(false);
                    hero2.setClickable(false);
                    hero3.setClickable(false);
                    //Log.i("", " quest hero : " + questHero1 + " " + questHero2 + " " + questHero3);
                    notifyItemChanged(position);
                });
            }

        }
        else if ( questSelected.getQuestStatus() == QuestStatus.ONGOING && finishTime > timeNow ) {

            startQuest.setText( "" + getDayMinSecDuration(finishTime - timeNow) );
            startQuest.setClickable(false);

            hero1.setClickable(false);
            hero2.setClickable(false);
            hero3.setClickable(false);

        }
        else if ( questSelected.getQuestStatus() == QuestStatus.ONGOING && finishTime <= timeNow   )   {

            startQuest.setText( "COMPLETE" );
            startQuest.setClickable(true);

            hero1.setClickable(false);
            hero2.setClickable(false);
            hero3.setClickable(false);


            if ( Math.random() <= questSelected.getQuestSuccessRate()) {
                startQuest.setOnClickListener(v-> {

                    completeSuccessQuest( questHero1,  questHero2 , questHero3, questSelected,  m,  position);

                });
            }
            else {
                startQuest.setOnClickListener(v-> {
                    completeFailQuest (questHero1, questHero2 ,questHero3, questSelected,  m, position);
                });
            }
        }

        //dummy button to complete quest immediately for testing
        Button completeNow = holder.layout.findViewById(R.id.CompleteNow);
        completeNow.setClickable(false);
        if ( questSelected.getQuestStatus() == QuestStatus.ONGOING) {

            completeNow.setClickable(true);
            if ( Math.random() <= questSelected.getQuestSuccessRate()) {
                completeNow.setOnClickListener(v-> {
                    completeSuccessQuest( questHero1,  questHero2 , questHero3, questSelected,  m,  position);

                });

            }
            else {
                completeNow.setOnClickListener(v-> {
                    completeFailQuest (questHero1, questHero2 ,questHero3, questSelected,  m,position);
                });


            }
        }
    }

    // logic for quest success after completion
    private void completeSuccessQuest (Hero questHero1, Hero questHero2 ,Hero questHero3,
                                       Quest questSelected, MainActivity m, int position) {

        m.GoldAmount += questSelected.getGoldReward();
        TextView Gold = m.findViewById(R.id.Gold);
        Gold.setText("Gold : " + m.GoldAmount);

        if ( questHero1 != null ) {
            questHero1.setLevel( questHero1.getLevel() + (questHero1.getExp() + questSelected.getExpReward())/100 >= 10 ? 10 :
                    questHero1.getLevel() + (questHero1.getExp() + questSelected.getExpReward())/100);
            questHero1.setExp((questHero1.getExp() + questSelected.getExpReward())%100);
            questHero1.setInQuest(false);
            questHero1.setOnQuest(false);
        }
        if ( questHero2 != null ) {
            questHero2.setLevel( questHero2.getLevel() + (questHero2.getExp() + questSelected.getExpReward())/100 >= 10 ? 10 :
                    questHero2.getLevel() + (questHero2.getExp() + questSelected.getExpReward())/100);
            questHero2.setExp((questHero2.getExp() + questSelected.getExpReward())%100);
            questHero2.setInQuest(false);
            questHero2.setOnQuest(false);
        }

        if ( questHero3 != null ) {
            questHero3.setLevel( questHero3.getLevel() + (questHero3.getExp() + questSelected.getExpReward())/100 >= 10 ? 10 :
                    questHero3.getLevel() + (questHero3.getExp() + questSelected.getExpReward())/100);
            questHero3.setExp((questHero3.getExp() + questSelected.getExpReward())%100);
            questHero3.setInQuest(false);
            questHero3.setOnQuest(false);
        }

        if (questSelected.getQuestDifficulty() == QuestDifficulty.LEGENDARY) {
            m.difficultyFactor += 1;
        }


        AlertDialog.Builder dialog = new AlertDialog.Builder(m);
        dialog.setMessage("Quest Success \n" +
                "Gold Reward: " +  questSelected.getGoldReward() + "\n" +
                "Exp Reward: " +  questSelected.getExpReward() + "\n" ).create().show();

        quests.set(position, generateReplacementQuest (quests.get(position)));
        notifyItemChanged(position);

    }

    // logic for quest fail after completion
    private void completeFailQuest (Hero questHero1, Hero questHero2 ,Hero questHero3,
                          Quest questSelected, MainActivity m, int position) {

        Log.i("", "Fail Quest :" );

        if ( questHero1 != null ) {
            questHero1.setInQuest(false);
            questHero1.setOnQuest(false);
        }
        if ( questHero2 != null ) {
            questHero2.setInQuest(false);
            questHero2.setOnQuest(false);
        }

        if ( questHero3 != null ) {
            questHero3.setInQuest(false);
            questHero3.setOnQuest(false);
        }
        boolean hero1Dead = false;
        boolean hero2Dead = false;
        boolean hero3Dead = false;


        if (questHero1 != null && Math.random() * 100 < questSelected.getQuestDeathRate()){
            m.herosHired.remove(questHero1);
            hero1Dead = true;
        }
        if (questHero2 != null && Math.random() * 100 < questSelected.getQuestDeathRate()){
            m.herosHired.remove(questHero2);
            hero2Dead = true;
        }
        if (questHero3 != null && Math.random() * 100 < questSelected.getQuestDeathRate()){
            m.herosHired.remove(questHero3);
            hero3Dead = true;
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(m);
        dialog.setMessage(
                "Quest Fail \n" +
                "Gold Reward: 0 \n" +
                "Exp Reward: 0 \n" +
                (hero1Dead  ?
                        questSelected.getQuestHero1().getHeroRarity() + " "  +
                        questSelected.getQuestHero1().getHeroClass() + " " +
                        questSelected.getQuestHero1().getHeroName() + " was dead in Quest\n": "" ) +
                (hero2Dead  ?
                        questSelected.getQuestHero2().getHeroRarity() + " "  +
                        questSelected.getQuestHero2().getHeroClass() + " " +
                        questSelected.getQuestHero2().getHeroName() + " was dead in Quest\n": "" ) +
                (hero3Dead  ?
                        questSelected.getQuestHero3().getHeroRarity() + " "  +
                        questSelected.getQuestHero3().getHeroClass() + " " +
                        questSelected.getQuestHero3().getHeroName() + " was dead in Quest\n": "" )
                ).create().show();

        quests.set(position, generateReplacementQuest (quests.get(position)));
        notifyItemChanged(position);




    }


    // open fragment for pick up heroes for selected quest
    private void openQuestForHeroSelectionFragment(MainActivity m, Quest questSelected, int heroPosition ) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("Quest For Selection of hero", questSelected);
        bundle.putSerializable("Quest Menu Adapter", this);
        bundle.putInt("Quest For Position of hero", heroPosition);
        QuestForHeroSelectionFragment fragment = new QuestForHeroSelectionFragment();
        fragment.setArguments(bundle);
        m.getSupportFragmentManager().beginTransaction()
                .replace(R.id.quest_menu_layout, fragment).commit();

    }

    @Override
    public int getItemCount() {
        return quests.size();
    }

    //transfer time format
    private String getDayMinSecDuration ( long time) {

        long days = TimeUnit.MILLISECONDS.toDays(time);
        long hours = TimeUnit.MILLISECONDS.toHours(time) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time) % 60;

        return  days + " day " + hours + " hrs " + minutes + " mins";
    }

    //generate a replacement quest after completion of existing quest
    private Quest generateReplacementQuest (Quest quest) {

        MainActivity m = (MainActivity) activity;
        Log.i("","Generate New Quest?");

        if ( quest.getQuestDifficulty() == QuestDifficulty.NORMAL  ) {

            Quest newNormalQuest = new Quest();
            newNormalQuest.setQuestName(quest.getQuestName());
            newNormalQuest.setQuestDuration(THIRTY_MIN);
            newNormalQuest.setQuestStatus(QuestStatus.IDLE);
            newNormalQuest.setHeroDeathRate(0);
            newNormalQuest.setNumberOfMobs((int) (Math.random() * 100  + 20));
            newNormalQuest.setQuestDifficulty(QuestDifficulty.NORMAL);
            newNormalQuest.setNumberOfNormalBosses((int) (Math.random() * 100 % 10));
            newNormalQuest.setNumberOfEliteBosses((int) (Math.random() * 100 % 10));
            newNormalQuest.setNumberOfLegendaryBosses(0);
            newNormalQuest.setGoldReward( 10 * newNormalQuest.getNumberOfMobs() + 50 * newNormalQuest.getNumberOfNormalBosses()
                    + 300 * newNormalQuest.getNumberOfEliteBosses() * m.difficultyFactor);
            newNormalQuest.setExpReward((int) (0.1 * newNormalQuest.getNumberOfMobs()  + 5  * newNormalQuest.getNumberOfNormalBosses() +
                    15 * newNormalQuest.getNumberOfEliteBosses()));
            return newNormalQuest;

        }

        else if ( quest.getQuestDifficulty() == QuestDifficulty.ELITE  ) {

            Quest newEliteQuest = new Quest();
            newEliteQuest.setQuestName(quest.getQuestName());
            newEliteQuest.setQuestDuration(TWO_HOURS);
            newEliteQuest.setQuestStatus(QuestStatus.IDLE);
            newEliteQuest.setHeroDeathRate(10);
            newEliteQuest.setNumberOfMobs((int) (Math.random() * 200  + 50));
            newEliteQuest.setQuestDifficulty(QuestDifficulty.ELITE);
            newEliteQuest.setNumberOfNormalBosses((int) (Math.random() * 100 % 25));
            newEliteQuest.setNumberOfEliteBosses((int) (Math.random() * 100 % 15));
            newEliteQuest.setNumberOfLegendaryBosses((int) (Math.random() * 100 % 3));
            newEliteQuest.setGoldReward(10 * newEliteQuest.getNumberOfMobs() + 50 * newEliteQuest.getNumberOfNormalBosses()
                    + 200 * newEliteQuest.getNumberOfEliteBosses() * m.difficultyFactor
                    + 1000 *  newEliteQuest.getNumberOfLegendaryBosses() * m.difficultyFactor);
            newEliteQuest.setExpReward((int) (0.1 * newEliteQuest.getNumberOfMobs()  + 5  * newEliteQuest.getNumberOfNormalBosses() +
                    15 * newEliteQuest.getNumberOfEliteBosses() + 30 *  newEliteQuest.getNumberOfLegendaryBosses()));
            return newEliteQuest;


        }

        else if (quest.getQuestDifficulty() == QuestDifficulty.LEGENDARY  ) {

            Quest newLegendaryQuest = new Quest();
            newLegendaryQuest.setQuestName(quest.getQuestName());
            newLegendaryQuest.setQuestDuration(ONE_DAY);
            newLegendaryQuest.setQuestStatus(QuestStatus.IDLE);
            newLegendaryQuest.setHeroDeathRate(20);
            newLegendaryQuest.setNumberOfMobs((int) (Math.random() * 300  + 100));
            newLegendaryQuest.setQuestDifficulty(QuestDifficulty.LEGENDARY);
            newLegendaryQuest.setNumberOfNormalBosses((int) (Math.random() * 100 % 45));
            newLegendaryQuest.setNumberOfEliteBosses((int) (Math.random() * 100 % 25));
            newLegendaryQuest.setNumberOfLegendaryBosses((int) (Math.random() * 100 % 5 + 1));
            newLegendaryQuest.setGoldReward(10 * newLegendaryQuest.getNumberOfMobs() + 50 * newLegendaryQuest.getNumberOfNormalBosses()
                    + m.difficultyFactor * 200 * newLegendaryQuest.getNumberOfEliteBosses()
                    + m.difficultyFactor * 1000 *  newLegendaryQuest.getNumberOfLegendaryBosses());
            newLegendaryQuest.setExpReward((int) (0.1 * newLegendaryQuest.getNumberOfMobs()  + 5  * newLegendaryQuest.getNumberOfNormalBosses() +
                    15 * newLegendaryQuest.getNumberOfEliteBosses() + 30 *  newLegendaryQuest.getNumberOfLegendaryBosses()));
            newLegendaryQuest.setQuestDifficultyFactor(m.difficultyFactor);
            return newLegendaryQuest;


        }

        return null;
    }



}
