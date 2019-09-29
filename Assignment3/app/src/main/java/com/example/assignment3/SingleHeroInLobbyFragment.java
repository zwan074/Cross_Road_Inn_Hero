package com.example.assignment3;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.assignment3.HeroObjects.Hero;
import com.example.assignment3.HeroObjects.HeroRarity;


public class SingleHeroInLobbyFragment extends Fragment {
    private static final String DESCRIBABLE_KEY = "HeroInfo";
    private Hero hero;
    private View view;

    public static SingleHeroInLobbyFragment newInstance(Hero hero) {

        SingleHeroInLobbyFragment fragment = new SingleHeroInLobbyFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DESCRIBABLE_KEY, hero);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        hero  = (Hero) getArguments().getSerializable(DESCRIBABLE_KEY);
        view = inflater.inflate(R.layout.single_hero_in_lobby, container, false);

        TextView heroInfo = (TextView) view.findViewById(R.id.hero_info);
        ImageButton closeButton = (ImageButton) view.findViewById(R.id.close_single_hero_in_lobby);
        Button hireButton = (Button) view.findViewById(R.id.hire);
        TextView goldToHire = (TextView) view.findViewById(R.id.GoldToHire);

        heroInfo.append("Hero Rarity: " + hero.getHeroRarity()+ "\n");
        heroInfo.append("Hero Name: " + hero.getHeroName()+ "\n");
        heroInfo.append("Hero Class: " + hero.getHeroClass()+ "\n");
        heroInfo.append("Hero Lvl: " + hero.getLevel()+ "\n");
        heroInfo.append("Hero Skill: " + hero.getHeroSkill()+ "\n");
        heroInfo.append("Hero AttackPower: " + hero.getHeroAttackPower()+ "\n");
        heroInfo.append("Hero DefencePower: " + hero.getHeroDefencePower()+ "\n");


        switch (hero.getHeroRarity()) {
            case NORMAL: goldToHire.setText("Gold : 10"  );break;
            case ELITE : goldToHire.setText("Gold : 100"  );break;
            case LEGENDARY: goldToHire.setText("Gold : 1000"  );break;
        }



        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(SingleHeroInLobbyFragment.this).commit();
                //Log.i("","clicked");
                //getFragmentManager().popBackStack();
            }
        });
        view.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction().remove(SingleHeroInLobbyFragment.this).commit();
                    //Log.i("","clicked");
                    //getFragmentManager().popBackStack();
                }
            }
        );
        hireButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MainActivity m = (MainActivity) getActivity();

                    if (hero.getHeroRarity() == HeroRarity.ELITE && m.GoldAmount >= 100) {
                        m.herosHired.add(hero);
                        m.GoldAmount -= 100;
                        TextView Gold = (TextView) m.findViewById(R.id.Gold) ;
                        Gold.setText("Gold : " + m.GoldAmount);
                        ConstraintLayout layout = m.findViewById(R.id.cross_road_inn_layout);
                        layout.removeView(hero.getHeroButton());
                        layout.removeView(hero.getHeroGif());
                    }

                    getFragmentManager().beginTransaction().remove(SingleHeroInLobbyFragment.this).commit();
                }
            }
        );
        // Inflate the layout for this fragment
        return view;
    }
}
