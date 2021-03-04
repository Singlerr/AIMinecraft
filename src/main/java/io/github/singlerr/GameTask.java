package io.github.singlerr;

import io.github.singlerr.network.FakePlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {

    private GameHandler gameHandler;

    public GameTask(GameHandler gameHandler){
        this.gameHandler = gameHandler;
    }

    @Override
    public void run() {
        for(FakePlayer fakePlayer : gameHandler.getPopulation()){
                fakePlayer.think();
        }
    }
}
