package com.jumpwatch.tit.JEI;

import com.jumpwatch.tit.Registry.BlockRegistry;
import com.jumpwatch.tit.crafting.recipe.MaceratorRecipe;
import com.jumpwatch.tit.theinventorstech;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class CrusherRecipeCategory implements IRecipeCategory<MaceratorRecipe> {
    public static final ResourceLocation id = new ResourceLocation(theinventorstech.MOD_ID, ".crusher_recipe_category");
    private final IDrawable background;
    private final IDrawable icon;

    public CrusherRecipeCategory(IGuiHelper helper) {
        ResourceLocation location = new ResourceLocation(theinventorstech.MOD_ID, "textures/gui/maceratorguijei.png");
        this.background = helper.createDrawable(location, 0, 0, 180, 69);
        this.icon = helper.createDrawableIngredient(new ItemStack(BlockRegistry.Macerator.get()));
    }
    @Override
    public ResourceLocation getUid() {
        return id;
    }

    @Override
    public Class<? extends MaceratorRecipe> getRecipeClass() {
        return  MaceratorRecipe.class;
    }

    @Override
    public String getTitle() {
        return new TranslationTextComponent("category." + theinventorstech.MOD_ID + ".crusher_recipe").getString();
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(MaceratorRecipe recipeNew, IIngredients ingredients) {
        ingredients.setInputIngredients(recipeNew.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM ,recipeNew.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, MaceratorRecipe recipeNew, IIngredients iIngredients) {
        IGuiItemStackGroup itemStackGroup = iRecipeLayout.getItemStacks();
        itemStackGroup.init(0, true, 0, 0);
        itemStackGroup.init(1, false, 60, 18);
        itemStackGroup.set(iIngredients);
    }
}
