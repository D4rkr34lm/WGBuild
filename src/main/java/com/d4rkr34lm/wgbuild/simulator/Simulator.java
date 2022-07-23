package com.d4rkr34lm.wgbuild.simulator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Simulator {

    private final Material ENABLED_SIMULATOR_MATERIAL = Material.LIGHT_BLUE_SHULKER_BOX;
    private final Material DISABLE_SIMULATOR_MATERIAL = Material.PURPLE_SHULKER_BOX;
    private HashMap<Integer, Phase> phases = new HashMap<Integer, Phase>();
    private Block block;
    private boolean enabled;
    private int priority;
    private double offsetX;
    private double offsetY;
    private double offsetZ;

    public Simulator(Block block, boolean enabled){
        this.enabled = enabled;
        this.block = block;

        ShulkerBox simulator = (ShulkerBox) block.getState();
        ItemStack dataContainer = simulator.getInventory().getItem(0);
        String data = dataContainer.getItemMeta().getDisplayName();

        String[] parts = data.split(";");

        String[] simulatorDataParts = parts[0].split(",");

        priority = Integer.parseInt(simulatorDataParts[0]);
        offsetX = Double.parseDouble(simulatorDataParts[1]);
        offsetY = Double.parseDouble(simulatorDataParts[2]);
        offsetZ = Double.parseDouble(simulatorDataParts[3]);

        for(int n = 1; n < parts.length; n++){
            String[] phaseDataParts = parts[n].split(",");
            int tick = Integer.parseInt(phaseDataParts[0]);

            phases.put(tick, new Phase(parts[n]));
        }
    }

    public void simulatePhase(int tick){
        if(enabled){
            Location offsettedLocation = new Location(block.getLocation().getWorld(), block.getLocation().getX() + offsetX, block.getLocation().getY() + offsetY, block.getLocation().getZ() + offsetZ);
            phases.get(tick).simulate(offsettedLocation);
        }
    }

    public void startSimulation(){
        block.setType(Material.AIR);
    }

    public void stopSimulation(){
        if(enabled){
            block.setType(ENABLED_SIMULATOR_MATERIAL);
        }
        else {
            block.setType(DISABLE_SIMULATOR_MATERIAL);
        }

        ShulkerBox simulatorShulker = (ShulkerBox) block.getState();

        ItemStack dataContainer = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = dataContainer.getItemMeta();
        itemMeta.setDisplayName(toString());
        dataContainer.setItemMeta(itemMeta);

        simulatorShulker.getInventory().setItem(0, dataContainer);
    }

    public boolean hasPhase(int tick){
        return phases.containsKey(tick);
    }

    public int getPriority(){
        return  priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Block getBlock() {
        return block;
    }

    public double[] getOffsets(){
        double[] offsets = new double[3];
        offsets[0] = offsetX;
        offsets[1] = offsetY;
        offsets[2] = offsetZ;

        return offsets;
    }

    public double getOffset(int axis){
        return getOffsets()[axis];
    }

    public void setOffset(int axis, double offset){
        switch (axis){
            case 0:
                offsetX = offset;
                break;
            case 1:
                offsetY = offset;
                break;
            case 2:
                offsetZ = offset;
                break;
        }
    }

    public int getTotalTntCount(){
        int count = 0;

        for(Phase phase : phases.values()){
            count += phase.getTnt();
        }

        return  count;
    }

    public ArrayList<Phase> getPhases() {
        ArrayList<Phase> phasesList = new ArrayList<>();
        phasesList.addAll(phases.values());
        phasesList.sort(new Comparator<Phase>() {
            @Override
            public int compare(Phase o1, Phase o2) {
                if(o1.getTick() == o2.getTick()){
                    return 0;
                }
                if(o1.getTick() > o2.getTick()){
                    return 1;
                }
                else {
                    return -1;
                }
            }
        });
        return  phasesList;
    }

    public void addPhase(Phase phase){
        phases.put(phase.getTick(), phase);
    }

    public void removePhase(Phase phase){
        phases.remove(phase.getTick());
    }

    @Override
    public String toString() {
        String data = "";
        data += Integer.toString(priority);
        data += ",";
        data += Double.toString(offsetX);
        data += ",";
        data += Double.toString(offsetY);
        data += ",";
        data += Double.toString(offsetZ);
        data += ";";



        for(Phase phase : getPhases()){
            data += phase.toString();
        }

        return data;
    }
}
