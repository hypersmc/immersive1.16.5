package com.jumpwatch.tit.crafting.recipe;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jumpwatch.tit.Registry.RecipeRegistry;
import com.jumpwatch.tit.crafting.recipe.IRecipe.IMaceratorRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class MaceratorRecipe implements IMaceratorRecipe {
    private static final Logger LOGGER = LogManager.getLogger();
    private final ResourceLocation id;
    private final ItemStack outputs;
    private final NonNullList<Ingredient> recipeItems;

    public MaceratorRecipe(ResourceLocation id, ItemStack output,
                           NonNullList<Ingredient> recipeItems) {
        this.id = id;
        this.outputs = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        if(recipeItems.get(0).test(inv.getItem(0))) {
            return this.recipeItems.get(0).test(inv.getItem(0));
        }

        return false;
    }



    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }


    @Override
    public ItemStack assemble(IInventory inv) {
        return getResultItem().copy();
    }

    public ItemStack getOutput() {
        return outputs.copy();
    }

    @Override
    public ItemStack getResultItem() {
        return outputs;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeRegistry.MACERATOR_SERIALIZER.get();
    }

    public static class MaceratorRecipeType implements IRecipeType<MaceratorRecipe> {
        @Override
        public String toString() {
            return MaceratorRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MaceratorRecipe>{



        @Override
        public MaceratorRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "output"));
            Integer count = JSONUtils.getAsInt(json, "count",1 );

            JsonArray ingredients = JSONUtils.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new MaceratorRecipe(recipeId, output, inputs);
        }

        @Nullable
        @Override
        public MaceratorRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buffer));
            }

            ItemStack output = buffer.readItem();
            return new MaceratorRecipe(recipeId, output, inputs);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, MaceratorRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buffer);
            }
            buffer.writeItemStack(recipe.getOutput(), false);
        }
    }
}