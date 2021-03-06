/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.hmdzl.spspd.change.items;

import java.util.ArrayList;

import com.hmdzl.spspd.change.Assets;
import com.hmdzl.spspd.change.Dungeon;
import com.hmdzl.spspd.change.actors.Actor;
import com.hmdzl.spspd.change.actors.buffs.Buff;
import com.hmdzl.spspd.change.actors.buffs.Haste;
import com.hmdzl.spspd.change.actors.hero.Hero;
import com.hmdzl.spspd.change.actors.mobs.Mob;
import com.hmdzl.spspd.change.actors.mobs.pets.PET;
import com.hmdzl.spspd.change.effects.particles.ElmoParticle;
import com.hmdzl.spspd.change.items.challengelists.ChallengeList;
import com.hmdzl.spspd.change.items.journalpages.JournalPage;
import com.hmdzl.spspd.change.items.keys.IronKey;
import com.hmdzl.spspd.change.items.misc.Spectacles.MagicSight;
import com.hmdzl.spspd.change.items.rings.RingOfForce;
import com.hmdzl.spspd.change.items.scrolls.Scroll;
import com.hmdzl.spspd.change.levels.Level;
import com.hmdzl.spspd.change.scenes.GameScene;
import com.hmdzl.spspd.change.scenes.InterlevelScene;
import com.hmdzl.spspd.change.sprites.ItemSpriteSheet;
import com.hmdzl.spspd.change.utils.GLog;
import com.hmdzl.spspd.change.windows.WndBag;
import com.hmdzl.spspd.change.windows.WndChallengeBook;
import com.hmdzl.spspd.change.windows.WndOtiluke;
import com.hmdzl.spspd.change.messages.Messages;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class ChallengeBook extends Item {
	
	public final float TIME_TO_USE = 1;
	
	public static final String AC_RETURN = "RETURN";
	public static final String AC_ADD = "ADD";
	public static final String AC_PORT = "READ";

	protected WndBag.Mode mode = WndBag.Mode.CHALLENGELIST;

	
	public int returnDepth = -1;
	public int returnPos;
	

	public boolean[] rooms = new boolean[10];	
	public boolean[] firsts = new boolean[10];	
		
	{
		//name = "Otiluke's journal";
		image = ItemSpriteSheet.CHALLENGE_BOOK;

		unique = true;
		
		//rooms[0] = true;
		//firsts[0] = true;
	}
		
	private static final String DEPTH = "depth";
	private static final String POS = "pos";
	private static final String ROOMS = "rooms";
	private static final String FIRSTS = "firsts";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DEPTH, returnDepth);
		bundle.put(ROOMS, rooms);
		bundle.put(FIRSTS, firsts);
		if (returnDepth != -1) {
			bundle.put(POS, returnPos);
		}
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		returnDepth = bundle.getInt(DEPTH);
		returnPos = bundle.getInt(POS);
		rooms = bundle.getBooleanArray(ROOMS);
		firsts = bundle.getBooleanArray(FIRSTS);
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		
		actions.add(AC_ADD);
		
		if (returnDepth > 0 && ( Dungeon.depth<35 && Dungeon.depth > 26) && !hero.petfollow){
		actions.add(AC_RETURN);
		}
		//charge >= reqCharges() &&		
		if (Dungeon.depth<26 && !hero.petfollow /*&& (rooms[0])*/){
		actions.add(AC_PORT);
		}
				
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		if (action == AC_PORT) {
			if (Dungeon.bossLevel()) {
				hero.spend(TIME_TO_USE);
				GLog.w(Messages.get(Item.class, "not_here"));
				return;
			}
		}

		if (action == AC_PORT) {
			GameScene.show(new WndChallengeBook(rooms, this));
		}
              
       if (action == AC_RETURN) {
    	   hero.spend(TIME_TO_USE);
			   updateQuickslot();		   
			   checkPetPort();		   
				InterlevelScene.mode = InterlevelScene.Mode.RETURN;	
				InterlevelScene.returnDepth = returnDepth;
				InterlevelScene.returnPos = returnPos;
				Game.switchScene(InterlevelScene.class);
				returnDepth=-1;
			}
               
       if (action == AC_ADD) {

    	   GameScene.selectItem(itemSelector, mode, Messages.get(this, "prompt"));
			
		}		
					
		 else {

			super.execute(hero, action);

		}
	}

	@Override
	public int price() {
		return 0;
	}
	
	public void reset() {
		returnDepth = -1;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}
	
	private PET checkpet(){
		for (Mob mob : Dungeon.level.mobs) {
			if(mob instanceof PET) {
				return (PET) mob;
			}
		}	
		return null;
	}
	
	private boolean checkpetNear(){
		for (int n : Level.NEIGHBOURS8) {
			int c =  Dungeon.hero.pos + n;
			if (Actor.findChar(c) instanceof PET) {
				return true;
			}
		}
		return false;
	}
	
	private void checkPetPort(){
		PET pet = checkpet();
		if(pet!=null && checkpetNear()){
		  //GLog.i("I see pet");
		  Dungeon.hero.petType=pet.type;
		  Dungeon.hero.petLevel=pet.level;
		  Dungeon.hero.petKills=pet.kills;	
		  Dungeon.hero.petHP=pet.HP;
		  Dungeon.hero.petExperience=pet.experience;
		  Dungeon.hero.petCooldown=pet.cooldown;
		  pet.destroy();
		  Dungeon.hero.petfollow=true;
		} else if (Dungeon.hero.haspet && Dungeon.hero.petfollow) {
			Dungeon.hero.petfollow=true;
		} else {
			Dungeon.hero.petfollow=false;
		}
		
	}

		
	@Override
	public String info() {
		String info = desc();
		return info;
	}
	
	protected WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(Item item) {
			if (item != null && item instanceof ChallengeList) {
				Hero hero = Dungeon.hero;
				int room = ((ChallengeList) item).room;
			
				hero.sprite.operate(hero.pos);
				hero.busy();
				hero.spend(2f);
				Sample.INSTANCE.play(Assets.SND_BURNING);
				hero.sprite.emitter().burst(ElmoParticle.FACTORY, 12);

				item.detach(hero.belongings.backpack);
				GLog.h(Messages.get(ChallengeBook.class,"add_page"));
				
				rooms[room] = true;
				firsts[room] = true;
				
		}
	 }
	};
}
