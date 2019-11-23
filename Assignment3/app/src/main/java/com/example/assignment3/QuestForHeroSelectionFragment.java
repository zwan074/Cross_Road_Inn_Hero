package com.example.assignment3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.QuestObjects.Quest;

public class QuestForHeroSelectionFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initialise components
        View view = inflater.inflate(R.layout.quest_menu_for_hero_info, container, false);
        MainActivity m = (MainActivity) getActivity();

        //close this fragment
        ImageButton closeButton = view.findViewById( R.id.close_quest_menu_for_hero_info);
        closeButton.setOnClickListener(v -> {
            m.getSupportFragmentManager().beginTransaction().remove(QuestForHeroSelectionFragment.this).commit();
            m.getSupportFragmentManager().popBackStack();
        });

        //pass variables and set up adapter
        Quest q = (Quest) this.getArguments().getSerializable("Quest For Selection of hero");
        int heroPosition = this.getArguments().getInt("Quest For Position of hero");
        QuestMenuAdapter questMenuAdapter = (QuestMenuAdapter) this.getArguments().getSerializable("Quest Menu Adapter");
        QuestForHeroSelectionAdapter madapter = new QuestForHeroSelectionAdapter(m.herosHired ,m,q,heroPosition,questMenuAdapter,this) ;

        RecyclerView recyclerView  = view.findViewById(R.id.QuestMenuForHeroInfoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(m));
        recyclerView.setAdapter(madapter);

        return view;
    }


}
