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

import com.hmdzl.spspd.change.Assets;
import com.hmdzl.spspd.change.actors.Char;
import com.hmdzl.spspd.change.actors.blobs.Blob;
import com.hmdzl.spspd.change.actors.blobs.ElectriShock;
import com.hmdzl.spspd.change.actors.blobs.Fire;
import com.hmdzl.spspd.change.effects.CellEmitter;
import com.hmdzl.spspd.change.effects.particles.EnergyParticle;
import com.hmdzl.spspd.change.effects.particles.FlameParticle;
import com.hmdzl.spspd.change.levels.Level;
import com.hmdzl.spspd.change.scenes.GameScene;
import com.hmdzl.spspd.change.sprites.TrapSprite;
import com.watabou.noosa.audio.Sample;

public class ShockTrap extends Trap {

	{
		color = TrapSprite.GREEN;
		shape = TrapSprite.DIAMOND;
	}

	@Override
	public void activate(Char ch) {
		super.activate(ch);
		for (int i : Level.NEIGHBOURS9){
			if (Level.insideMap(pos+i) && !Level.solid[pos+i]) {
				GameScene.add(Blob.seed(pos + i, 10, ElectriShock.class));
				CellEmitter.get(pos + i).burst(EnergyParticle.FACTORY, 5);
			}
		}
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}
}