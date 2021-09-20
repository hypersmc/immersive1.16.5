package com.jumpwatch.tit.crafting.recipe;

import com.google.gson.JsonObject;
import com.jumpwatch.tit.Registry.RecipeRegistry;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class CrusherRecipeNew extends SingleItemRecipe {
    private final Ingredient input;
    private final ItemStack output;
    public static final IRecipeType<CrusherRecipeNew> RECIPE_TYPE = new IRecipeType<CrusherRecipeNew>() {
        @Override
        public String toString() {
            return "theinventorstech:crusher";
        }
    };
    private static final Logger LOGGER = LogManager.getLogger();
    public CrusherRecipeNew(ResourceLocation resourceLocation, Ingredient input, ItemStack output) {
        super(RecipeRegistry.CRUSHER_RECIPE_NEW_I_RECIPE_TYPE, RecipeRegistry.Crusher.get(), resourceLocation, "", input, output);
        this.input = input;
        this.output = output;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> i = NonNullList.create();
        i.add(input);
        i.add(ingredient);
        return i;
    }

    @Override
    public boolean matches(IInventory inv, World world) {
        return this.ingredient.test(inv.getItem(0));
    }



    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CrusherRecipeNew> {

        @Override
        public CrusherRecipeNew fromJson(ResourceLocation recipeid, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            ResourceLocation itemid = new ResourceLocation(JSONUtils.getAsString(json, "result"));
            int count = JSONUtils.getAsInt(json, "count", 1);
            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(itemid), count);
            LOGGER.info("Registered: '" + json + "' as crusher recipe!");

            return new CrusherRecipeNew(recipeid, ingredient, result);
        }

        @Nullable
        @Override
        public CrusherRecipeNew fromNetwork(ResourceLocation recipeid, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            return new CrusherRecipeNew(recipeid, ingredient, result);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, CrusherRecipeNew recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}
