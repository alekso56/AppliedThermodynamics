package com.tgame.apptherm.logic;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.ForgeSubscribe;
import appeng.api.DimentionalCoord;
import appeng.api.TileRef;
import appeng.api.exceptions.AppEngTileMissingException;
import appeng.api.me.tiles.IGridMachine;
import appeng.api.me.util.IGridCache;
import appeng.api.me.util.IGridInterface;

import com.tgame.apptherm.api.events.ATRemapEvent;
import com.tgame.apptherm.api.tiles.IATCoolantMachine;

/**
 * A IGridCache that maps out all machines that are coolants This allows for
 * better implementation not requiring Edits to IGridCaches
 * 
 * 
 * @author tgame14
 * 
 */
public class LogicMap implements IGridCache {

	protected Map<DimentionalCoord, IATCoolantMachine> coordMap;
	private boolean ticked;

	public LogicMap() {
		this.coordMap = new HashMap<DimentionalCoord, IATCoolantMachine>();
		this.ticked = false;
	}

	private Map<DimentionalCoord, IATCoolantMachine> mapGrid(IGridInterface grid) {
		Map<DimentionalCoord, IATCoolantMachine> map = new HashMap<DimentionalCoord, IATCoolantMachine>();
		
		for (TileRef<IGridMachine> mach : grid.getMachines()) {
			IATCoolantMachine candidate;
			try {
				IGridMachine tile = mach.getTile();

				if (tile instanceof IATCoolantMachine) {
					candidate = (IATCoolantMachine) tile;
					map.put(mach.getCoord(), candidate);

				}

			} catch (AppEngTileMissingException e) {

				e.printStackTrace();
			}
		}
		return map;

	}

	@Override
	public void reset(IGridInterface grid) {
		coordMap = mapGrid(grid);
		
	}

	@Override
	public void onUpdateTick(IGridInterface grid) {
		if (!ticked) {
			coordMap = mapGrid(grid);
			this.ticked = true;
		}

	}

	@Override
	public String getCacheName() {
		return "mapperForAT";
	}

	@Override
	public NBTTagCompound savetoNBTData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadfromNBTData(NBTTagCompound data) {
		// TODO Auto-generated method stub

	}
	
	public double sumCooling() {
		double sum = 0;
		
		for(IATCoolantMachine mach : coordMap.values()) {
			if(mach.isActive())
				sum += mach.coolPerTick();
		}
		
		return sum;
	}
	
	public Map<DimentionalCoord, IATCoolantMachine> getCoolersMap() {
		return this.coordMap;
	}
	
	@ForgeSubscribe
	public void remapTile(ATRemapEvent event) {
		coordMap.put(event.dim, (IATCoolantMachine) event.tile);
	}
	
	
}
