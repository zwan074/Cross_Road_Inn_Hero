package com.example.assignment3;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.assignment3.HeroObjects.Hero;
import com.example.assignment3.HeroObjects.HeroClass;
import com.example.assignment3.HeroObjects.HeroRarity;
import com.example.assignment3.HeroObjects.HeroSkill;
import com.example.assignment3.QuestObjects.Quest;

import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {
    AppCompatActivity MainActivity = this;

    LinkedList<Hero> herosAtLobby = new LinkedList<>() ;
    LinkedList<Hero> herosHired = new LinkedList<>();
    LinkedList<Quest> quests = new LinkedList<>();
    int difficultyFactor = 1;
    int GoldAmount = 10000;

    ConstraintLayout cross_road_inn_lobby_layout;
    Button inn_boss ;
    ImageButton quest_book;
    TextView Gold ;
    TextView totalHeroHired;

    Button doorPos;
    Button pos1;
    Button pos2;
    Button pos3;
    Button pos4;

    Thread heroEnterInn;

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
        Log.i("","OnCreate");
        if (savedInstanceState != null) {

            herosHired = (LinkedList<Hero>) savedInstanceState.getSerializable("Hero hired");
            GoldAmount = (int) savedInstanceState.getSerializable("Gold Amount");
            quests = (LinkedList<Quest>) savedInstanceState.getSerializable("Quest Info");
            difficultyFactor = savedInstanceState.getInt("Quests Difficulty factor");
        }

        cross_road_inn_lobby_layout = findViewById(R.id.cross_road_inn_layout);

        inn_boss = findViewById(R.id.inn_boss);
        quest_book = findViewById(R.id.questBook);
        Gold = findViewById(R.id.Gold);
        totalHeroHired = findViewById(R.id.totalHeroHired);

        doorPos = findViewById(R.id.Door);
        pos1 = findViewById(R.id.pos1);
        pos2 = findViewById(R.id.pos2);
        pos3 = findViewById(R.id.pos3);
        pos4 = findViewById(R.id.pos4);

        Gold.setText ("Gold : " + GoldAmount); ;

        totalHeroHired.setText("Total Heroes : " +  herosHired.size());

        inn_boss.setOnClickListener(v -> MainActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.cross_road_inn_layout, new CrossRoadInnRoomsFragment()).commit());

        quest_book.setOnClickListener(v -> MainActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.cross_road_inn_layout, new QuestMenuFragment()).commit());




    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("", "onResume");
        heroEnterInn = new Thread(new Runnable() {
            @Override
            synchronized public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                    //Log.i("", "Thread name " + Thread.currentThread().getName());
                    if (herosAtLobby.size () < 10 && Math.random() > 0.6) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initOneHeroInLobby();
                            }
                        });

                    }

                }


            }
        });

        heroEnterInn.start();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("","OnsavedInstanceState" );
        outState.putSerializable("Hero hired", herosHired);
        outState.putInt("Gold Amount",GoldAmount);
        outState.putInt("Quests Difficulty factor",difficultyFactor);
        outState.putSerializable("Quest Info",quests);
        heroEnterInn.interrupt();
    }

    private void initOneHeroInLobby () {

        int heroRarity = (int) (Math.random() * 100) % 3;
        switch (heroRarity) {
            case 0 : generateNormalHero (); break;
            case 1 : generateEliteHero (); break;
            case 2 : generateLegendaryHero (); break;
        }


    }

    private void generateNormalHero () {

        Random rand = new Random();
        final Hero hero = new Hero();
        int heroClass = (int) (Math.random() * 100) % 3;
        int heroGIF = (int) (Math.random() * 100) % 2;
        GifImageView GifImageView = new GifImageView(this);
        hero.setHeroName(generateHeroName((int) (Math.random() * 100) % 4 + 3));

        hero.setHeroRarity(HeroRarity.NORMAL);
        hero.setHeroSkill(HeroSkill.EMPTY);
        hero.setHeroAttackPower(rand.nextInt((10 - 1) + 1) + 1);
        hero.setHeroDefencePower(rand.nextInt((10 - 1) + 1) + 1);
        hero.setHeroAttackPowerEnhancement(0);
        hero.setHeroDefencePowerEnhancement(0);
        hero.setLevel(rand.nextInt(10) );

        setHeroGIFImageViewAndButton ( heroClass, heroGIF,  GifImageView ,  hero);

        herosAtLobby.add(hero);
        cross_road_inn_lobby_layout.addView(hero.getHeroButton());
        cross_road_inn_lobby_layout.addView(hero.getHeroGif());

        HeroEnterInnAnimation (hero.getHeroGif(),hero.getHeroButton(),new Date() , hero);


    }


    private void generateEliteHero () {

        Random rand = new Random();
        final Hero hero = new Hero();
        int heroClass = (int) (Math.random() * 100) % 3;
        int heroGIF = (int) (Math.random() * 100) % 2;
        GifImageView GifImageView = new GifImageView(this);
        hero.setHeroName(generateHeroName((int) (Math.random() * 100) % 4 + 3));

        hero.setHeroRarity(HeroRarity.ELITE);
        if (Math.random() > 0.8) {
            switch(heroClass) {
                case 0: hero.setHeroSkill(HeroSkill.CLEAVE)  ;break;
                case 1: hero.setHeroSkill(HeroSkill.FIREBALL)  ;break;
                case 2: hero.setHeroSkill(HeroSkill.HEAL)  ;break;
            }
        }else { hero.setHeroSkill(HeroSkill.EMPTY); }

        hero.setHeroAttackPower(rand.nextInt((15 - 5) + 1) + 5);
        hero.setHeroDefencePower(rand.nextInt((15 - 5) + 1) + 5);
        hero.setHeroAttackPowerEnhancement(0);
        hero.setHeroDefencePowerEnhancement(0);
        hero.setLevel(rand.nextInt(10) );

        setHeroGIFImageViewAndButton ( heroClass, heroGIF,  GifImageView ,  hero);

        herosAtLobby.add(hero);
        cross_road_inn_lobby_layout.addView(hero.getHeroButton());
        cross_road_inn_lobby_layout.addView(hero.getHeroGif());
        HeroEnterInnAnimation (hero.getHeroGif(),hero.getHeroButton(),new Date(), hero );

    }

    private void generateLegendaryHero () {

        Random rand = new Random();
        final Hero hero = new Hero();
        int heroClass = (int) (Math.random() * 100) % 3;
        int heroGIF = (int) (Math.random() * 100) % 2;
        GifImageView GifImageView = new GifImageView(this);
        hero.setHeroName(generateHeroName((int) (Math.random() * 100) % 4 + 3));

        hero.setHeroRarity(HeroRarity.LEGENDARY);
        switch(heroClass) {
            case 0: hero.setHeroSkill(HeroSkill.CLEAVE)  ;break;
            case 1: hero.setHeroSkill(HeroSkill.FIREBALL)  ;break;
            case 2: hero.setHeroSkill(HeroSkill.HEAL)  ;break;
        }
        hero.setHeroAttackPower(rand.nextInt((20 - 10) + 1) + 10);
        hero.setHeroDefencePower(rand.nextInt((20 - 10) + 1) + 10);
        hero.setHeroAttackPowerEnhancement(0);
        hero.setHeroDefencePowerEnhancement(0);
        hero.setLevel(rand.nextInt(10) );

        setHeroGIFImageViewAndButton ( heroClass, heroGIF,  GifImageView ,  hero);

        herosAtLobby.add(hero);
        cross_road_inn_lobby_layout.addView(hero.getHeroButton());
        cross_road_inn_lobby_layout.addView(hero.getHeroGif());

        HeroEnterInnAnimation (hero.getHeroGif(),hero.getHeroButton(),new Date(), hero );

    }


    private void setHeroGIFImageViewAndButton (int heroClass, int heroGIF, GifImageView GifImageView , final Hero hero) {

        Button heroButton = new Button(this);
        heroButton.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        heroButton.setBackgroundColor(0);
        heroButton.setOnClickListener(view -> {

            SingleHeroInLobbyFragment fragment = SingleHeroInLobbyFragment.newInstance(hero);
            MainActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.cross_road_inn_layout, fragment).commit();
        });
        hero.setHeroButton(heroButton);

        GifImageView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));

        if (heroClass == 0) {
            hero.setHeroClass(HeroClass.WARRIOR);

            if(heroGIF == 0) {
                GifImageView.setImageResource(R.drawable.warrior1);
                hero.setHeroGif(GifImageView);
                hero.setHeroGifID(R.drawable.warrior1);
            }
            else {
                GifImageView.setImageResource(R.drawable.warrior2);
                hero.setHeroGif(GifImageView);
                hero.setHeroGifID(R.drawable.warrior2);
            }
        }

        else if (heroClass == 1) {
            hero.setHeroClass(HeroClass.MAGE);

            if(heroGIF == 0) {
                GifImageView.setImageResource(R.drawable.mage1);
                hero.setHeroGif(GifImageView);
                hero.setHeroGifID(R.drawable.mage1);
            }
            else {
                GifImageView.setImageResource(R.drawable.mage2);
                hero.setHeroGif(GifImageView);
                hero.setHeroGifID(R.drawable.mage2);
            }
        }

        else if (heroClass == 2) {
            hero.setHeroClass(HeroClass.PRIEST);

            if(heroGIF == 0) {
                GifImageView.setImageResource(R.drawable.priest1);
                hero.setHeroGif(GifImageView);
                hero.setHeroGifID(R.drawable.priest1);
            }
            else {
                GifImageView.setImageResource(R.drawable.priest2);
                hero.setHeroGif(GifImageView);
                hero.setHeroGifID(R.drawable.priest2);
            }
        }

    }


    private void HeroEnterInnAnimation (final GifImageView heroGifImageView, final Button heroButton,
                                          final Date now, final Hero hero ){

        heroGifImageView.animate().translationX(doorPos.getX()).translationY(doorPos.getY()).withEndAction(
                () -> {
                    Random rand = new Random();
                    float x , y;
                    if (Math.random() > 0.5) {

                        x = rand.nextInt((int) ((pos2.getX() - pos1.getX()) + 1)) + pos1.getX();
                        y = rand.nextInt((int) ((pos2.getY() - pos1.getY()) + 1)) + pos1.getY();
                    }

                    else {
                        x = rand.nextInt((int) ((pos4.getX() - pos3.getX()) + 1)) + pos3.getX();
                        y = rand.nextInt((int) ((pos4.getY() - pos3.getY()) + 1)) + pos3.getY();
                    }

                    heroGifImageView.animate().translationX(x).translationY(y).setDuration(3000)
                            .withEndAction(heroRandomWalk(heroGifImageView,heroButton,now,hero)).start();
                }

        );

    }


    private Runnable heroRandomWalk(final GifImageView heroGifImageView,
                                    final Button heroButton, final Date now,final Hero hero) {


        return new Runnable() {
            @Override
            public void run() {

                int x = 0, y = 0, direction = (int) (Math.random() * 100) % 4;
                final long timeElapsed = new Date().getTime() - now.getTime();
                Random rand = new Random();

                switch (direction) {
                    case 0:
                        x = 20;
                        break;
                    case 1:
                        x = -20;
                        break;
                    case 2:
                        y = 20;
                        break;
                    case 3:
                        y = -20;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + direction);
                }
                //Log.i("",""+ heroGifImageView.getX() + heroGifImageView.getY());
                //Log.i("","now "+ now.getTime());
                //Log.i("","new "+ new Date().getTime());
                //Log.i("","timeElapsed "+ timeElapsed);
                if ( timeElapsed < 10000.0 * (rand.nextInt((15 - 3) + 1) + 3) ) {

                    float nextX =  heroGifImageView.getX() + x;
                    float nextY = heroGifImageView.getY() + y;

                    if ( (nextX > pos1.getX() && nextX < pos2.getX()
                            && nextY > pos1.getY() && nextY < pos2.getY())
                        || (nextX > pos3.getX() && nextX < pos4.getX()
                            && nextY > pos3.getY() && nextY < pos4.getY())
                        ) {

                        heroButton.animate().translationX(nextX).translationY(nextY);
                        heroGifImageView.animate().translationXBy(x).translationYBy(y).setDuration(1000).withEndAction(this).start();

                    }
                    else {
                        heroButton.animate().translationX(heroGifImageView.getX()).translationY(heroGifImageView.getY());
                        heroGifImageView.animate().withEndAction(this).start();

                    }


                }

                else {

                    heroGifImageView.animate().translationXBy(x).translationYBy(y).setDuration(1000).withEndAction(
                        new Runnable() {
                            @Override
                            public void run() {
                                heroGifImageView.animate().translationX(doorPos.getX()).translationY(doorPos.getY()).setDuration(5000).withEndAction(
                                        () -> {
                                            cross_road_inn_lobby_layout.removeView(heroButton);
                                            cross_road_inn_lobby_layout.removeView(heroGifImageView);
                                            herosAtLobby.remove(hero);
                                        }
                                );
                            }
                        }

                    );


                }
            }

        };

    }

    private String generateHeroName(int n)
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
