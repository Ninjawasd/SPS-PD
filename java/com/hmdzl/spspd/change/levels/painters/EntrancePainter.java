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
package com.hmdzl.spspd.change.levels.painters;

import com.hmdzl.spspd.change.Dungeon;
import com.hmdzl.spspd.change.actors.Actor;
import com.hmdzl.spspd.change.actors.mobs.GoldCollector;
import com.hmdzl.spspd.change.levels.Level;
import com.hmdzl.spspd.change.levels.Room;
import com.hmdzl.spspd.change.levels.Terrain;

public class EntrancePainter extends Painter {

	public static void paint(Level level, Room room) {

		fill(level, room, Terrain.WALL);
		fill(level, room, 1, Terrain.EMPTY);

		for (Room.Door door : room.connected.values()) {
			door.set(Room.Door.Type.REGULAR);
		}

		level.entrance = room.random(1);
		set(level, level.entrance, Terrain.ENTRANCE);

		if (Dungeon.gold > (2000000/(Math.max(1,20-Dungeon.depth)))){
			GoldCollector gc = new GoldCollector();
			gc.pos = room.random();
			level.mobs.add(gc);
			Actor.occupyCell(gc);
		}

	}

}
