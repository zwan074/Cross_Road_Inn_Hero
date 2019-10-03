package com.example.assignment3;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.assignment3.HeroObjects.Hero;
import com.example.assignment3.HeroObjects.HeroRarity;

import static com.example.assignment3.HeroObjects.HeroRarity.NORMAL;


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

        TextView heroInfo = view.findViewById(R.id.hero_info);
        ImageButton closeButton = view.findViewById(R.id.close_single_hero_in_lobby);
        Button hireButton =  view.findViewById(R.id.hire);
        TextView goldToHire = view.findViewById(R.id.GoldToHire);

        heroInfo.append("Hero Rarity: " + hero.getHeroRarity()+ "\n");
        heroInfo.append("Hero Name: " + hero.getHeroName()+ "\n");
        heroInfo.append("Hero Class: " + hero.getHeroClass()+ "\n");
        heroInfo.append("Hero Lvl: " + hero.getLevel()+ "\n");
        heroInfo.append("Hero Skill: " + hero.getHeroSkill()+ "\n");
        heroInfo.append("Hero AttackPower: " + hero.getHeroAttackPower()+ "\n");
        heroInfo.append("Hero DefencePower: " + hero.getHeroDefencePower()+ "\n");


        switch (hero.getHeroRarity()) {
            case NORMAL: heroInfo.setBackgroundColor(Color.parseColor("#CCDDD1C6"));goldToHire.setText("Gold : 20"  );break;
            case ELITE : heroInfo.setBackgroundColor(Color.parseColor("#CC00E5FF"));goldToHire.setText("Gold : 100"  );break;
            case LEGENDARY : heroInfo.setBackgroundColor(Color.parseColor("#CCFF9100"));goldToHire.setText("Gold : 500"  );break;
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
                    AlertDialog.Builder dialog = new AlertDialog.Builder(m);
                    if ( m.herosHired.size() < 10) {
                        if (hero.getHeroRarity() == NORMAL) {
                            if ( m.GoldAmount >= 20) {
                                m.GoldAmount -= 20;
                                hireHero (m);
                                dialog.setMessage("A " + hero.getHeroRarity() + " hero hired and cost 20 GOLD").create().show();
                            }
                            else {
                                dialog.setMessage("Not Enough Gold").create().show();

                            }
                        }

                        else if (hero.getHeroRarity() == HeroRarity.ELITE ) {
                            if ( m.GoldAmount >= 100) {
                                m.GoldAmount -= 100;
                                hireHero(m);
                                dialog.setMessage("An " + hero.getHeroRarity() + " hero hired and cost 100 GOLD").create().show();
                            }
                            else {
                                dialog.setMessage("Not Enough Gold").create().show();
                            }
                        }
                        else if (hero.getHeroRarity() == HeroRarity.LEGENDARY ) {
                            if ( m.GoldAmount >= 500) {
                                m.GoldAmount -= 500;
                                hireHero(m);
                                dialog.setMessage("A " + hero.getHeroRarity() + " hero hired and cost 500 GOLD").create().show();
                            }
                            else {
                                dialog.setMessage("Not Enough Gold").create().show();
                            }
                        }


                        getFragmentManager().beginTransaction().remove(SingleHeroInLobbyFragment.this).commit();
                    }
                    else {
                        dialog.setMessage("Out of Rooms for New Hero").create().show();
                    }

                }
            }
        );
        // Inflate the layout for this fragment
        return view;
    }

    private void hireHero (MainActivity m) {
        m.herosHired.add(hero);
        m.herosAtLobby.remove(hero);
        TextView Gold = (TextView) m.findViewById(R.id.Gold) ;
        Gold.setText("Gold : " + m.GoldAmount);
        TextView totalHeroHired = (TextView)  m.findViewById(R.id.totalHeroHired);
        totalHeroHired.setText("Total Heroes : " +  m.herosHired.size());

        ConstraintLayout layout = m.findViewById(R.id.cross_road_inn_layout);
        layout.removeView(hero.getHeroButton());
        layout.removeView(hero.getHeroGif());

        Log.i("" , "hero At Lobby : " + m.herosAtLobby.size());
    }
}
