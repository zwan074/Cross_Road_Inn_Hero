package com.example.assignment3;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.assignment3.HeroObjects.Hero;
import com.example.assignment3.HeroObjects.HeroClass;
import com.example.assignment3.HeroObjects.HeroRarity;
import com.example.assignment3.HeroObjects.HeroSkill;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {
    LinkedList<Hero> herosAtLobby = new LinkedList<>();
    LinkedList<Hero> herosHired = new LinkedList<>();
    ArrayList<HeroClass> heroClasses = new ArrayList<>();
    int GoldAmount = 10000;

    public int getGoldAmount() {
        return GoldAmount;
    }

    public void setGoldAmount(int goldAmount) {
        GoldAmount = goldAmount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout layout = findViewById(R.id.cross_road_inn_layout);

        Button inn_boss = (Button) findViewById(R.id.inn_boss);
        TextView Gold = (TextView) findViewById(R.id.Gold) ;
        Gold.setText ("Gold : " + GoldAmount); ;


        inn_boss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().beginTransaction()
                        .replace(R.id.cross_road_inn_layout, new CrossRoadInnRoomsFragment()).commit();

            }
        });


        heroClasses.add(HeroClass.WARRIOR);
        heroClasses.add(HeroClass.MAGE);
        heroClasses.add(HeroClass.PRIEST);

        initOneHeroInLobby (layout,heroClasses);
        initOneHeroInLobby (layout,heroClasses);
        initOneHeroInLobby (layout,heroClasses);
        initOneHeroInLobby (layout,heroClasses);
        initOneHeroInLobby (layout,heroClasses);

    }



    private void initOneHeroInLobby (ConstraintLayout layout, ArrayList<HeroClass> heroClasses) {

        String heroName = generateHeroName((int) (Math.random() * 100) % 4 + 3);
        HeroClass heroClass = heroClasses.get ( (int) (Math.random() * 100) % 3 );
        HeroSkill heroSkill = null;
        int heroAttackPower = 0;
        int heroDefencePower = 0;
        int heroAttackPowerEnhanced = 0;
        int heroDefencePowerEnhanced = 0;
        int level = 0;

        final Hero hero = new Hero();

        hero.setHeroName(heroName);
        hero.setHeroClass(heroClass);
        hero.setHeroSkill(heroSkill);
        hero.setHeroAttackPower(heroAttackPower);
        hero.setHeroDefencePower(heroDefencePower);
        hero.setLevel(level);
        hero.setHeroRarity(HeroRarity.ELITE);
        hero.setHeroAttackPowerEnhancement(heroAttackPowerEnhanced);
        hero.setHeroDefencePowerEnhancement(heroDefencePowerEnhanced);

        GifImageView Gifview = new GifImageView(this);
        Gifview.setImageResource(R.drawable.warrior1);
        Gifview.setLayoutParams(new ViewGroup.LayoutParams(100, 100));

        hero.setHeroGif(Gifview);
        hero.setHeroGifID(R.drawable.warrior1);
        Button heroButton = new Button(this);
        heroButton.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        heroButton.setBackgroundColor(0);
        heroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            SingleHeroInLobbyFragment fragment = SingleHeroInLobbyFragment.newInstance(hero);

            getFragmentManager().beginTransaction()
                .replace(R.id.cross_road_inn_layout, fragment).commit();
            }
        });

        hero.setHeroButton(heroButton);

        herosAtLobby.add(hero);
        layout.addView(hero.getHeroButton());
        layout.addView(hero.getHeroGif());

        HeroEnterInnAnimation (layout,hero.getHeroGif(),hero.getHeroButton(),new Date() );



    }

    private void HeroEnterInnAnimation (  final ConstraintLayout layout,
                                 final GifImageView heroGifImageView, final Button heroButton, final Date now ){

        heroGifImageView.animate().translationX(650).translationY(300).withEndAction(
            new Runnable() {
                @Override
                public void run() {
                    Random rand = new Random();
                    int x = rand.nextInt((600 - 300) + 1) + 300;
                    int y = rand.nextInt((1000 - 300) + 1) + 650;
                    heroGifImageView.animate().translationX(x).translationY(y).setDuration(3000)
                            .withEndAction(heroRandomWalk(layout,heroGifImageView,heroButton,now)).start();
                }
            }

        );

    }


    private Runnable heroRandomWalk(final ConstraintLayout layout, final GifImageView heroGifImageView, final Button heroButton, final Date now) {


        final Runnable aRunnable = new Runnable() {
            @Override
            public void run() {
                int x = 0, y = 0, direction = (int) (Math.random() * 100) % 4;
                final Long timeElapsed = new Date().getTime() - now.getTime();


                switch (direction) {
                    case 0:
                        x = 20;
                        y = 0;
                        break;
                    case 1:
                        x = -20;
                        y = 0;
                        break;
                    case 2:
                        x = 0;
                        y = 20;
                        break;
                    case 3:
                        x = 0;
                        y = -20;
                        break;
                }
                //Log.i("",""+ heroGifImageView.getX() + heroGifImageView.getY());
                //Log.i("","now "+ now.getTime());
                //Log.i("","new "+ new Date().getTime());
                //Log.i("","timeElapsed "+ timeElapsed);
                final int finalX = x;
                final int finalY = y;
                if ( timeElapsed < 100000.0  ) {
                    heroButton.animate().translationX(heroGifImageView.getX()).translationY(heroGifImageView.getY()).withEndAction(
                        new Runnable() {
                            @Override
                            public void run() {
                            heroButton.animate().translationXBy(finalX).translationYBy(finalY);
                            }
                        }
                    );

                    heroGifImageView.animate().translationXBy(x).translationYBy(y).setDuration(1000).withEndAction(this).start();
                }

                else {
                    layout.removeView(heroButton);
                    heroGifImageView.animate().translationXBy(x).translationYBy(y).setDuration(1000).withEndAction(
                        new Runnable() {
                            @Override
                            public void run() {
                                heroGifImageView.animate().translationX(650).translationY(200).setDuration(5000).withEndAction(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                        layout.removeView(heroGifImageView);
                                        }
                                    }
                                );
                            }
                        }

                    );

                }
            }

        };


        return aRunnable;

    }

    String generateHeroName(int n)
    {

        // chose Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
