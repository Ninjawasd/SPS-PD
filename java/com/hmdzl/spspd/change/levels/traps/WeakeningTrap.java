/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
package com.hmdzl.spspd.change.levels.traps;

import com.hmdzl.spspd.change.Dungeon;
import com.hmdzl.spspd.change.actors.Actor;
import com.hmdzl.spspd.change.actors.Char;
import com.hmdzl.spspd.change.actors.buffs.Buff;
import com.hmdzl.spspd.change.actors.buffs.Slow;
import com.hmdzl.spspd.change.actors.buffs.Weakness;
import com.hmdzl.spspd.change.effects.CellEmitter;
import com.hmdzl.spspd.change.effects.particles.ShadowParticle;
import com.hmdzl.spspd.change.items.Heap;
import com.hmdzl.spspd.change.sprites.TrapSprite;

public class WeakeningTrap extends Trap{

	{
		color = TrapSprite.GREEN;
		shape = TrapSprite.WAVES;
	}

	@Override
	public void activate(Char ch) {
		super.activate(ch);
		if (Dungeon.visible[ pos ]){
			CellEmitter.get(pos).burst(ShadowParticle.UP, 5);
		}

		//Char ch = Actor.findChar( pos );
		if (ch == Dungeon.hero){
			Buff.prolong( ch, Weakness.class, Weakness.duration(ch)*2f);
		} else if (ch != null) {
			Buff.prolong( ch, Slow.class, Slow.duration(ch));
		}
		Heap heap = Dungeon.level.heaps.get(pos);
		if (heap != null) {heap.poison();}
	}
}