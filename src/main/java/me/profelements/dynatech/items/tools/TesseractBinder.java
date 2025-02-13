package me.profelements.dynatech.items.tools;

import com.xzavier0722.mc.plugin.slimefun4.storage.callback.IAsyncReadCallback;
import com.xzavier0722.mc.plugin.slimefun4.storage.controller.SlimefunBlockData;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.profelements.dynatech.DynaTechItems;
import me.profelements.dynatech.items.electric.transfer.Tesseract;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Optional;

public class TesseractBinder extends SlimefunItem {
    public TesseractBinder(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        addItemHandler(bindTesseract());
    }

    private ItemUseHandler bindTesseract() {
        return e -> {
            e.cancel();

            Optional<Block> block = e.getClickedBlock();
            Optional<SlimefunItem> sfBlock = e.getSlimefunBlock();
            if (block.isPresent() && sfBlock.isPresent()) {
                Location blockLocation = block.get().getLocation();
                SlimefunItem sfItem = sfBlock.get();
                ItemStack item = e.getItem();
                Boolean hasPermision = Slimefun.getProtectionManager().hasPermission(e.getPlayer(), blockLocation, Interaction.INTERACT_BLOCK);

                if (e.getPlayer().isSneaking()) {
                    String locString = PersistentDataAPI.getString(item.getItemMeta(), Tesseract.WIRELESS_LOCATION_KEY);
                    var blockData = StorageCacheUtils.getBlock(blockLocation);
                    if (hasPermision && blockData != null && blockData.getSfId().equals(DynaTechItems.TESSERACT.getItemId()) && item.hasItemMeta() && locString != null) {
                        if (blockData.isDataLoaded()) {
                            bind(blockLocation, locString, e.getPlayer());
                        } else {
                            Slimefun.getDatabaseManager().getBlockDataController().loadBlockDataAsync(
                                blockData,
                                new IAsyncReadCallback<>() {
                                    @Override
                                    public boolean runOnMainThread() {
                                        return true;
                                    }

                                    @Override
                                    public void onResult(SlimefunBlockData result) {
                                        bind(blockLocation, locString, e.getPlayer());
                                    }
                                }
                            );
                        }
                    }
                } else if (hasPermision && sfItem.getId().equals(DynaTechItems.TESSERACT.getItemId()) && blockLocation != null) {
                    ItemMeta im = item.getItemMeta();
                    String locString = Tesseract.LocationToString(blockLocation);

                    PersistentDataAPI.setString(im, Tesseract.WIRELESS_LOCATION_KEY, locString);
                    item.setItemMeta(im);
                    Tesseract.setItemLore(item, blockLocation);
                }

            }
        };
    }

    private void bind(Location loc, String locStr, Player p) {
        StorageCacheUtils.setData(loc, "tesseract-pair-location", locStr);
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "已绑定光学传输器!"));
    }
}
