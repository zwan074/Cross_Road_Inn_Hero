package com.example.assignment3;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.QuestObjects.Quest;
import com.example.assignment3.QuestObjects.QuestDifficulty;
import com.example.assignment3.QuestObjects.QuestStatus;

import java.util.Date;
import java.util.stream.Collectors;

public class QuestMenuFragment extends Fragment {

    static final long THIRTY_MIN = 1000 * 60 * 30 ;
    static final long TWO_HOURS = 1000 * 60 * 60 * 2 ;
    static final long ONE_DAY = 1000 * 60 * 60 * 24 ;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.quest_menu, container, false);
        MainActivity m = (MainActivity) getActivity();
        generateQuests ();

        QuestMenuAdapter madapter = new QuestMenuAdapter(m.quests ,m) ;

        RecyclerView recyclerView  = view.findViewById(R.id.QuestMenuRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(m));
        recyclerView.setAdapter(madapter);

        ImageButton closeButton = view.findViewById(R.id.close_quest_menu);
        closeButton.setOnClickListener(v -> getFragmentManager().beginTransaction().remove(QuestMenuFragment.this).commit());


        return view;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void generateQuests () {

        MainActivity m = (MainActivity) getActivity();

        int numberOfNormalQuests =  m.quests.stream().filter(e ->  e.getQuestDifficulty() == QuestDifficulty.NORMAL).collect(Collectors.toList()).size();
        int numberOfEliteQuests = m.quests.stream().filter(e -> e.getQuestDifficulty() == QuestDifficulty.ELITE).collect(Collectors.toList()).size();
        int numberOfLegendaryQuests = m.quests.stream().filter(e -> e.getQuestDifficulty() == QuestDifficulty.LEGENDARY).collect(Collectors.toList()).size();

        if ( numberOfNormalQuests < 5  ) {
            for (int i = 0 ; i < 5 - numberOfNormalQuests; i++ ){
                Quest newNormalQuest = new Quest();
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
                m.quests.add(newNormalQuest);

            }

        }

        if ( numberOfEliteQuests < 4  ) {
            for (int i = 0 ; i < 4 - numberOfEliteQuests; i++ ){
                Quest newEliteQuest = new Quest();
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
                m.quests.add(newEliteQuest);

            }
        }

        if ( numberOfLegendaryQuests < 1  ) {

            Quest newLegendaryQuest = new Quest();
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
            m.quests.add(newLegendaryQuest);


        }


    }



}
