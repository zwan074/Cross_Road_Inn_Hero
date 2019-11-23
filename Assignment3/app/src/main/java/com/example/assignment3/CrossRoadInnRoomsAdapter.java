package com.example.assignment3;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.HeroObjects.Hero;

import java.util.LinkedList;

import pl.droidsonroids.gif.GifImageView;

public class CrossRoadInnRoomsAdapter extends RecyclerView.Adapter<CrossRoadInnRoomsAdapter.MyViewHolder> {

    private LinkedList<Hero> heroHired;
    private Activity activity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout layout;
        public MyViewHolder(ConstraintLayout v) {
            super(v);
            layout = v;
        }
    }
    public CrossRoadInnRoomsAdapter(LinkedList<Hero> heroHired,  Activity activity) {
        this.activity = activity;
        this.heroHired = heroHired;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public CrossRoadInnRoomsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
    // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_hired_hero, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }
    // Replace the contents of a view (invoked by the layout manager)

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        //initialise components in this item
        final Hero heroSelected = heroHired.get(position);

        switch (heroSelected.getHeroRarity()) {
            case NORMAL: holder.layout.setBackgroundColor(Color.parseColor("#CCDDD1C6"));;break;
            case ELITE : holder.layout.setBackgroundColor(Color.parseColor("#CC00E5FF"));break;
            case LEGENDARY : holder.layout.setBackgroundColor(Color.parseColor("#CCFF9100"));break;
        }

        GifImageView gifImageView = holder.layout.findViewById(R.id.HeroGIF);
        gifImageView.setImageResource(heroSelected.getHeroGifID());

        final TextView heroInfo = holder.layout.findViewById(R.id.HeroInfo);
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

        //hero AP enhancement logic
        final Button heroAP = holder.layout.findViewById(R.id.enhanceAP);
        heroAP.setText("AP+" +heroSelected.getHeroAttackPowerEnhancement());
        heroAP.setOnClickListener(view -> {

            MainActivity m = (MainActivity) activity;
            AlertDialog.Builder dialog = new AlertDialog.Builder(m);
            int cost = (int) Math.pow(heroSelected.getHeroAttackPowerEnhancement(),2);
            if ( m.getGoldAmount() - cost >=0){
                if (Math.random() > 0.5 ){
                    m.setGoldAmount(
                        m.getGoldAmount() - cost
                    );
                    heroSelected.setHeroAttackPowerEnhancement(heroSelected.getHeroAttackPowerEnhancement()+1);
                    heroAP.setText("AP+" +heroSelected.getHeroAttackPowerEnhancement());
                    dialog.setMessage("Success Enhance AP and cost " + cost + " GOLD").create().show();
                }
                else {
                    m.setGoldAmount(
                            m.getGoldAmount() - cost
                    );
                    dialog.setMessage("Fail Enhance AP and cost " + cost + " GOLD").create().show();
                }
            }
            else {
                dialog.setMessage("Not Enough Gold, need " + cost + " GOLD for upgrading").create().show();
            }

            TextView totalGold = (TextView)  m.findViewById(R.id.Gold);
            totalGold.setText("Gold : " +  m.getGoldAmount());



        });

        //hero DP enhancement logic
        final Button heroDP = holder.layout.findViewById(R.id.enhanceDP);
        heroDP.setText("DP+" +heroSelected.getHeroDefencePowerEnhancement());
        heroDP.setOnClickListener(view -> {

            MainActivity m = (MainActivity) activity;
            AlertDialog.Builder dialog = new AlertDialog.Builder(m);
            int cost = (int) Math.pow(heroSelected.getHeroDefencePowerEnhancement(),2);
            if ( m.getGoldAmount() - cost >=0){
                if (Math.random() > 0.5 ){
                    m.setGoldAmount(
                        m.getGoldAmount() - cost
                    );
                    heroSelected.setHeroDefencePowerEnhancement(heroSelected.getHeroDefencePowerEnhancement()+1);
                    heroDP.setText("DP+" +heroSelected.getHeroDefencePowerEnhancement());
                    dialog.setMessage("Success Enhance DP and cost " + cost + " GOLD").create().show();
                }
                else {
                    m.setGoldAmount(
                        m.getGoldAmount() - cost
                    );
                    dialog.setMessage("Fail Enhance DP and cost " + cost + " GOLD").create().show();
                }
            }
            else {
                dialog.setMessage("Not Enough Gold, need " + cost + " GOLD for upgrading" ).create().show();
            }

            TextView totalGold = m.findViewById(R.id.Gold);
            totalGold.setText("Gold : " +  m.getGoldAmount());



        });

        //fire hero from list
        Button fireHero = holder.layout.findViewById(R.id.fireHero);
        if (!heroSelected.isOnQuest()) {
            fireHero.setOnClickListener(view -> {


                MainActivity m = (MainActivity) activity;
                AlertDialog.Builder dialog = new AlertDialog.Builder(m);

                heroInfo.setText("");
                if (heroHired.size() > 0) {
                    heroHired.remove(position);
                    TextView totalHeroHired = (TextView) m.findViewById(R.id.totalHeroHired);
                    totalHeroHired.setText("Total Heroes : " + heroHired.size());

                    TextView totalGold = (TextView) m.findViewById(R.id.Gold);
                    totalGold.setText("Gold : " + m.getGoldAmount());
                    dialog.setMessage(heroSelected.getHeroName() + "has been fired").create().show();
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());

                }

            });
        }
        else {
            fireHero.setText("On Quest");
            fireHero.setClickable(false);
            heroAP.setClickable(false);
            heroDP.setClickable(false);
        }


    }
    @Override
    public int getItemCount() {
        return heroHired.size();
    }
}
