package com.d4rkr34lm.wgbuild.simulator;

import com.d4rkr34lm.wgbuild.WGBuild;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SimulatorGuiManager implements Listener {
    private WGBuild plugin;

    private final Material ENABLED_SIMULATOR_MATERIAL = Material.LIGHT_BLUE_SHULKER_BOX;
    private final Material DISABLE_SIMULATOR_MATERIAL = Material.PURPLE_SHULKER_BOX;
    private ItemStack increaseTntItem;
    private ItemStack decreaseTntItem;
    private ItemStack increaseTickItem;
    private ItemStack decreaseTickItem;
    private ItemStack previousPageItem;
    private ItemStack nextPageItem;
    private ItemStack newPhaseItem;
    private ItemStack settingsItem;
    private ItemStack inventoryClosedByCodeMarker;

    private HashMap<Player, Simulator> openedSimulators = new HashMap<>();

    public  SimulatorGuiManager(WGBuild plugin){
        Bukkit.getPluginManager().registerEvents(this, plugin);

        increaseTntItem = getScull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNjYmY5ODgzZGQzNTlmZGYyMzg1YzkwYTQ1OWQ3Mzc3NjUzODJlYzQxMTdiMDQ4OTVhYzRkYzRiNjBmYyJ9fX0=");
        ItemMeta increaseTntItemMeta = increaseTntItem.getItemMeta();
        increaseTntItemMeta.setDisplayName("+ 1 Tnt");
        List<String> increaseTntItemLore = new ArrayList<>();
        increaseTntItemLore.add("Shift Click for + 5 Tnt");
        increaseTntItemMeta.setLore(increaseTntItemLore);
        increaseTntItem.setItemMeta(increaseTntItemMeta);

        decreaseTntItem = getScull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzI0MzE5MTFmNDE3OGI0ZDJiNDEzYWE3ZjVjNzhhZTQ0NDdmZTkyNDY5NDNjMzFkZjMxMTYzYzBlMDQzZTBkNiJ9fX0=");
        ItemMeta decreaseTntItemMeta = decreaseTntItem.getItemMeta();
        decreaseTntItemMeta.setDisplayName("- 1 Tnt");
        List<String> decreaseTntItemLore = new ArrayList<>();
        decreaseTntItemLore.add("Shift Click for - 5 Tnt");
        decreaseTntItemMeta.setLore(decreaseTntItemLore);
        decreaseTntItem.setItemMeta(decreaseTntItemMeta);

        increaseTickItem = getScull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNjYmY5ODgzZGQzNTlmZGYyMzg1YzkwYTQ1OWQ3Mzc3NjUzODJlYzQxMTdiMDQ4OTVhYzRkYzRiNjBmYyJ9fX0=");
        ItemMeta increaseTickItemMeta = increaseTickItem.getItemMeta();
        increaseTickItemMeta.setDisplayName("+ 1 Tick");
        List<String> increaseTickItemLore = new ArrayList<>();
        increaseTickItemLore.add("Shift Click for + 5 Ticks");
        increaseTickItemMeta.setLore(increaseTickItemLore);
        increaseTickItem.setItemMeta(increaseTickItemMeta);

        decreaseTickItem = getScull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzI0MzE5MTFmNDE3OGI0ZDJiNDEzYWE3ZjVjNzhhZTQ0NDdmZTkyNDY5NDNjMzFkZjMxMTYzYzBlMDQzZTBkNiJ9fX0=");
        ItemMeta decreaseTickItemMeta = decreaseTickItem.getItemMeta();
        decreaseTickItemMeta.setDisplayName("- 1 Tick");
        List<String> decreaseTickItemLore = new ArrayList<>();
        decreaseTickItemLore.add("Shift Click for - 5 Ticks");
        decreaseTickItemMeta.setLore(decreaseTickItemLore);
        decreaseTickItem.setItemMeta(decreaseTickItemMeta);

        previousPageItem = getScull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzdhZWU5YTc1YmYwZGY3ODk3MTgzMDE1Y2NhMGIyYTdkNzU1YzYzMzg4ZmYwMTc1MmQ1ZjQ0MTlmYzY0NSJ9fX0=");
        ItemMeta previousPageItemMeta = previousPageItem.getItemMeta();
        previousPageItemMeta.setDisplayName("Previous page");
        previousPageItem.setItemMeta(previousPageItemMeta);

        nextPageItem = getScull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgyYWQxYjljYjRkZDIxMjU5YzBkNzVhYTMxNWZmMzg5YzNjZWY3NTJiZTM5NDkzMzgxNjRiYWM4NGE5NmUifX19");
        ItemMeta nextPageItemMeta = nextPageItem.getItemMeta();
        nextPageItemMeta.setDisplayName("Next page");
        nextPageItem.setItemMeta(nextPageItemMeta);

        newPhaseItem = getScull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWEyZDg5MWM2YWU5ZjZiYWEwNDBkNzM2YWI4NGQ0ODM0NGJiNmI3MGQ3ZjFhMjgwZGQxMmNiYWM0ZDc3NyJ9fX0=");
        ItemMeta newPhaseItemMeta = newPhaseItem.getItemMeta();
        newPhaseItemMeta.setDisplayName("New phase");
        newPhaseItem.setItemMeta(newPhaseItemMeta);

        settingsItem = new ItemStack(Material.ANVIL);
        ItemMeta settingsItemMeta = settingsItem.getItemMeta();
        settingsItemMeta.setDisplayName("Settings");
        settingsItem.setItemMeta(settingsItemMeta);

        inventoryClosedByCodeMarker = new ItemStack(Material.COMMAND_BLOCK);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.getItem() == null || event.getItem().getType() != Material.CLOCK){
            if(event.getAction().isRightClick()){
                if(event.getClickedBlock().getType() == ENABLED_SIMULATOR_MATERIAL || event.getClickedBlock().getType() == DISABLE_SIMULATOR_MATERIAL){
                    openSimulator(event.getClickedBlock(), event.getPlayer());
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event){
        if(event.getView().getTitle().contains("Simulator")){
           if(event.getAction() == InventoryAction.PICKUP_ALL || event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY){
               Player player = (Player) event.getWhoClicked();
               Simulator simulator = openedSimulators.get(player);
               String[] splitTitle = event.getView().getTitle().split(" Tnt:");
               String pageString = splitTitle[0].replace("Simulator Page ", "");
               int page = Integer.parseInt(pageString);
               page--;

               int row = event.getSlot() % 9;

               int changeStrenght = 1;
               if(event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY){
                   changeStrenght = 5;
               }

               String itemName = event.getInventory().getItem(event.getSlot()).getItemMeta().getDisplayName();

               if(itemName.equals("Next page")){
                   page++;
               }
               else if(itemName.equals("Previous page")){
                   page--;
               }
               else if (itemName.equals("New phase")){
                   int lastTick;
                   if(!simulator.getPhases().isEmpty()){
                       lastTick = simulator.getPhases().get(simulator.getPhases().size() - 1).getTick();
                   }
                   else {
                       lastTick = 0;
                   }

                   Phase newPhase = new Phase(lastTick + 1, 1, 0.0,0.0, 0.0);
                   simulator.addPhase(newPhase);
               }
               else if(itemName.equals("+ 1 Tnt") || itemName.equals("- 1 Tnt")){
                   Phase phase = simulator.getPhases().get(row + page * 8);
                   if(itemName.equals("+ 1 Tnt")){
                       phase.setTnt(phase.getTnt() + changeStrenght);
                   }
                   else {
                       phase.setTnt(phase.getTnt() - changeStrenght);
                   }
               }
               else if(itemName.equals("+ 1 Tick") || itemName.equals("- 1 Tick")){
                   Phase phase = simulator.getPhases().get(row + page * 8);
                   simulator.removePhase(phase);
                   if(itemName.equals("+ 1 Tick")){
                        phase.setTick(phase.getTick() + changeStrenght);
                   }
                   else {
                       phase.setTick(phase.getTick() - changeStrenght);
                   }
                   simulator.addPhase(phase);
               }



               event.getInventory().addItem(inventoryClosedByCodeMarker);
               showGui(player, simulator,  page);
               event.setCancelled(true);
           }
           else {
               event.setCancelled(true);
           }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        if(event.getView().getTitle().contains("Simulator")){
            if(!event.getInventory().contains(inventoryClosedByCodeMarker)){
                Simulator simulator = openedSimulators.get(event.getPlayer());

                Block block = simulator.getBlock();
                ShulkerBox simulatorShulkerBox = (ShulkerBox) block.getState();
                ItemStack dataContainer = simulatorShulkerBox.getInventory().getItem(0);
                ItemMeta meta = dataContainer.getItemMeta();
                meta.setDisplayName(simulator.toString());
                dataContainer.setItemMeta(meta);

                openedSimulators.remove(event.getPlayer());
            }
        }
    }

    public ItemStack getScull(String value){
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setPlayerProfile(Bukkit.createProfile(UUID.randomUUID(), null));
        PlayerProfile playerProfile = skullMeta.getPlayerProfile();

        playerProfile.getProperties().add(new ProfileProperty("textures", value));
        skullMeta.setPlayerProfile(playerProfile);
        skull.setItemMeta(skullMeta);
        return skull;
    }

    public void openSimulator(Block simulatorBlock, Player player){
        Simulator simulator;
        if(simulatorBlock.getType() == ENABLED_SIMULATOR_MATERIAL){
            simulator = new Simulator(simulatorBlock, true);
        }
        else {
            simulator = new Simulator(simulatorBlock, false);
        }

        openedSimulators.put(player, simulator);
        showGui(player ,simulator, 0);
    }

    public void showGui(Player player, Simulator simulator, int page){
        Inventory inventory = Bukkit.createInventory(null, 6 * 9, "Simulator Page " + (page + 1) + " Tnt: " + simulator.getTotalTntCount() + " Priority: " + simulator.getPriority());

        ArrayList<Phase> phases = simulator.getPhases();

        inventory.setItem(8 , settingsItem);
        for(int n = 0 ; n < 8; n++){

            if(n + 8 * page > phases.size() - 1){
                inventory.setItem(n + 18, newPhaseItem);
            }
            else {
                ItemStack tnt = new ItemStack(Material.TNT, phases.get(n + 8 * page).getTnt());
                ItemMeta tntItemMeta = tnt.getItemMeta();
                tntItemMeta.setDisplayName("Tnt: " + phases.get(n + 8 * page).getTnt());
                tnt.setItemMeta(tntItemMeta);
                ItemStack tick = new ItemStack(Material.PAPER, phases.get(n + 8 * page).getTick());
                ItemMeta tickItemMeta = tick.getItemMeta();
                tickItemMeta.setDisplayName("Tick: " + phases.get(n + 8 * page).getTick());
                tick.setItemMeta(tickItemMeta);

                inventory.setItem(n, increaseTntItem);
                inventory.setItem(n + 9, tnt);
                inventory.setItem(n + 18, decreaseTntItem);
                inventory.setItem(n + 27, increaseTickItem);
                inventory.setItem(n + 36, tick);
                inventory.setItem(n + 45, decreaseTickItem);
            }
        }

        if(page != 0){
            inventory.setItem(35, previousPageItem);
        }

        if(page == 0 && phases.size() >= 8){
            inventory.setItem(26, nextPageItem);
        }
        else if(phases.size() >= 8 + page * 7){
            inventory.setItem(26, nextPageItem);
        }

        player.openInventory(inventory);
    }
}
