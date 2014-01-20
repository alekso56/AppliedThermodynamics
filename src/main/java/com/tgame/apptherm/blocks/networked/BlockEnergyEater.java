package com.tgame.apptherm.blocks.networked;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.tgame.apptherm.AppTherm;
import com.tgame.apptherm.blocks.BlockInfo;
import com.tgame.apptherm.tileentities.TileEntityEater;

public class BlockEnergyEater extends BlockContainer {
	
	public BlockEnergyEater(int id) {
		super(id, Material.iron);
		
		this.setCreativeTab(AppTherm.AppThermTab);
		this.setHardness(2.0F);
		this.setUnlocalizedName("appliedthermodynamics." + BlockInfo.EATER_UNLOCALIZED_NAME);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityEater();
	}

}
