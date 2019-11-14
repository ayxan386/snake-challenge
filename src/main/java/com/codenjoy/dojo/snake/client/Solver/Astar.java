package com.codenjoy.dojo.snake.client.Solver;

import com.codenjoy.dojo.services.Direction;

import java.util.*;

public class Astar {
  public static Deque<String> path = new ArrayDeque<>();
  private static int i = 0;
  private static final int every = 1;
  public static String nextCommand() {
    if(i != 0 && i % every == 0){
      i = 0;
      path = new ArrayDeque<>();
      return "null";
    }
    i++;
    return path.pop();
  }

  public static boolean hasNext() {
    if(i != 0 && i % every == 0){
      i = 0;
      path = new ArrayDeque<>();
      return false;
    }
    return !path.isEmpty();
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
    path = new ArrayDeque<>();
    while (!curr.equals(head)) {
      path.push(getDiff(prev, curr).toString());
      prev = curr;
      curr = cameFrom.get(prev);
    }
    path.push(getDiff(prev, head).toString());
  }

  public static void solve(ArrayList<ArrayList<Integer>> grid, MyPoint head, MyPoint target) {
    i = 0;
    Deque<MyPoint> openSet = new ArrayDeque<>();
    openSet.add(head);
    Map<MyPoint, MyPoint> cameFrom = new HashMap<>();

    Map<MyPoint, Integer> gScore = defaultInfinity(grid);
    gScore.put(head, 0);
    Map<MyPoint, Integer> fScore = defaultInfinity(grid);
    fScore.put(head, h(head, target));
//    for(ArrayList<Integer> row : grid)
//      System.out.println(row);
    while (!openSet.isEmpty()) {
      MyPoint current = getMinimal(openSet, fScore);
      if (current.equals(target)) {
        calculatePath(cameFrom, head, target);
        return;
      }
      openSet.remove(current);

      ArrayList<MyPoint> neigh = getNeigh(grid, current, grid.size(), grid.get(0).size());

      for (MyPoint point : neigh) {
        if (gScore.get(current) + 1 < gScore.get(point)) {
          cameFrom.put(point, current);
          gScore.put(point, gScore.get(current) + 1);
          fScore.put(point, gScore.get(point) + h(point, target));

          if (!openSet.contains(point)) openSet.add(point);
        }
      }

    }

  }

  private static ArrayList<MyPoint> getNeigh(ArrayList<ArrayList<Integer>> grid, MyPoint curr, int height, int width) {
    ArrayList<MyPoint> res = new ArrayList<>();
    if (curr.getY() - 1 > 0 && grid.get(curr.getY() - 1).get(curr.getX()) == 0)
      res.add(new MyPoint(curr.getX(), curr.getY() - 1));
    if (curr.getY() + 1 < height && grid.get(curr.getY() + 1).get(curr.getX()) == 0)
      res.add(new MyPoint(curr.getX(), curr.getY() + 1));
    if (curr.getX() - 1 > 0 && grid.get(curr.getY()).get(curr.getX() - 1) == 0) {
      res.add(new MyPoint(curr.getX() - 1, curr.getY()));
    }
    if (curr.getX() + 1 < width && grid.get(curr.getY()).get(curr.getX() + 1) == 0) {
      res.add(new MyPoint(curr.getX() + 1, curr.getY()));
    }
    return res;
  }

  private static MyPoint getMinimal(Deque<MyPoint> openSet, Map<MyPoint, Integer> fScore) {
    MyPoint res = openSet.getFirst();
    for (MyPoint point : openSet) {
      if (fScore.get(point) < fScore.get(res)) {
        res = point;
      }
    }
    return res;
  }

  private static Map<MyPoint, Integer> defaultInfinity(ArrayList<ArrayList<Integer>> grid) {
    HashMap<MyPoint, Integer> res = new HashMap<>();
    for (int i = 0; i < grid.size(); i++)
      for (int j = 0; j < grid.size(); j++) {
        res.put(new MyPoint(j, i), Integer.MAX_VALUE);
      }
    return res;
  }

  private static Integer h(MyPoint a, MyPoint b) {
    return (int) (Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
  }

}
