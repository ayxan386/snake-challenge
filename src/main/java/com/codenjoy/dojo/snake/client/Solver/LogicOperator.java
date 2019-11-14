package com.codenjoy.dojo.snake.client.Solver;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snake.client.Board;
import com.codenjoy.dojo.snake.model.Elements;

import java.util.ArrayList;
import java.util.List;

public class LogicOperator {

  public String step(Board board){
    if (board.isGameOver()) return Direction.RIGHT.toString();

    ArrayList<ArrayList<Integer>> grid;
    grid = initGrid(board);
    Point head = board.getHead();
    Point apple = board.getApples().get(0);
    int length = board.getSnake().size();
    List<Point> stones = board.getStones();

    String res = Direction.DOWN.toString();

    if (MySolver.hasNext()) res = MySolver.nextCommand();
    else {
      MyPoint h = new MyPoint(head.getX(), head.getY());
      MyPoint t = new MyPoint(apple.getX(), apple.getY());
      if (length > 55) {
        t = new MyPoint(stones.get(0).getX(), stones.get(0).getY());
        grid.get(t.getY()).set(t.getX(), 0);
      }

      MySolver.solve(grid, h, t);
      if (MySolver.hasNext())
        res = MySolver.nextCommand();
      else {
        Point tail = board.get(Elements.TAIL_END_RIGHT,
            Elements.TAIL_END_LEFT,
            Elements.TAIL_END_UP,
            Elements.TAIL_END_DOWN).get(0);
        t = new MyPoint(tail.getX(), tail.getY());
        grid.get(t.getY()).set(t.getX(), 0);
        MySolver.solve(grid, h, t);

        if (MySolver.hasNext()) {
          res = MySolver.nextCommand();
        } else {
          t = new MyPoint(stones.get(0).getX(), stones.get(0).getY());
          grid.get(t.getY()).set(t.getX(), 0);
          MySolver.solve(grid, h, t);

          if (MySolver.hasNext())
            res = MySolver.nextCommand();
        }
      }
    }
    return res;
  }

  private ArrayList<ArrayList<Integer>> initGrid(Board board) {
    ArrayList<ArrayList<Integer>> grid = new ArrayList<>();

    List<Point> snake = board.getSnake();
    List<Point> walls = board.getWalls();
    List<Point> stones = board.getStones();

    int size = board.size();

    for (int i = 0; i < size; i++) {
      ArrayList<Integer> row = new ArrayList<>();
      for (int j = 0; j < size; j++) {
        row.add(0);
      }
      grid.add(row);
    }

    for (Point p : snake) {
      grid.get(p.getY()).set(p.getX(), 3);
    }

    for (Point p : walls) {
      grid.get(p.getY()).set(p.getX(), 1);
    }
    for (Point p : stones) {
      grid.get(p.getY()).set(p.getX(), 2);
    }
    return grid;
  }
}
