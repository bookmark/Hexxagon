
package com.helloworld.controls;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.helloworld.map.Cell;
import com.helloworld.map.Coordinate;
import com.helloworld.map.Hexagon;
import com.helloworld.map.Map;
import com.helloworld.utils.HexxagonConstant;

public class Gameplay {
    // private static int type;

    public static int currentPlayer;

    public static int scorePlayer1 = 0;

    public static int scorePlayer2 = 0;

    private static ArrayList<Coordinate> moveable = new ArrayList<Coordinate>();

    /**
     * @param cell :Cell can kiem tra
     * @return 0: Không làm gì .</br> 1: Di chuyển của Player 1</br> 2: Di
     *         chuyển của Player 2</br> 3: Cell được selected cần tính phạm vi
     *         di chuyển.</br> 4: Cell được un-selected cần hủy các cell
     *         moveable.
     */
    public static int doAction(Cell cell) {
        int type = cell.getType();
        Hexagon hexagon = (Hexagon)cell.getSprite();
        Coordinate coordinate = new Coordinate(hexagon.xDim, hexagon.yDim);
        ArrayList<Coordinate> zone = new ArrayList<Coordinate>();
        int score = 0;
        if (type == HexxagonConstant.CELL_DEFAULT) {
            // Do nothing
        } else if (type == HexxagonConstant.CELL_PLAYER1 || type == HexxagonConstant.CELL_PLAYER2) {
            if (cell.isSelected()) {
                cell.setSelected(false);
                unSetCellMovable(moveable);
                return 5;
            } else {
                cell.setSelected(true);
                moveable = getMoveable(cell);
                setCellMovble(moveable);
                return 4;
            }
        } else if (type == HexxagonConstant.COPYABLE || type == HexxagonConstant.JUMPABLE) {
            unSetCellMovable(moveable);
            doMove(coordinate);
            getCellRound1(hexagon.xDim, hexagon.yDim, zone);
            score = getEnemyLost(zone);
            updateScore(score);
            return currentPlayer;
        }
        return 0;
    }

    /**
     * Cap nhat lai diem cua player moi luot di chuyen
     */
    private static void updateScore(int score) {
        int score1 = 0;
        int score2 = 0;
        if (currentPlayer == HexxagonConstant.PLAYER1) {
            score1 = 0 + score;
            score2 = 0 - score;
        } else {
            score1 = 0 - score;
            score2 = 0 + score;
        }

        Gameplay.scorePlayer1 = Gameplay.scorePlayer1 + score1;
        Gameplay.scorePlayer2 = Gameplay.scorePlayer2 + score2;
    }

