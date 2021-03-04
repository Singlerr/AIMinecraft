package io.github.singlerr;

import io.github.singlerr.network.FakePlayer;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class GameHandler {

    private List<FakePlayer> population;
    private Location startLoc;
    private int num;

    public GameHandler(int pop) {
        population = new ArrayList<>();
        num = pop;
    }

    public void startAt(Location startLoc, int dist) {
        this.startLoc = startLoc.clone();
        for (int i = 0; i < num; i++) {
            FakePlayer fakePlayer = null;
            if((i+1) % 2 == 0)
                fakePlayer = new FakePlayer(-5);
            else
                fakePlayer = new FakePlayer(5);
            fakePlayer.initialize(startLoc.add(dist, 0, 0));
            population.add(fakePlayer);
            if ((i + 1) % 2 == 0) {
                fakePlayer.setTarget(population.get(i-1).getEntity());
                population.get(i-1).setTarget(fakePlayer.getEntity());
            }
        }
        GameTask task = new GameTask(Main.getGameHandler());
        task.runTaskTimer(Main.getInstance(),1L,1L);

        Main.setGameTask(task);
    }

    public List<FakePlayer> getPopulation() {
        return population;
    }

    public FakePlayer getFakePlayer(String name) {
        return population.stream().filter(p -> p.getName().equals(name)).findAny().get();
    }
}
