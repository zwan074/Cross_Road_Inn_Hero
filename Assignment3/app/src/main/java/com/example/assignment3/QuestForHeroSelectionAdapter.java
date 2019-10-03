package com.example.assignment3;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.HeroObjects.Hero;
import com.example.assignment3.QuestObjects.Quest;

import java.util.LinkedList;

import pl.droidsonroids.gif.GifImageView;

public class QuestForHeroSelectionAdapter extends RecyclerView.Adapter<QuestForHeroSelectionAdapter.MyViewHolder> {
    private LinkedList<Hero> heroHired;
    private Quest quest;
    private int heroPosition;
    private Activity activity;
    private QuestMenuAdapter questMenuAdapter;
    private QuestForHeroSelectionFragment questForHeroSelectionFragment;
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
    @Override
    public int getItemCount() {
        return heroHired.size();
    }


}
