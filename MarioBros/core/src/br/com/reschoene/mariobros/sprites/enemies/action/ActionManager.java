package br.com.reschoene.mariobros.sprites.enemies.action;

import java.util.ArrayList;
import java.util.List;

public class ActionManager {
    private final List<ActionManaged> actions;
    private int actionIdxToProcess = 0;

    public ActionManager(){
        this.actions = new ArrayList<>();
    }

    public void addAction(float timeToExecute, Executable action){
        actions.add(new ActionManaged(timeToExecute, action));
    }

    public void update(float dt){
        if (actions.get(actionIdxToProcess).process(dt)){
            actionIdxToProcess++;
            actionIdxToProcess = (actionIdxToProcess % actions.size());
        }
    }
}
