package com.example.assignment3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CrossRoadInnRoomsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cross_road_inn_rooms, container, false);
        MainActivity m = (MainActivity) getActivity();

        ImageButton closeButton = view.findViewById( R.id.close_cross_road_inn_rooms);
        closeButton.setOnClickListener(v -> {
            getFragmentManager().beginTransaction().remove(CrossRoadInnRoomsFragment.this).commit();
        });

        TextView totalHeroHired =  m.findViewById(R.id.totalHeroHired);
        totalHeroHired.setText("Total Heros : " + m.herosHired.size());

        TextView totalGold = m.findViewById(R.id.Gold);
        totalGold.setText("Gold : " +  m.GoldAmount);

        CrossRoadInnRoomsAdapter madapter = new CrossRoadInnRoomsAdapter(m.herosHired ,m) ;

        RecyclerView recyclerView  = view.findViewById(R.id.HeroInfoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(m));
        recyclerView.setAdapter(madapter);
        // Inflate the layout for this fragment
        return view;
    }


}
