package com.example.assignment3.HeroObjects;

import android.graphics.drawable.Drawable;
import android.widget.Button;

import java.io.Serializable;

import pl.droidsonroids.gif.GifImageView;

public class Hero implements Serializable {

   private HeroRarity heroRarity;
   private String heroName;
   private HeroClass heroClass;
   private HeroSkill heroSkill;
   private int heroAttackPower;
   private int heroDefencePower;
   private int heroAttackPowerEnhancement;
   private int heroDefencePowerEnhancement;
   private int level;
   private int exp;
   private GifImageView heroGif;
   private int heroGifID;
   private Button heroButton;

   public Hero() {}

   public int getHeroAttackPowerEnhancement() {
      return heroAttackPowerEnhancement;
   }

   public void setHeroAttackPowerEnhancement(int heroAttackPowerEnhancement) {
      this.heroAttackPowerEnhancement = heroAttackPowerEnhancement;
   }

   public int getHeroDefencePowerEnhancement() {
      return heroDefencePowerEnhancement;
   }

   public void setHeroDefencePowerEnhancement(int heroDefencePowerEnhancement) {
      this.heroDefencePowerEnhancement = heroDefencePowerEnhancement;
   }

   public int getHeroGifID() {
      return heroGifID;
   }

   public void setHeroGifID(int heroGifID) {
      this.heroGifID = heroGifID;
   }

   public HeroRarity getHeroRarity() {
      return heroRarity;
   }

   public void setHeroRarity(HeroRarity heroRarity) {
      this.heroRarity = heroRarity;
   }

   public String getHeroName() {
      return heroName;
   }

   public void setHeroName(String heroName) {
      this.heroName = heroName;
   }

   public HeroClass getHeroClass() {
      return heroClass;
   }

   public void setHeroClass(HeroClass heroClass) {
      this.heroClass = heroClass;
   }

   public HeroSkill getHeroSkill() {
      return heroSkill;
   }

   public void setHeroSkill(HeroSkill heroSkill) {
      this.heroSkill = heroSkill;
   }

   public int getHeroAttackPower() {
      return heroAttackPower;
   }

   public void setHeroAttackPower(int heroAttackPower) {
      this.heroAttackPower = heroAttackPower;
   }

   public int getHeroDefencePower() {
      return heroDefencePower;
   }

   public void setHeroDefencePower(int heroDefencePower) {
      this.heroDefencePower = heroDefencePower;
   }

   public int getLevel() {
      return level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getExp() {
      return exp;
   }

   public void setExp(int exp) {
      this.exp = exp;
   }

   public GifImageView getHeroGif() {
      return heroGif;
   }

   public void setHeroGif(GifImageView heroGif) {
      this.heroGif = heroGif;
   }

   public Button getHeroButton() {
      return heroButton;
   }

   public void setHeroButton(Button heroButton) {
      this.heroButton = heroButton;
   }
}
