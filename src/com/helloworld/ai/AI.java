
package com.helloworld.ai;

import com.helloworld.controls.Gameplay;
import com.helloworld.map.Coordinate;
import com.helloworld.utils.HexxagonConstant;

import java.util.ArrayList;
import java.util.Iterator;

public class AI {

    private ArrayList<Coordinate> units_a;

    private ArrayList<Coordinate> units_b;

    private static final int POINT_COPYABLE = 2;

    private static final int POINT_ENEMY = 1;

    public AI(ArrayList<Coordinate> units_a, ArrayList<Coordinate> units_b) {
        this.units_a = units_a;
        this.units_b = units_b;
    }

    public ArrayList<Coordinate> getBestMove() {
        return findBestMove();
    }

    @SuppressWarnings("null")
    private ArrayList<Coordinate> findBestMove() {
        int highestScore = 0;
        ArrayList<Coordinate> bestMove = null;

        Iterator<Coordinate> iA = units_a.iterator();
        Iterator<Coordinate> iB = units_b.iterator();

        while (iA.hasNext()) {
            Coordinate unit_a = iA.next();

            // lay toa do cac nuoc di
            ArrayList<Coordinate> moveArea = Gameplay.getAllCellMovable(unit_a);

            Iterator<Coordinate> iMoveArea = moveArea.iterator();

            while (iMoveArea.hasNext()) {
                Coordinate move_unit = iMoveArea.next();
                int score = 0;
                int x_move = move_unit.getX();
                int y_move = move_unit.getY();

                // cong diem uu tien cho nuoc di sao chep
                if (move_unit.getMoveType() == HexxagonConstant.COPYABLE) {
                    score += POINT_COPYABLE;
                }

                // kiem tra xem an duoc bao quan
                while (iB.hasNext()) {
                    Coordinate unit_b = iB.next();
                    int x_b = unit_b.getX();
                    int y_b = unit_b.getY();

                    if (Math.abs(x_move - x_b) == 1 || Math.abs(y_move - y_b) == 1) {
                        if (Math.abs((x_b + y_b) - (x_move + y_move)) < 2) {
                            score += POINT_ENEMY;
                        }
                    }
                }

                // update lai toa do neu score cao hon score cu
                if (highestScore < score) {
                    highestScore = score;
                    // from
                    bestMove.add(unit_a);
                    // to
                    bestMove.add(move_unit);
                } else if (highestScore == score) {
                    if (Math.random() > 0.5) {
                        // from
                        bestMove.add(unit_a);
                        // to
                        bestMove.add(move_unit);
                    }
                }
            }
        }

        return bestMove;

    }
}
