package com.codenjoy.dojo.snake.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.snake.client.Solver.Astar;
import com.codenjoy.dojo.snake.client.Solver.MyPoint;
import com.codenjoy.dojo.snake.model.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

  private Dice dice;
  private Board board;

  public YourSolver(Dice dice) {
    this.dice = dice;
  }

  @Override
  public String get(Board board) {
    this.board = board;
    if (board.isGameOver()) return Direction.RIGHT.toString();

    Point head = board.getHead();
    Point apple = board.getApples().get(0);
    List<Point> snake = board.getSnake();
    int length = snake.size();
    List<Point> walls = board.getWalls();
    List<Point> stones = board.getStones();

    ArrayList<ArrayList<Integer>> grid = new ArrayList<>();
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

    String res = Direction.DOWN.toString();
    if (Astar.hasNext()) res = Astar.nextCommand();
    else {
      MyPoint h = new MyPoint(head.getX(), head.getY());
      MyPoint t = new MyPoint(apple.getX(), apple.getY());
      if (length > 45) {
        t = new MyPoint(stones.get(0).getX(), stones.get(0).getY());
        grid.get(t.getY()).set(t.getX(), 0);
      }
      Astar.solve(grid, h, t);
      if (Astar.hasNext())
        res = Astar.nextCommand();
      else {
        System.out.println("Following my tail");
        Point tail = board.get(Elements.TAIL_END_RIGHT,
            Elements.TAIL_END_LEFT,
            Elements.TAIL_END_UP,
            Elements.TAIL_END_DOWN).get(0);

        t = new MyPoint(tail.getX(), tail.getY());
        grid.get(t.getY()).set(t.getX(), 0);
        Astar.solve(grid, h, t);
        System.out.println(Astar.path);
        if (Astar.hasNext()) {
          res = Astar.nextCommand();
          System.out.println("Found path to my tail");
        } else {
          System.out.println("Getting to stone");
          t = new MyPoint(stones.get(0).getX(), stones.get(0).getY());
          grid.get(t.getY()).set(t.getX(), 0);
          Astar.solve(grid, h, t);
          if (Astar.hasNext())
            res = Astar.nextCommand();
        }
      }
    }
    return res;
  }

  /*
   int x = 0;
      int y = 0;
      String dir = "";
      if(apple != null) {

          if (head.getX() < apple.getX()) x--;
          else if (head.getX() > apple.getX()) x++;
          if (head.getY() < apple.getY()) y--;
          else if (head.getY() > apple.getY()) y++;
          if (y == 1) dir = Direction.DOWN.toString();
          if (y == -1) dir = Direction.UP.toString();
          if (x == -1) dir = Direction.RIGHT.toString();
          if (x == 1) dir = Direction.LEFT.toString();
      }else dir = Direction.UP.toString();
      //System.out.println(board.toString());
      return dir;
   */
  public static void main(String[] args) {
    WebSocketRunner.runClient(
        // paste here board page url from browser after registration
        "http://104.248.23.201/codenjoy-contest/board/player/6qxc14yqx2fv1j6y4wfw?code=2438855538772707227",
        new YourSolver(new RandomDice()),
        new Board());
  }

}
