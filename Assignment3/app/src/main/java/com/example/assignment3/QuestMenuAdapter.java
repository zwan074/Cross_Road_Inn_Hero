package com.example.assignment3;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.QuestObjects.Quest;

import java.io.Serializable;
import java.util.LinkedList;

import pl.droidsonroids.gif.GifImageView;

public class QuestMenuAdapter extends RecyclerView.Adapter<QuestMenuAdapter.MyViewHolder> implements Serializable {

    private LinkedList<Quest> quests;
    private Activity activity;

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
            "Quest Duration: " + questSelected.getQuestDuration()+ "\n" +
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


        hero1.setOnClickListener(v -> openQuestForHeroSelectionFragment(m,questSelected,1));
        hero2.setOnClickListener(v -> openQuestForHeroSelectionFragment(m,questSelected,2));
        hero3.setOnClickListener(v -> openQuestForHeroSelectionFragment(m,questSelected,3));

        Button viewTeamStats = holder.layout.findViewById(R.id.ViewTeamStats);
        viewTeamStats.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putSerializable("Quest For Team Stats", questSelected);
            bundle.putSerializable("Quest Menu Adapter", this);
            QuestForTeamStatsFragment fragment = new QuestForTeamStatsFragment();
            fragment.setArguments(bundle);
            m.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.quest_menu_layout, fragment).commit();});
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
}