    /**
     * Lay ra cell dang duoc selected
     * 
     * @return Coordinate
     */
    private static Coordinate getCellSelected() {
        Coordinate coordinate = null;
        boolean isDone = false;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (Map.maps[i][j].isSelected()) {
                    coordinate = new Coordinate(i, j);
                    isDone = true;
                    break;
                }
            }
            if (isDone)
                break;
        }
        return coordinate;
    }

    /**
     * Tinh toan ca di chuyen copy va jump
     * 
     * @param player Di chuyen quan cua player 1 hoac player 2
     * @return Di chuyển thành công hay không </br> True : Thành công</br> False
     *         : Thất bại
     */
    private static void doMove(Coordinate coordinate) {
        if (coordinate.getMoveType() == HexxagonConstant.COPYABLE) {
            Map.maps[coordinate.getX()][coordinate.getY()].setType(currentPlayer);
        } else if (coordinate.getMoveType() == HexxagonConstant.JUMPABLE) {
            Coordinate cellSelected = getCellSelected();
            Map.maps[cellSelected.getX()][cellSelected.getY()]
                    .setType(HexxagonConstant.CELL_DEFAULT);
            Map.maps[coordinate.getX()][coordinate.getY()].setType(currentPlayer);
        }
    }

    /**
     * Tim ra tat ca cell co the move được (vòng 1 và 2)
     * 
     * @param cell
     */
    private static ArrayList<Coordinate> getMoveable(Cell cell) {
        int x;
        int y;

        ArrayList<Coordinate> around = new ArrayList<Coordinate>();

        Hexagon hexagon = (Hexagon)cell.getSprite();
        x = hexagon.xDim;
        y = hexagon.yDim;
        // Round 1
        getCellRound1(x, y, around);
        // Round 2
        getCellRound2(x, y, around);
        return around;
    }

    private static void getCellRound2(int x, int y, ArrayList<Coordinate> around) {
        int x2 = x + 2;
        int y2 = y - 2;
        if (x2 >= 0 && x2 <= 8 && y2 >= 0 && y2 <= 8) {
            around.add(new Coordinate(x2, y2, HexxagonConstant.JUMPABLE));
        }
        y2++;
        if (x2 >= 0 && x2 <= 8 && y2 >= 0 && y2 <= 8) {
            around.add(new Coordinate(x2, y2, HexxagonConstant.JUMPABLE));
        }
        y2++;
        if (x2 >= 0 && x2 <= 8 && y2 >= 0 && y2 <= 8) {
            around.add(new Coordinate(x2, y2, HexxagonConstant.JUMPABLE));
        }
        // Check canh 2. (Moi canh 2 diem).
        x2--;
        y2++;
        if (x2 >= 0 && x2 <= 8 && y2 >= 0 && y2 <= 8) {
            around.add(new Coordinate(x2, y2, HexxagonConstant.JUMPABLE));
        }
        x2--;
        y2++;
        if (x2 >= 0 && x2 <= 8 && y2 >= 0 && y2 <= 8) {
            around.add(new Coordinate(x2, y2, HexxagonConstant.JUMPABLE));
        }
        // Check canh 3.
        x2--;
        if (x2 >= 0 && x2 <= 8 && y2 >= 0 && y2 <= 8) {
            around.add(new Coordinate(x2, y2, HexxagonConstant.JUMPABLE));
        }
        x2--;
        if (x2 >= 0 && x2 <= 8 && y2 >= 0 && y2 <= 8) {
            around.add(new Coordinate(x2, y2, HexxagonConstant.JUMPABLE));
        }
        // Check canh 4
        y2--;
        if (x2 >= 0 && x2 <= 8 && y2 >= 0 && y2 <= 8) {
            around.add(new Coordinate(x2, y2, HexxagonConstant.JUMPABLE));
        }
        y2--;
        if (x2 >= 0 && x2 <= 8 && y2 >= 0 && y2 <= 8) {
            around.add(new Coordinate(x2, y2, HexxagonConstant.JUMPABLE));
        }
        // Check canh 5.
        x2++;
        y2--;
        if (x2 >= 0 && x2 <= 8 && y2 >= 0 && y2 <= 8) {
            around.add(new Coordinate(x2, y2, HexxagonConstant.JUMPABLE));
        }
        x2++;
        y2--;
        if (x2 >= 0 && x2 <= 8 && y2 >= 0 && y2 <= 8) {
            around.add(new Coordinate(x2, y2, HexxagonConstant.JUMPABLE));
        }
        // Check canh 6.
        x2++;
        if (x2 >= 0 && x2 <= 8 && y2 >= 0 && y2 <= 8) {
            around.add(new Coordinate(x2, y2, HexxagonConstant.JUMPABLE));
        }
    }

    /**
     * Lay ra các ô xung quanh cell
     * 
     * @param cell
     * @param around : Các ô lấy được sẽ được lưu vào một ArrayList của param
     *            truyền vào
     */
    private static void getCellRound1(int x, int y, ArrayList<Coordinate> around) {
        // Hexagon hexagon = (Hexagon)cell.getSprite();
        // int x = hexagon.xDim;
        // int y = hexagon.yDim;

        int totalRow = x + y;
        // Check top 1
        if (x + 1 <= totalRow && y - 1 >= 0) {
            around.add(new Coordinate(x + 1, y - 1, HexxagonConstant.COPYABLE));
        }
        // Check buttom 4
        if (x - 1 >= 0 && y + 1 <= totalRow) {
            around.add(new Coordinate(x - 1, y + 1, HexxagonConstant.COPYABLE));
        }
        // Check 2 va 5
        if (x + 1 <= 8) {
            around.add(new Coordinate(x + 1, y, HexxagonConstant.COPYABLE));
        }
        if (x - 1 >= 0) {
            around.add(new Coordinate(x - 1, y, HexxagonConstant.COPYABLE));
        }
        // Check 3 va 6
        if (y + 1 <= 8) {
            around.add(new Coordinate(x, y + 1, HexxagonConstant.COPYABLE));
        }
        if (y - 1 >= 0) {
            around.add(new Coordinate(x, y - 1, HexxagonConstant.COPYABLE));
        }
    }

    /**
     * Set type = CELL_MOVEABLE cua Cell
     * 
     * @param cells
     */
    private static void setCellMovble(ArrayList<Coordinate> movbleCell) {
        int length = movbleCell.size();
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                Coordinate coordinate = movbleCell.get(i);
                if (Map.maps[coordinate.getX()][coordinate.getY()].getType() == HexxagonConstant.CELL_DEFAULT) {
                    Map.maps[coordinate.getX()][coordinate.getY()]
                            .setType(coordinate.getMoveType());
                }
            }
        }
    }

    /**
     * Ham tinh cac o Cell co the di chuyen duoc
     * 
     * @param coordinate Toa do cua Cell
     * @return : Tra ve toan bo Cell co the di chuyen duoc
     */
    public static ArrayList<Coordinate> getAllCellMovable(Coordinate coordinate) {
        ArrayList<Coordinate> rs = new ArrayList<Coordinate>();
        getCellRound1(coordinate.getX(), coordinate.getY(), rs);
        getCellRound2(coordinate.getX(), coordinate.getY(), rs);
        int length = rs.size();
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                Coordinate coordinate1 = rs.get(i);
                if (Map.maps[coordinate1.getX()][coordinate1.getY()].getType() == HexxagonConstant.CELL_DEFAULT) {
                    rs.add(coordinate1);
                }
            }
        }
        return rs;
    }

    /**
     * Huy trang thai movble cua Cell
     * 
     * @param movbleCell
     */
    private static void unSetCellMovable(ArrayList<Coordinate> movbleCell) {
        int length = movbleCell.size();
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                Coordinate coordinate = movbleCell.get(i);
                Map.maps[coordinate.getX()][coordinate.getY()]
                        .setType(HexxagonConstant.CELL_DEFAULT);
            }
        }
    }

    /**
     * Lay ra duoc cac so luong quan se bi tieu diet Dong thoi thiet lap
     * 
     * @param array
     * @return
     */
    private static int getEnemyLost(ArrayList<Coordinate> array) {
        int length = array.size();
        int count = 0;
        for (int i = 0; i < length; i++) {
            Coordinate coordinate = array.get(i);
            if (currentPlayer != Map.maps[coordinate.getX()][coordinate.getY()].getType()) {
                Map.maps[coordinate.getX()][coordinate.getY()].setType(currentPlayer);
                count++;
            }
        }
        return count;
    }

    /**
     * @param player HexxagonConstant.PLAYER1 hoac HexxagonConstant.PLAYER2
     * @return Tra ve toan bo so quan cua player
     */
    public static ArrayList<Coordinate> getAllUnitCoordinates(int player) {
        ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (Map.maps[i][j].getType() == player) {
                    Coordinate coordinate = new Coordinate(i, j);
                    coordinates.add(coordinate);
                }

            }
        }
        return coordinates;
    }
}
