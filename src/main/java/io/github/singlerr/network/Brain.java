package io.github.singlerr.network;


import io.github.singlerr.util.EntityUtil;
import net.citizensnpcs.api.npc.NPC;

import java.io.Serializable;
import java.util.Random;

public class Brain implements Serializable {

    private final int damage = 1;
    private boolean acting = false;
    public static final int STATE_MAX = 60;
    public static final double EPSILON = 0.3f;
    public static final double GAMMA = 0.9f;
    public static final double ALPHA = 0.1;

    private double[] qtable = new double[STATE_MAX + 1];

    public Brain() {

    }

    public void initialize() {
        for (int i = 0; i < STATE_MAX; i++)
            qtable[i] = random(100);
    }

    public Action getNextAction(NPC self, NPC target) {
        int state = EntityUtil.getNPCHeath(self) + 2*EntityUtil.getNPCHeath(target);
        Action action = null;
        if (random() < EPSILON) {
            if (random(1) == 0) {
                //ATTACK
                action = Action.ATTACK;
            } else {
                //FLEE
                action = Action.FLEE;
            }
        } else {
            int attackV = state - damage > 0 ? state - damage : 0;
            int fleeV = state;
            if (qtable[attackV] > qtable[fleeV]) {
                action = Action.ATTACK;
            } else {
                action = Action.FLEE;
            }
        }
        if (action.equals(Action.ATTACK))
            qtable[state - damage > 0 ? state - damage : 0] = getValue(self, target);
        else
            qtable[state] = getValue(self, target);
        return action;
    }


    public double getValue(NPC self, NPC target) {

        double qvalue = 0;
        double qmax;


        int state = EntityUtil.getNPCHeath(self) + 2*EntityUtil.getNPCHeath(target);
        if (state >= 30) {
            int attackV = state - damage > 0 ? state - damage : 0;
            int fleeV = state;
            if (qtable[attackV] > qtable[fleeV]) {
                qmax = qtable[attackV];
            } else {
                qmax = qtable[fleeV];
            }
            qvalue = qtable[state] + ALPHA * (GAMMA * qmax - qtable[state]);
        } else {
            if (state <= 20) {
                qvalue = qtable[state] + ALPHA * (1000 - qtable[state]);
            } else {
                qvalue = qtable[state];
            }

        }


        return qvalue;
    }

    public void stopActing(){
        acting = false;
    }
    public double random() {
        return new Random().nextDouble();
    }

    public double random(int max) {
        return new Random().nextInt(max + 1);
    }

    public boolean isActing() {
        return acting;
    }

    public void act() {
        acting = true;
    }

    public enum Action {
        ATTACK,
        FLEE
    }
}
