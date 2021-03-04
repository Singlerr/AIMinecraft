package io.github.singlerr.network;

import io.github.singlerr.Main;
import io.github.singlerr.util.EntityUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.*;
import java.util.UUID;

public class FakePlayer {



    private int dist;
    private int gen = 0;
    private Brain.Action currentAction;
    private NPC target;
    private NPC self;
    private Brain brain;
    private Location spawnLoc;
    private String name;


    public FakePlayer(int dist) {
        brain = new Brain();
        brain.initialize();
        name = UUID.randomUUID().toString().substring(0, 5);
        this.dist = dist;
    }

    public void initialize(Location loc) {
        spawnLoc = loc.clone();
        self = EntityUtil.spawnNPC(name, loc);
    }


    public void restart() {
        self.spawn(spawnLoc);
        self.setProtected(false);
        self.getEntity().setInvulnerable(false);
        ItemStack weapon = new ItemStack(Material.DIAMOND_SWORD);
        weapon.addEnchantment(Enchantment.DAMAGE_ALL, 5);

        ((LivingEntity) self.getEntity()).getEquipment().setItemInMainHand(weapon);
    }

    public NPC getEntity() {
        return self;
    }

    public void setTarget(NPC target) {
        this.target = target;
    }

    public void think() {
        if (brain.isActing())
            return;
        if (isDone()) {
            //RESTART
            gen++;
            Bukkit.broadcastMessage(ChatColor.GREEN + "WINNER: " + (self.isSpawned() ? self.getName() : target.getName()) + " , GEN: " + gen);
            Bukkit.broadcastMessage(ChatColor.GREEN + "Health: " + ChatColor.RED + (self.isSpawned() ? EntityUtil.getNPCHeath(self) : EntityUtil.getNPCHeath(target)));
            try {
                File f = new File(Main.getInstance().getDataFolder(), "gen-" + gen + ".gen");
                if (!f.exists())
                    f.createNewFile();

                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
                out.writeObject(brain);
                out.flush();
                out.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex2) {
                ex2.printStackTrace();
            }
            target.despawn();
            self.despawn();
            FakePlayer fakePlayer = Main.getGameHandler().getFakePlayer(target.getName());
            fakePlayer.getBrain().stopActing();
            getBrain().stopActing();
            fakePlayer.restart();
            restart();
            return;
        }

        brain.act();
        currentAction = brain.getNextAction(self, target);
        if (currentAction == Brain.Action.ATTACK) {

            self.getNavigator().setTarget(target.getEntity(), true);

        } else if (currentAction == Brain.Action.FLEE) {
           /* Location loc = new Location(self.getEntity().getWorld(),2*self.getEntity().getLocation().getX() - target.getEntity().getLocation().getX(),
                    2*self.getEntity().getLocation().getY() - target.getEntity().getLocation().getY(),
                    2*self.getEntity().getLocation().getZ() - target.getEntity().getLocation().getZ());*/
            self.getNavigator().setTarget(self.getEntity().getLocation().add(new Vector(dist,0,0)));
        }

    }

    public Location getSpawnLocation() {
        return spawnLoc;
    }

    public String getName() {
        return name;
    }

    public Brain getBrain() {
        return brain;
    }

    public boolean isDone() {
        return (!target.isSpawned()) || (!self.isSpawned());
    }

    public Brain.Action getCurrentAction() {
        return currentAction;
    }
}
