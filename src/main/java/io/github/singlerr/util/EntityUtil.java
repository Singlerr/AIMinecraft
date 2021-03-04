package io.github.singlerr.util;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class EntityUtil {

    public static int getNPCHeath(NPC npc){
        return (int) ((LivingEntity)npc.getEntity()).getHealth();
    }

    public static NPC spawnNPC(String name, Location loc){
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        NPC npc = registry.createNPC(EntityType.PLAYER,name);
        npc.spawn(loc);
        npc.setProtected(false);
        npc.getEntity().setInvulnerable(false);
        ItemStack weapon = new ItemStack(Material.DIAMOND_SWORD);
        weapon.addEnchantment(Enchantment.DAMAGE_ALL,5);

        ((LivingEntity)npc.getEntity()).getEquipment().setItemInMainHand(weapon);

        return npc;
    }


}
