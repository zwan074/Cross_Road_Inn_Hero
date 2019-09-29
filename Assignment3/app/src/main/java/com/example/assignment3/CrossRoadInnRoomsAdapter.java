package com.example.assignment3;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Hero heroSelected = heroHired.get(position);

        GifImageView gifImageView = holder.layout.findViewById(R.id.HeroGIF);
        gifImageView.setImageResource(heroSelected.getHeroGifID());

        TextView heroInfo = (TextView) holder.layout.findViewById(R.id.HeroInfo);
        heroInfo.append("Hero Rarity: " + heroSelected.getHeroRarity()+ "\n");
        heroInfo.append("Hero Name: " + heroSelected.getHeroName()+ "\n");
        heroInfo.append("Hero Class: " + heroSelected.getHeroClass()+ "\n");
        heroInfo.append("Hero Lvl: " + heroSelected.getLevel()+ "\n");
        heroInfo.append("Hero Skill: " + heroSelected.getHeroSkill()+ "\n");
        heroInfo.append("Hero AP: " + heroSelected.getHeroAttackPower()+ "\n");
        heroInfo.append("Hero DP: " + heroSelected.getHeroDefencePower()+ "\n");
        heroInfo.append("Hero EXP: " + heroSelected.getExp());

        Button fireHero = (Button) holder.layout.findViewById(R.id.fireHero);
        fireHero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                heroHired.remove(position);
                MainActivity m = (MainActivity) activity;
                TextView totalHeroHired = (TextView)  m.findViewById(R.id.totalHeroHired);
                totalHeroHired.setText("Total Heros : " +  heroHired.size());

                TextView totalGold = (TextView)  m.findViewById(R.id.totalGold);
                totalGold.setText("Gold : " +  m.getGoldAmount());

                notifyItemRemoved(position);
                notifyItemRangeChanged(position,heroHired.size());

            }
        });

        final Button heroAP = (Button) holder.layout.findViewById(R.id.enhanceAP);
        heroAP.setText("AP+" +heroSelected.getHeroAttackPowerEnhancement());
        heroAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity m = (MainActivity) activity;
                AlertDialog.Builder dialog = new AlertDialog.Builder(m);

                if ( m.getGoldAmount() - (int) Math.pow(heroSelected.getHeroAttackPowerEnhancement(),2) >=0){
                    if (Math.random() > 0.5 ){
                        m.setGoldAmount(
                            m.getGoldAmount() - (int) Math.pow(heroSelected.getHeroAttackPowerEnhancement(), 2)
                        );
                        heroSelected.setHeroAttackPowerEnhancement(heroSelected.getHeroAttackPowerEnhancement()+1);
                        heroAP.setText("AP+" +heroSelected.getHeroAttackPowerEnhancement());
                        dialog.setMessage("Success Enhance AP").create().show();
                    }
                    else {
                        m.setGoldAmount(
                                m.getGoldAmount() - (int) Math.pow(heroSelected.getHeroAttackPowerEnhancement(), 2)
                        );
                        dialog.setMessage("Fail Enhance AP").create().show();
                    }
                }
                else {
                    dialog.setMessage("Not Enough Gold").create().show();
                }

                TextView totalGold = (TextView)  m.findViewById(R.id.totalGold);
                totalGold.setText("Gold : " +  m.getGoldAmount());

                TextView Gold = (TextView) m.findViewById(R.id.Gold) ;
                Gold.setText ("Gold : " +  m.getGoldAmount());


            }
        });

        final Button heroDP = (Button) holder.layout.findViewById(R.id.enhanceDP);
        heroDP.setText("DP+" +heroSelected.getHeroDefencePowerEnhancement());
        heroDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity m = (MainActivity) activity;
                AlertDialog.Builder dialog = new AlertDialog.Builder(m);

                if ( m.getGoldAmount() - (int) Math.pow(heroSelected.getHeroDefencePowerEnhancement(),2) >=0){
                    if (Math.random() > 0.5 ){
                        m.setGoldAmount(
                            m.getGoldAmount() - (int) Math.pow(heroSelected.getHeroDefencePowerEnhancement(), 2)
                        );
                        heroSelected.setHeroDefencePowerEnhancement(heroSelected.getHeroDefencePowerEnhancement()+1);
                        heroDP.setText("DP+" +heroSelected.getHeroDefencePowerEnhancement());
                        dialog.setMessage("Success Enhance DP").create().show();
                    }
                    else {
                        m.setGoldAmount(
                            m.getGoldAmount() - (int) Math.pow(heroSelected.getHeroDefencePowerEnhancement(), 2)
                        );
                        dialog.setMessage("Fail Enhance DP").create().show();
                    }
                }
                else {
                    dialog.setMessage("Not Enough Gold").create().show();
                }

                TextView totalGold = (TextView)  m.findViewById(R.id.totalGold);
                totalGold.setText("Gold : " +  m.getGoldAmount());

                TextView Gold = (TextView) m.findViewById(R.id.Gold) ;
                Gold.setText ("Gold : " +  m.getGoldAmount());


            }
        });




    }
    @Override
    public int getItemCount() {
        return heroHired.size();
    }
}
