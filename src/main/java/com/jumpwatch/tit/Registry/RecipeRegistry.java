package com.jumpwatch.tit.Registry;

import com.jumpwatch.tit.crafting.recipe.CrusherRecipeNew;
import com.jumpwatch.tit.theinventorstech;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;

public class RecipeRegistry {
//    public static final class Types {
//        public static final IRecipeType<CrusherRecipeNew> Crusher = IRecipeType.register(theinventorstech.MOD_ID + ":crusher");
//
//        private Types() {}
//    }
public static final IRecipeType<CrusherRecipeNew> CRUSHER_RECIPE_NEW_I_RECIPE_TYPE = IRecipeType.register(theinventorstech.MOD_ID + ":crusher");


//    public static final class Serializers {
//        public static final RegistryObject<IRecipeSerializer<?>> Crusher = theinventorsregistry.RECIPE_SERIALIZERS.register("crusher", CrusherRecipeNew.Serializer::new);
//
//        private Serializers() {}
//    }

public static final RegistryObject<IRecipeSerializer<?>> Crusher = theinventorsregistry.RECIPE_SERIALIZERS.register("crusher", CrusherRecipeNew.Serializer::new);
    private RecipeRegistry() {

    }

    static void register() {

    }
}
