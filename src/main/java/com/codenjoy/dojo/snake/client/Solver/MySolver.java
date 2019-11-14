package com.codenjoy.dojo.snake.client.Solver;

import com.codenjoy.dojo.services.Direction;

import java.util.*;

public class MySolver {
  public static Optional<String> path = Optional.empty();

  public static String nextCommand() {
    String res = path.orElse("DOWN");
    path = Optional.empty();
    return res;
  }

  public static boolean hasNext() {
    return path.isPresent();
  }

  private static Direction getDiff(MyPoint p1, MyPoint p2) {

    if (p1.getX() < p2.getX()) return Direction.LEFT;
    if (p1.getX() > p2.getX()) return Direction.RIGHT;
    if (p1.getY() < p2.getY()) return Direction.DOWN;

    return Direction.UP;

  }

  private static void calculatePath(Map<MyPoint, MyPoint> cameFrom, MyPoint head, MyPoint target) {
    MyPoint prev = target;
    MyPoint curr = cameFrom.get(prev);
    while (!curr.equals(head)) {
      prev = curr;
      curr = cameFrom.get(prev);
      path = Optional.ofNullable(getDiff(prev, curr).toString());
//      System.out.println(path.get());
    }
    path = Optional.ofNullable(getDiff(prev, curr).toString());
  }

  public static void solve(ArrayList<ArrayList<Integer>> grid, MyPoint head, MyPoint target) {
    Deque<MyPoint> openSet = new ArrayDeque<>();
    openSet.add(head);
    Map<MyPoint, MyPoint> cameFrom = new HashMap<>();
    Map<MyPoint, Boolean> visited = new HashMap<>();
    visited.put(head, true);

    while (!openSet.isEmpty()) {
      MyPoint current = openSet.poll();
      if (current.equals(target)) {
        calculatePath(cameFrom, head, target);
        return;
      }

      ArrayList<MyPoint> neigh = getNeigh(grid, current, grid.size(), grid.get(0).size());

      for (MyPoint point : neigh)
        if (!visited.containsKey(point) || !visited.get(point)) {
          openSet.add(point);
          visited.put(point, true);
          cameFrom.put(point, current);
        }
    }
  }

  private static ArrayList<MyPoint> getNeigh(ArrayList<ArrayList<Integer>> grid, MyPoint curr, int height, int width) {
    ArrayList<MyPoint> res = new ArrayList<>();

    if (curr.getY() - 1 > 0 && grid.get(curr.getY() - 1).get(curr.getX()) == 0) {
      res.add(new MyPoint(curr.getX(), curr.getY() - 1));
    }
    if (curr.getY() + 1 < height && grid.get(curr.getY() + 1).get(curr.getX()) == 0) {
      res.add(new MyPoint(curr.getX(), curr.getY() + 1));
    }
    if (curr.getX() - 1 > 0 && grid.get(curr.getY()).get(curr.getX() - 1) == 0) {
      res.add(new MyPoint(curr.getX() - 1, curr.getY()));
    }
    if (curr.getX() + 1 < width && grid.get(curr.getY()).get(curr.getX() + 1) == 0) {
      res.add(new MyPoint(curr.getX() + 1, curr.getY()));
    }
    return res;
  }

}
