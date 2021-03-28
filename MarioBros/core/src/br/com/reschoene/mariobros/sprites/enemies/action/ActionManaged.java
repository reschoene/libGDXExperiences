package br.com.reschoene.mariobros.sprites.enemies.action;

public class ActionManaged {
    private final float timeToExecute;
    private float currentTime = 0;
    private final Executable action;

    public ActionManaged(float timeToExecute, Executable action){
        this.timeToExecute = timeToExecute;
        this.action = action;
    }

    public boolean process(float dt){
        boolean executed = false;
        currentTime += dt;

        if (currentTime >= timeToExecute){
            action.execute();
            currentTime = 0;
            executed = true;
        }

        return executed;
    }
}
