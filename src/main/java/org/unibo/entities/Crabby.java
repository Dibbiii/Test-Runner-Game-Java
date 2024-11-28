package org.unibo.entities;
import static org.unibo.utils.Constants.EnemyConstants.*;
import static org.unibo.Game.*;
import static org.unibo.utils.HelpMethods.*;
import static org.unibo.utils.Constants.Directions.*;

public class Crabby extends Enemy {

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitBox(x, y, 22 * (int) SCALE, 19 * (int) SCALE);
    }

    private void updateMove( int[][] levelData, Player player) {
        if(firstUpdate) {
            firstUpdateCheck(levelData); //function in the Enemy class
        }
        if(isInAir) {
            updateInAir(levelData);
        } else {
            switch (enemyState){
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if(canSeePlayer(levelData, player)) {
                        turnTowardsPlayer(player);
                    }
                    if(isPlayerCloseForAttack(player)) {
                        newState(ATTACKING);
                    }

                    move(levelData);
                    break;
                }
            }
        }
        public void update(int[][] levelData, Player player) {
            updateMove(levelData, player);
            updateAnimationTick();
        }
    }

