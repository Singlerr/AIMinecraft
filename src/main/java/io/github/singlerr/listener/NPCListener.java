package io.github.singlerr.listener;

import io.github.singlerr.Main;
import io.github.singlerr.network.Brain;
import io.github.singlerr.network.FakePlayer;
import io.github.singlerr.util.EntityUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.event.NavigationCompleteEvent;
import net.citizensnpcs.api.event.NPCDamageByEntityEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCListener implements Listener{

    @EventHandler
    public void onDamage(NPCDamageByEntityEvent e){
        if(CitizensAPI.getNPCRegistry().isNPC(e.getDamager())){
            FakePlayer attacker = Main.getGameHandler().getFakePlayer(CitizensAPI.getNPCRegistry().getNPC(e.getDamager()).getName());
            if(attacker.getBrain().isActing() && attacker.getCurrentAction() == Brain.Action.ATTACK){
                attacker.getBrain().stopActing();
            }
        }
    }
    @EventHandler
    public void onComplete(NavigationCompleteEvent e){
        FakePlayer player = Main.getGameHandler().getFakePlayer(e.getNPC().getName());
        if(player.getCurrentAction() == Brain.Action.FLEE && player.getBrain().isActing()){
            player.getBrain().stopActing();
        }
    }
}
