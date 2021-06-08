package com.jumpwatch.tit;

import com.jumpwatch.tit.Registry.theimmersiveregistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("theimmersivetech")
public class theimmersivetech
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "theimmersivetech";
    public theimmersivetech() {
        LOGGER.info("Starting Registry");
        theimmersiveregistry.register();
        MinecraftForge.EVENT_BUS.register(this);
    }


}
