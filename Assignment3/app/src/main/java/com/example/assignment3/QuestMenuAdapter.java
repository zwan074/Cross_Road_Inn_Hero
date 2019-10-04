package com.example.assignment3;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import com.example.assignment3.QuestObjects.QuestDifficulty;
import com.example.assignment3.QuestObjects.QuestStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

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
        final Quest questSelected = quests.get(position);
        MainActivity m = (MainActivity) activity;
        //Log.i("","" + position + " " + questSelected.getQuestHero1() + " " + questSelected.getQuestHero2() + " " + questSelected.getQuestHero3());
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
                    .replace(R.id.quest_menu_layout, fragment).commit();});

        Button startQuest = holder.layout.findViewById(R.id.Start);

        long timeNow = new Date().getTime();
        long finishTime = 0;
        if (questSelected.getQuestStartTime() != null)  finishTime = questSelected.getQuestStartTime() + questSelected.getQuestDuration();
        Hero questHero1 = questSelected.getQuestHero1();
        Hero questHero2 = questSelected.getQuestHero2();
        Hero questHero3 = questSelected.getQuestHero3();
        //Log.i("", " quest status : " + questSelected.getQuestStatus());
        //Log.i("", " quest hero : " + questHero1 + " " + questHero2 + " " + questHero3);

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
                    //Log.i("", " quest hero : " + questHero1 + " " + questHero2 + " " + questHero3);
                    notifyItemChanged(position);
                });
            }

        }
        else if ( questSelected.getQuestStatus() == QuestStatus.ONGOING && finishTime > timeNow ) {
            startQuest.setText( "" + getDayMinSecDuration(finishTime - timeNow) );
            startQuest.setClickable(false);
        }
        else if ( questSelected.getQuestStatus() == QuestStatus.ONGOING && finishTime <= timeNow   )   {
            startQuest.setText( "COMPLETE" );
            questSelected.setQuestStatus(QuestStatus.COMPLETE);
            startQuest.setClickable(true);
            if ( Math.random() <= questSelected.getQuestSuccessRate()) {
                startQuest.setOnClickListener(v-> {

                    m.GoldAmount += questSelected.getGoldReward();

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
                    quests.remove(position);
                    notifyItemChanged(position);
                    notifyItemRangeChanged(position, getItemCount());


                });
            }
            else {
                startQuest.setOnClickListener(v-> {
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

                    if (questHero1 != null && Math.random() * 100 < questSelected.getQuestDeathRate()){
                        m.herosHired.remove(questHero1);
                    }
                    if (questHero2 != null && Math.random() * 100 < questSelected.getQuestDeathRate()){
                        m.herosHired.remove(questHero2);
                    }
                    if (questHero3 != null && Math.random() * 100 < questSelected.getQuestDeathRate()){
                        m.herosHired.remove(questHero3);
                    }
                    quests.remove(position);
                    notifyItemChanged(position);
                    notifyItemRangeChanged(position, getItemCount());

                });


            }

        }
        if ( questSelected.getQuestStatus() == QuestStatus.ONGOING) {
            Button completeNow = holder.layout.findViewById(R.id.CompleteNow);
            if ( Math.random() <= questSelected.getQuestSuccessRate()) {
                completeNow.setOnClickListener(v-> {

                    m.GoldAmount += questSelected.getGoldReward();

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
                    quests.remove(position);
                    notifyItemChanged(position);
                    notifyItemRangeChanged(position, getItemCount());


                });
            }
            else {
                completeNow.setOnClickListener(v-> {
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

                    if (questHero1 != null && Math.random() * 100 < questSelected.getQuestDeathRate()){
                        m.herosHired.remove(questHero1);
                    }
                    if (questHero2 != null && Math.random() * 100 < questSelected.getQuestDeathRate()){
                        m.herosHired.remove(questHero2);
                    }
                    if (questHero3 != null && Math.random() * 100 < questSelected.getQuestDeathRate()){
                        m.herosHired.remove(questHero3);
                    }
                    quests.remove(position);
                    notifyItemChanged(position);
                    notifyItemRangeChanged(position, getItemCount());

                });


            }
        }






    }

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

    private String getDayMinSecDuration ( long time) {

        long days = TimeUnit.MILLISECONDS.toDays(time);
        long hours = TimeUnit.MILLISECONDS.toHours(time) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time) % 60;

        return  days + " day " + hours + " hrs " + minutes + " mins";
    }
}
