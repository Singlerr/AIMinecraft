package io.github.singlerr;

import io.github.singlerr.listener.NPCListener;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static GameHandler gameHandler;
    private static GameTask gameTask;
    private static Main instance;{
        instance = this;
    }

    public static GameTask getGameTask() {
        return gameTask;
    }

    public static Main getInstance() {
        return instance;
    }

    public static void setGameTask(GameTask gameTask) {
        Main.gameTask = gameTask;
    }

    public static GameHandler getGameHandler() {
        return gameHandler;
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new NPCListener(),this);
        if(! getDataFolder().exists())
            getDataFolder().mkdir();
    }

    @Override
    public void onDisable() {
        CitizensAPI.getNPCRegistry().deregisterAll();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(label.equalsIgnoreCase("ai")){
            if(! (sender instanceof Player))
                return false;

            Player player = (Player) sender;
            if(args.length > 0){
                if(args[0].equalsIgnoreCase("spawn")){
                    gameHandler = new GameHandler(2);
                    gameHandler.startAt(player.getLocation(),2);
                }
            }
        }

        return false;
    }
}
