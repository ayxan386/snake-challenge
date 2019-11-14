package com.codenjoy.dojo.snake.client.Solver;

import java.util.Objects;

public class MyPoint {
  private int x,y;

  public MyPoint(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MyPoint)) return false;
    MyPoint myPoint = (MyPoint) o;
    return x == myPoint.x &&
        y == myPoint.y;
  }

  @Override
  public int hashCode() {
    return x * 500 + y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public String toString() {
    return String.format("[%d,%d]\n",getX(),getY());
  }
}
