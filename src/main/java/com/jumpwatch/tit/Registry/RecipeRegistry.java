package com.jumpwatch.tit.Registry;

import com.jumpwatch.tit.crafting.recipe.MaceratorRecipe;
import com.jumpwatch.tit.theinventorstech;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RecipeRegistry {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, theinventorstech.MOD_ID);

public static final RegistryObject<MaceratorRecipe.Serializer> MACERATOR_SERIALIZER = RECIPE_SERIALIZER.register("crusher", MaceratorRecipe.Serializer::new);
public static IRecipeType<MaceratorRecipe> Macerator_recipe = new MaceratorRecipe.MaceratorRecipeType();


    private RecipeRegistry() {

    }

    static void register(IEventBus eventBus) {
        RECIPE_SERIALIZER.register(eventBus);
        LOGGER.info("Started registering recipe types!");
        Registry.register(Registry.RECIPE_TYPE, MaceratorRecipe.TYPE_ID, Macerator_recipe);
    }
}
