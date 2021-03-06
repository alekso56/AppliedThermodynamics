package com.tgame.apptherm.integration.computercraft;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.tileentity.TileEntity;
import openperipheral.api.IPeripheralAdapter;
import openperipheral.api.LuaMethod;
import openperipheral.api.LuaType;
import appeng.api.DimentionalCoord;
import appeng.api.me.util.IGridInterface;

import com.tgame.apptherm.integration.Mods;
import com.tgame.apptherm.logic.LogicBase;
import com.tgame.apptherm.logic.LogicInfo;
import com.tgame.apptherm.logic.LogicMap;
import com.tgame.apptherm.util.reflection.CallWrapper;
import com.tgame.apptherm.util.reflection.ReflectionHelper;

import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.Method;
import dan200.computer.api.IComputerAccess;

@Interface(iface = "openperipherals.api.IPeripheralAdapter", modid = Mods.OPENPERIPHERAL)
public class AdapterNetworked implements IPeripheralAdapter {

	private static Class<?> clazz = ReflectionHelper.getClass("appeng.api.me.tiles.IGridTileEntity");

	@Override
	public Class<?> getTargetClass() {
		return clazz;
	}

	private static IGridInterface getGrid(Object tile) {
		return new CallWrapper<IGridInterface>().call(tile, "getGrid");

	}

	@Method(modid = Mods.OPENPERIPHERAL)
	@LuaMethod(description = "get the current Heat levels in the network", returnType = LuaType.NUMBER)
	public double getHeatValue(IComputerAccess computer, Object te)
			throws Exception {

		TileEntity tile = (TileEntity) te;
		IGridInterface grid = getGrid(te);
		if (grid == null)
			throw new Exception("Not Connected to Grid");

		LogicBase logic = (LogicBase) grid.getCacheByID(LogicInfo.heatCacheID);
		return logic.getFinalHeat();

	}

	@Method(modid = Mods.OPENPERIPHERAL)
	@LuaMethod(description = "get the amount of cooling blocks in the network", returnType = LuaType.NUMBER)
	public int getCoolersAmount(IComputerAccess computer, Object te)
			throws Exception {
		TileEntity tile = (TileEntity) te;
		IGridInterface grid = getGrid(te);
		if (grid == null)
			throw new Exception("Not Connected to Grid");

		LogicMap logic = (LogicMap) grid.getCacheByID(LogicInfo.mapCacheID);
		return logic.getCoolersMap().size();

	}

	@Method(modid = Mods.OPENPERIPHERAL)
	@LuaMethod(description = "Returns a Table of coordinates and dimId's of all coolants", returnType = LuaType.TABLE)
	public Map<Integer, DimentionalCoord> getCoolersCoords(
			IComputerAccess computer, Object te) throws Exception {
		TileEntity tile = (TileEntity) te;
		IGridInterface grid = getGrid(te);
		if (grid == null)
			throw new Exception("Not Connected to Grid");

		LogicMap logic = (LogicMap) grid.getCacheByID(LogicInfo.mapCacheID);
		Map<Integer, DimentionalCoord> map = new HashMap<Integer, DimentionalCoord>();
		for (DimentionalCoord dim : logic.getCoolersMap().keySet())
			map.put(dim.hashCode(), dim);

		return map;

	}

	@Method(modid = Mods.OPENPERIPHERAL)
	@LuaMethod(description = "Returns the total cooling value forced on the network", returnType = LuaType.NUMBER)
	public double getCoolingValue(IComputerAccess computer, Object te)
			throws Exception {
		TileEntity tile = (TileEntity) te;
		IGridInterface grid = getGrid(te);
		if (grid == null)
			throw new Exception("Not connected to Grid");
		LogicBase logic = (LogicBase) grid.getCacheByID(LogicInfo.heatCacheID);

		return logic.getTotalCoolingValue();
	}
}
