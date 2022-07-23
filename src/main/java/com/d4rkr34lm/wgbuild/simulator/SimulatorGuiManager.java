package com.d4rkr34lm.wgbuild.simulator;

import com.d4rkr34lm.wgbuild.WGBuild;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
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

    /*
     * Sim gui Items
     */
    private ItemStack increaseTntItem;
    private ItemStack decreaseTntItem;
    private ItemStack increaseTickItem;
    private ItemStack decreaseTickItem;
    private ItemStack previousPageItem;
    private ItemStack nextPageItem;
    private ItemStack newPhaseItem;
    private ItemStack settingsItem;
    private ItemStack inventoryClosedByCodeMarker;
    private int taskID = 0;
    private HashMap<Player, Simulator> openedSimulators = new HashMap<>();

    public  SimulatorGuiManager(WGBuild plugin){
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;

        /*
         * Sim Gui Items
         */

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
            if(event.getAction().isRightClick() && event.getClickedBlock() != null){
                if(event.getClickedBlock().getType() == ENABLED_SIMULATOR_MATERIAL || event.getClickedBlock().getType() == DISABLE_SIMULATOR_MATERIAL){
                    openSimulator(event.getClickedBlock(), event.getPlayer());
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onSimGuiClick(InventoryClickEvent event){
        if(event.getView().getTitle().contains("Sim Page ")){
           if(event.getAction() == InventoryAction.PICKUP_ALL || event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY){
               Player player = (Player) event.getWhoClicked();
               Simulator simulator = openedSimulators.get(player);
               String[] splitTitle = event.getView().getTitle().split(" Tnt:");
               String pageString = splitTitle[0].replace("Sim Page ", "");
               int page = Integer.parseInt(pageString);
               page--;

               int column = event.getSlot() % 9;

               int changeStrenght = 1;
               if(event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY){
                   changeStrenght = 5;
               }

               String itemName = event.getInventory().getItem(event.getSlot()).getItemMeta().getDisplayName();

               boolean changedInventory = false;

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
                   Phase phase = simulator.getPhases().get(column + page * 8);
                   if(itemName.equals("+ 1 Tnt")){
                       phase.setTnt(phase.getTnt() + changeStrenght);
                   }
                   else {
                       if(phase.getTnt() - changeStrenght > 0){
                           phase.setTnt(phase.getTnt() - changeStrenght);
                       }
                   }
               }
               else if(itemName.equals("+ 1 Tick") || itemName.equals("- 1 Tick")){
                   Phase phase = simulator.getPhases().get(column + page * 8);
                   simulator.removePhase(phase);
                   if(itemName.equals("+ 1 Tick")){
                        phase.setTick(phase.getTick() + changeStrenght);
                        while (simulator.hasPhase(phase.getTick())){
                            phase.setTick(phase.getTick() + 1);
                        }
                   }
                   else {
                       int count = 1;
                       int lastViableTick = phase.getTick();
                       int currentTick = phase.getTick();
                       while (currentTick > 1){
                           currentTick--;
                           if(!simulator.hasPhase(currentTick)){
                               lastViableTick = currentTick;
                           }

                           if(count >= changeStrenght && lastViableTick != phase.getTick()){
                               break;
                           }
                           count++;
                       }
                       phase.setTick(lastViableTick);
                       simulator.addPhase(phase);
                   }
                   simulator.addPhase(phase);
               }
               else if(event.getCurrentItem().getType() == Material.PAPER){
                   Phase phase = simulator.getPhases().get(column + page * 8);
                   simulator.removePhase(phase);
               }
               else if(event.getCurrentItem().getType() == Material.ANVIL){
                   event.getInventory().addItem(inventoryClosedByCodeMarker);
                   showSettings(player, simulator);
                   changedInventory = true;
               }

                if(!changedInventory){
                    event.getInventory().addItem(inventoryClosedByCodeMarker);
                    showGui(player, simulator, page);
                    event.setCancelled(true);
                }
           }
           else {
               event.setCancelled(true);
           }
        }
    }

    @EventHandler
    public void onSettingsGuiClick(InventoryClickEvent event){
        if(event.getView().getTitle().equals("Sim settings")) {
            if (event.getAction() == InventoryAction.PICKUP_ALL || event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                Player player = (Player) event.getWhoClicked();
                Simulator simulator = openedSimulators.get(player);

                int column = event.getSlot() % 9;

                double changeStrenght = 1.0;
                if(event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY){
                    changeStrenght = 5.0;
                }

                String itemName = event.getInventory().getItem(event.getSlot()).getItemMeta().getDisplayName();
                boolean changedInventory = false;

                if(itemName.contains("-Offset Settings")){

                }
                else if(itemName.contains("+") && itemName.contains("Pixel")){
                    simulator.setOffset(column, simulator.getOffset(column) + changeStrenght * 0.0625);
                }
                else if(itemName.contains("-") && itemName.contains("Pixel")){
                    simulator.setOffset(column, simulator.getOffset(column) - changeStrenght * 0.0625);
                }
                else if(itemName.equals("+ 1 Priority")){
                    simulator.setPriority(simulator.getPriority() + 1);
                }
                else if (itemName.equals("- 1 Priority")) {
                    simulator.setPriority(simulator.getPriority() - 1);
                } else if(event.getCurrentItem().getType() == Material.REPEATER){

                } else if (event.getCurrentItem().getType() == Material.BARRIER) {
                    simulator.setOffset(0, 0.0);
                    simulator.setOffset(1, 0.0);
                    simulator.setOffset(2, 0.0);
                    simulator.setPriority(1);
                }

                if(!changedInventory){
                    event.getInventory().addItem(inventoryClosedByCodeMarker);
                    showSettings(player, simulator);
                    event.setCancelled(true);
                }
            }
            else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        if(event.getView().getTitle().equals("Sim settings")){
            if(!event.getInventory().contains(inventoryClosedByCodeMarker)){
                Simulator simulator = openedSimulators.get(event.getPlayer());
                Player player = (Player) event.getPlayer();
                 taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        showGui(player, simulator, 0);
                        Bukkit.getScheduler().cancelTask(taskID);
                    }
                }, 1, 1);
            }
        }
        else if(event.getView().getTitle().contains("Sim Page ")){
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
        Inventory inventory = Bukkit.createInventory(null, 6 * 9, "Sim Page " + (page + 1) + " Tnt: " + simulator.getTotalTntCount() + " Priority: " + simulator.getPriority());

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

    public void showSettings(Player player, Simulator simulator){
        String[] offsetScullValues = {
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzM4YWIxNDU3NDdiNGJkMDljZTAzNTQzNTQ5NDhjZTY5ZmY2ZjQxZDllMDk4YzY4NDhiODBlMTg3ZTkxOSJ9fX0=",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTcxMDcxYmVmNzMzZjQ3NzAyMWIzMjkxZGMzZDQ3ZjBiZGYwYmUyZGExYjE2NWExMTlhOGZmMTU5NDU2NyJ9fX0=",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzk5MmM3NTNiZjljNjI1ODUzY2UyYTBiN2IxNzRiODlhNmVjMjZiYjVjM2NjYjQ3M2I2YTIwMTI0OTYzMTIifX19"
        };

        String[] axisChar = {
                "x", "y", "z"
        };

        ItemStack[] increaseOffsetItems = new ItemStack[3];
        ItemStack[] decreaseOffsetItems = new ItemStack[3];
        ItemStack[] offsetItems = new ItemStack[3];

        for(int i = 0; i < 3; i++){
            double offset = simulator.getOffsets()[i];
            double offsetInPixels = Math.round(((offset / 0.0625) * 100.0 ) / 100.0);
            String offsetDisplay = "Current " + axisChar[i] + "-offset: " + offset + " " + ChatColor.ITALIC + offsetInPixels + " Pixel";

            increaseOffsetItems[i] = getScull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNjYmY5ODgzZGQzNTlmZGYyMzg1YzkwYTQ1OWQ3Mzc3NjUzODJlYzQxMTdiMDQ4OTVhYzRkYzRiNjBmYyJ9fX0=");
            ItemMeta increaseOffsetItemMeta = increaseOffsetItems[i].getItemMeta();
            increaseOffsetItemMeta.setDisplayName("+ 1 Pixel");
            List<String> increaseOffsetItemLore = new ArrayList<String>();
            increaseOffsetItemLore.add(offsetDisplay);
            increaseOffsetItemLore.add(" ");
            increaseOffsetItemLore.add("Shift Click for + 5 Pixel");
            increaseOffsetItemMeta.setLore(increaseOffsetItemLore);
            increaseOffsetItems[i].setItemMeta(increaseOffsetItemMeta);

            decreaseOffsetItems[i] = getScull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzI0MzE5MTFmNDE3OGI0ZDJiNDEzYWE3ZjVjNzhhZTQ0NDdmZTkyNDY5NDNjMzFkZjMxMTYzYzBlMDQzZTBkNiJ9fX0=");
            ItemMeta decreaseOffsetItemMeta = decreaseOffsetItems[i].getItemMeta();
            decreaseOffsetItemMeta.setDisplayName("- 1 Pixel");
            List<String> decreaseOffsetItemLore = new ArrayList<>();
            decreaseOffsetItemLore.add(offsetDisplay);
            decreaseOffsetItemLore.add(" ");
            decreaseOffsetItemLore.add("Shift Click for - 5 Pixel");
            decreaseOffsetItemMeta.setLore(decreaseOffsetItemLore);
            decreaseOffsetItems[i].setItemMeta(decreaseOffsetItemMeta);

            offsetItems[i] = getScull(offsetScullValues[i]);
            ItemMeta offsetItemMeta = offsetItems[i].getItemMeta();
            offsetItemMeta.setDisplayName(axisChar[i] + "-Offset Settings");
            List<String> offsetItemLore = new ArrayList<>();
            offsetItemLore.add(offsetDisplay);
            offsetItemMeta.setLore(offsetItemLore);
            offsetItems[i].setItemMeta(offsetItemMeta);
        }
        ItemStack increasePriorityItem = getScull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNjYmY5ODgzZGQzNTlmZGYyMzg1YzkwYTQ1OWQ3Mzc3NjUzODJlYzQxMTdiMDQ4OTVhYzRkYzRiNjBmYyJ9fX0=");
        ItemMeta increasePriorityItemMeta = increasePriorityItem.getItemMeta();
        increasePriorityItemMeta.setDisplayName("+ 1 Priority");
        increasePriorityItem.setItemMeta(increasePriorityItemMeta);

        ItemStack priorityItem = new ItemStack(Material.REPEATER, simulator.getPriority());
        ItemMeta priorityItemMeta = priorityItem.getItemMeta();
        priorityItemMeta.setDisplayName("Priority: " + simulator.getPriority());
        priorityItem.setItemMeta(priorityItemMeta);

        ItemStack decreasePriorityItem = getScull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzI0MzE5MTFmNDE3OGI0ZDJiNDEzYWE3ZjVjNzhhZTQ0NDdmZTkyNDY5NDNjMzFkZjMxMTYzYzBlMDQzZTBkNiJ9fX0=");
        ItemMeta decreasePriorityItemMeta = decreasePriorityItem.getItemMeta();
        decreasePriorityItemMeta.setDisplayName("- 1 Priority");
        decreasePriorityItem.setItemMeta(decreasePriorityItemMeta);


        ItemStack resetItem = new ItemStack(Material.BARRIER);
        ItemMeta resetItemMeta = resetItem.getItemMeta();
        resetItemMeta.setDisplayName("Reset settings");
        resetItem.setItemMeta(resetItemMeta);

        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Sim settings");

        for(int n = 0; n < 3; n++){
            inventory.setItem(n, increaseOffsetItems[n]);
            inventory.setItem(n + 9, offsetItems[n]);
            inventory.setItem(n + 18, decreaseOffsetItems[n]);
        }

        inventory.setItem(4, increasePriorityItem);
        inventory.setItem(13, priorityItem);
        inventory.setItem(22, decreasePriorityItem);

        inventory.setItem(16, resetItem);

        player.openInventory(inventory);
    }
}
