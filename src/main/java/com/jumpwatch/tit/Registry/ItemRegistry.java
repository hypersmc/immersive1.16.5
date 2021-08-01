package com.jumpwatch.tit.Registry;


import com.jumpwatch.tit.Items.ItemVoltmeter;
import com.jumpwatch.tit.Items.ItemWrench;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

public class ItemRegistry {

    static void register() {}


    //Item list
    /*
     * Example:
     *
     *   public static final RegistryObject<Item> testitem = globalreg.ITEMS.register("test_item", () -> new testItem(new Item.Properties().durability(300)));
     */

    public static final ItemWrench WRENCH = new ItemWrench();
    public static final ItemVoltmeter VOLTMETER = new ItemVoltmeter();
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                WRENCH,
                VOLTMETER
        );
    }


}
