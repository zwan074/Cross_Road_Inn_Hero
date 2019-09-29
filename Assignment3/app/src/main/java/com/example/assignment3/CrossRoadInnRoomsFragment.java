package com.example.assignment3;

import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CrossRoadInnRoomsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cross_road_inn_rooms, container, false);
        MainActivity m = (MainActivity) getActivity();
        ConstraintLayout layout = view.findViewById(R.id.cross_road_inn_layout);

        ImageButton closeButton = (ImageButton) view.findViewById( R.id.close_cross_road_inn_rooms);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(CrossRoadInnRoomsFragment.this).commit();
                //Log.i("","clicked");
                //getFragmentManager().popBackStack();
            }
        });

        TextView totalHeroHired = (TextView)  view.findViewById(R.id.totalHeroHired);
        totalHeroHired.setText("Total Heros : " + m.herosHired.size());

        TextView totalGold = (TextView)  view.findViewById(R.id.totalGold);
        totalGold.setText("Gold : " +  m.GoldAmount);

        CrossRoadInnRoomsAdapter madapter = new CrossRoadInnRoomsAdapter(m.herosHired ,m) ;

        RecyclerView recyclerView  = (RecyclerView) view.findViewById(R.id.HeroInfoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(m));
        recyclerView.setAdapter(madapter);
        // Inflate the layout for this fragment
        return view;
    }


}
