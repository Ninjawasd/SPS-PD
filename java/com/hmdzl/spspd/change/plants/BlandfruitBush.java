package com.hmdzl.spspd.change.plants;

import com.hmdzl.spspd.change.Dungeon;
import com.hmdzl.spspd.change.actors.Char;
import com.hmdzl.spspd.change.items.food.fruit.Blackberry;
import com.hmdzl.spspd.change.items.food.fruit.Blandfruit;
import com.hmdzl.spspd.change.items.food.fruit.Blueberry;
import com.hmdzl.spspd.change.items.food.fruit.Cloudberry;
import com.hmdzl.spspd.change.items.food.fruit.Moonberry;
import com.hmdzl.spspd.change.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

/**
 * Created by Evan on 13/08/2014.
 */
public class BlandfruitBush extends Plant {

	{
		image = 8;
	}

	@Override
	public void activate(Char ch) {
		super.activate(ch);
		Dungeon.level.drop(new Blandfruit(), pos).sprite.drop();
		switch (Random.Int(4)){
			case 0:
				Dungeon.level.drop(new Blackberry(), pos).sprite.drop();
				break;
			case 1:
				Dungeon.level.drop(new Blueberry(), pos).sprite.drop();
				break;
			case 2:
				Dungeon.level.drop(new Cloudberry(), pos).sprite.drop();
				break;
			case 3:
				Dungeon.level.drop(new Moonberry(), pos).sprite.drop();
				break;
		}
	}

	public static class Seed extends Plant.Seed {
		{
			image = ItemSpriteSheet.SEED_BLANDFRUIT;

			plantClass = BlandfruitBush.class;
			alchemyClass = null;
		}
	}
}
