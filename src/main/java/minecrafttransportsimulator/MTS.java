package minecrafttransportsimulator;

import java.io.File;
import java.io.InputStreamReader;

import org.apache.logging.log4j.Logger;

import mcinterface.BuilderItem;
import mcinterface.InterfaceNetwork;
import minecrafttransportsimulator.dataclasses.MTSRegistry;
import minecrafttransportsimulator.items.components.AItemBase;
import minecrafttransportsimulator.items.instances.ItemJerrycan;
import minecrafttransportsimulator.items.instances.ItemJumperCable;
import minecrafttransportsimulator.items.instances.ItemKey;
import minecrafttransportsimulator.items.instances.ItemTicket;
import minecrafttransportsimulator.items.instances.ItemWrench;
import minecrafttransportsimulator.systems.ConfigSystem;
import minecrafttransportsimulator.systems.PackParserSystem;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MTS.MODID, name = MTS.MODNAME, version = MTS.MODVER)
public class MTS {
	public static final String MODID="mts";
	public static final String MODNAME="Minecraft Transport Simulator";
	public static final String MODVER="19.0.0-BETA-5";
	
	@Instance(value = MTS.MODID)
	public static MTS instance;
	public static Logger MTSLog;
	public static File minecraftDir;
	
	//TODO remove this when we complete item abstraction.
	public static AItemBase WRENCH;
	
	static{
		//Enable universal bucket so we can use buckets on fuel pumps.
		FluidRegistry.enableUniversalBucket();
		
		//Create main items.
		//TODO this should be not in the main MTS class when we abstract it.  Need our own loader.
		WRENCH = BuilderItem.createItem(new ItemWrench());
		BuilderItem.createItem(new ItemKey());
		BuilderItem.createItem(new ItemJumperCable());
		BuilderItem.createItem(new ItemJerrycan());
		BuilderItem.createItem(new ItemTicket());
		
		//Manually create the internal core mod pack items.
		//These need to be created before we do checks for block registration.
		//If we don't, then we risk not creating and registering the blocks.
		try{
			PackParserSystem.addBookletDefinition(new InputStreamReader(MTSRegistry.class.getResourceAsStream("/assets/" + MTS.MODID + "/jsondefs/booklets/handbook_en.json"), "UTF-8"), "handbook_en", MTS.MODID);
			PackParserSystem.addBookletDefinition(new InputStreamReader(MTSRegistry.class.getResourceAsStream("/assets/" + MTS.MODID + "/jsondefs/booklets/handbook_ru.json"), "UTF-8"), "handbook_ru", MTS.MODID);
			PackParserSystem.addDecorDefinition(new InputStreamReader(MTSRegistry.class.getResourceAsStream("/assets/" + MTS.MODID + "/jsondefs/decors/fuelpump.json"), "UTF-8"), "fuelpump", MTS.MODID);
			PackParserSystem.addDecorDefinition(new InputStreamReader(MTSRegistry.class.getResourceAsStream("/assets/" + MTS.MODID + "/jsondefs/decors/vehiclebench.json"), "UTF-8"), "vehiclebench", MTS.MODID);
			PackParserSystem.addDecorDefinition(new InputStreamReader(MTSRegistry.class.getResourceAsStream("/assets/" + MTS.MODID + "/jsondefs/decors/enginebench.json"), "UTF-8"), "enginebench", MTS.MODID);
			PackParserSystem.addDecorDefinition(new InputStreamReader(MTSRegistry.class.getResourceAsStream("/assets/" + MTS.MODID + "/jsondefs/decors/propellerbench.json"), "UTF-8"), "propellerbench", MTS.MODID);
			PackParserSystem.addDecorDefinition(new InputStreamReader(MTSRegistry.class.getResourceAsStream("/assets/" + MTS.MODID + "/jsondefs/decors/wheelbench.json"), "UTF-8"), "wheelbench", MTS.MODID);
			PackParserSystem.addDecorDefinition(new InputStreamReader(MTSRegistry.class.getResourceAsStream("/assets/" + MTS.MODID + "/jsondefs/decors/seatbench.json"), "UTF-8"), "seatbench", MTS.MODID);
			PackParserSystem.addDecorDefinition(new InputStreamReader(MTSRegistry.class.getResourceAsStream("/assets/" + MTS.MODID + "/jsondefs/decors/gunbench.json"), "UTF-8"), "gunbench", MTS.MODID);
			PackParserSystem.addDecorDefinition(new InputStreamReader(MTSRegistry.class.getResourceAsStream("/assets/" + MTS.MODID + "/jsondefs/decors/custombench.json"), "UTF-8"), "custombench", MTS.MODID);
			PackParserSystem.addDecorDefinition(new InputStreamReader(MTSRegistry.class.getResourceAsStream("/assets/" + MTS.MODID + "/jsondefs/decors/instrumentbench.json"), "UTF-8"), "instrumentbench", MTS.MODID);
			PackParserSystem.addDecorDefinition(new InputStreamReader(MTSRegistry.class.getResourceAsStream("/assets/" + MTS.MODID + "/jsondefs/decors/decorbench.json"), "UTF-8"), "decorbench", MTS.MODID);
			PackParserSystem.addDecorDefinition(new InputStreamReader(MTSRegistry.class.getResourceAsStream("/assets/" + MTS.MODID + "/jsondefs/decors/itembench.json"), "UTF-8"), "itembench", MTS.MODID);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		//Set logger and add log items from pre-boot operations.
		MTSLog = event.getModLog();
		for(String logEntry : PackParserSystem.logEntries){
			MTSLog.error(logEntry);
		}
		
		//Load config file an set minecraft directory.
		ConfigSystem.loadFromDisk(new File(event.getSuggestedConfigurationFile().getParent(), "mtsconfig.json"));
		minecraftDir = new File(event.getModConfigurationDirectory().getParent());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		InterfaceNetwork.init();
	}
}
