package com.jumpwatch.tit.JEI;



import com.jumpwatch.tit.theinventorstech;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;

import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.stream.Collectors;

@JeiPlugin
public class JEIimplementation implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID = new ResourceLocation(theinventorstech.MOD_ID, "jei_plugin");
    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IModPlugin.super.registerCategories(registration);
    }

    private static Collection<?> getRecipes(RecipeManager manager, IRecipeType<?> type) {
        return manager.getRecipes().parallelStream().filter(recipes -> recipes.getType() == type).collect(Collectors.toList());
    }
}