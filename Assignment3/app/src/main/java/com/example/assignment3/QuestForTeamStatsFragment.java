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
import com.example.assignment3.QuestObjects.Quest;

import pl.droidsonroids.gif.GifImageView;

public class QuestForTeamStatsFragment extends Fragment{

    private Quest quest;
    private MainActivity activity;
    private QuestMenuAdapter questMenuAdapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initialise component in this fragment
        view = inflater.inflate(R.layout.quest_team_stats, container, false);
        activity = (MainActivity) getActivity();
        quest = (Quest) this.getArguments().getSerializable("Quest For Team Stats");
        questMenuAdapter = (QuestMenuAdapter) this.getArguments().getSerializable("Quest Menu Adapter");

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

        //add or remove heroes
        if (quest.getQuestHero1()!=null) setComponents( quest.getQuestHero1(),hero1Layout,hero1GIF,hero1Info,hero1AP,hero1DP,hero1RemoveThis,1);
        if (quest.getQuestHero2()!=null) setComponents( quest.getQuestHero2(),hero2Layout,hero2GIF,hero2Info,hero2AP,hero2DP,hero2RemoveThis,2);
        if (quest.getQuestHero3()!=null) setComponents( quest.getQuestHero3(),hero3Layout,hero3GIF,hero3Info,hero3AP,hero3DP,hero3RemoveThis,3);

        //close fragment
        ImageButton closeTeamStats = view.findViewById(R.id.close_quest_team_stats);
        closeTeamStats.setOnClickListener(v->{
            //activity.getSupportFragmentManager().beginTransaction().replace(R.id.cross_road_inn_layout, new QuestMenuFragment()).commit()
            activity.getSupportFragmentManager().beginTransaction().remove(QuestForTeamStatsFragment.this).commit();
            activity.getSupportFragmentManager().popBackStack();
            }
        );

        //refreshing quest summary after hero has been added or removed
        TextView questSummary =view.findViewById(R.id.Quest_Summary);
        questSummary.setText(quest.getQuestSummary ());

        return view;
    }

    //logic for set up a single hero component in this fragment
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
                    questSummary.setText(quest.getQuestSummary ());
                    questMenuAdapter.notifyDataSetChanged();
                });
        }


    }


}
