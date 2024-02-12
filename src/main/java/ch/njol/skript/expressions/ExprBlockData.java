/**
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
 * Copyright Peter Güttinger, SkriptLang team and contributors
 */
package ch.njol.skript.expressions;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;

@Name("Block Data")
@Description({
	"Get the <a href='classes.html#blockdata'>block data</a> associated with a block.",
	"This data can also be used to set blocks."
})
@Examples({
	"set {_data} to block data of target block",
	"set block at player to {_data}",
	"",
	"set block data of target block to oak_stairs[facing=south;waterlogged=true]"
})
@Since("2.5, 2.5.2 (set), INSERT VERSION (block displays)")
public class ExprBlockData extends SimplePropertyExpression<Object, BlockData> {

	static {
		if (Skript.classExists("org.bukkit.block.data.BlockData"))
			register(ExprBlockData.class, BlockData.class, "block[ ]data", Skript.isRunningMinecraft(1, 19, 4) ? "blocks/displays" : "blocks");
	}

	@Nullable
	@Override
	public BlockData convert(Object object) {
		if (object instanceof Block)
			return ((Block) object).getBlockData();
		if (object instanceof BlockDisplay)
		        return ((BlockDisplay) object).getBlock();
		return null;
		
	}

	@Nullable
	@Override
	public Class<?>[] acceptChange(ChangeMode mode) {
		if (mode == ChangeMode.SET)
			return CollectionUtils.array(BlockData.class);
		return null;
	}

	@Override
	public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
		if (delta == null)
			return;
		BlockData blockData = ((BlockData) delta[0]);
		for (Object object : getExpr().getArray(event)) {
			if (object instanceof Block) {
				((Block) object).setBlockData(blockData);
			} else if (object instanceof BlockDisplay) {
				((BlockDisplay) object).setBlock(blockData);
			}
		}
	}

	@Override
	public Class<? extends BlockData> getReturnType() {
		return BlockData.class;
	}

	@Override
	protected String getPropertyName() {
		return "block data";
	}

}
