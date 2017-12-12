/*
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 * Copyright 2011-2016 Peter Güttinger and contributors
 * 
 */

package ch.njol.skript.bukkitutil.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * Block compatibility implemented with magic numbers. No other choice until
 * Spigot 1.13.
 */
public class MagicBlockCompat implements BlockCompat {
	
	private class MagicBlockValues extends BlockValues {

		private Material id;
		private byte data;

		@SuppressWarnings("deprecation")
		public MagicBlockValues(Block block) {
			this.id = block.getType();
			this.data = block.getData(); // Some black magic here, please look away...
		}
		
		@SuppressWarnings("deprecation")
		public MagicBlockValues(ItemStack stack) {
			this.id = stack.getType();
			this.data = stack.getData().getData(); // And terrible hack again
		}

		@SuppressWarnings("deprecation")
		@Override
		public void setBlock(Block block, boolean applyPhysics) {
			block.setType(id);
			block.setData(data);
		}

		@Override
		public boolean equals(BlockValues other) {
			if (!(other instanceof MagicBlockValues))
				return false;
			MagicBlockValues magic = (MagicBlockValues) other;
			return id == magic.id && data == magic.data;
		}

		@Override
		public int hashCode() {
			// FindBugs reports "Scariest" bug when done with just ordinal << 8 | data
			// byte -> int widening seems to be a bit weird in Java
			return (id.ordinal() << 8) | (data & 0xff);
		}
		
	}
	
	@Override
	public BlockValues getBlockValues(Block block) {
		return new MagicBlockValues(block);
	}

	@Override
	public BlockValues getBlockValues(ItemStack stack) {
		return new MagicBlockValues(stack);
	}
	
}
