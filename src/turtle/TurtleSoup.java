/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package turtle;

import java.util.List;
import java.util.ArrayList;

public class TurtleSoup {

    /**
     * Draw a square.
     * 
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
        for (int i = 0; i < 4; i++) {
            turtle.forward(sideLength);
            turtle.turn(90);
        }
    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
        return 180 - 360.0/sides;
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
        return (int)Math.round(360.0/(180 - angle));
    }

    /**
     * requires: turtle to be at (0, 0) with heading of 0.0
     *
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     * 
     * @param turtle the turtle context
     * @param sides number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
        double interiorAngle = calculateRegularPolygonAngle(sides);
        // Sets initial direction
        turtle.turn(90);
        // Iterates through sides to draw polygon
        for (int i = 0; i < sides; i++) {
            turtle.forward(sideLength);
            turtle.turn(180 + interiorAngle);
        }
    }

    /**
     * Given the current direction, current location, and a target location, calculate the heading
     * towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentHeading. The angle must be expressed in
     * degrees, where 0 <= angle < 360. 
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentHeading current direction as clockwise from north
     * @param currentX current location x-coordinate
     * @param currentY current location y-coordinate
     * @param targetX target point x-coordinate
     * @param targetY target point y-coordinate
     * @return adjustment to heading (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateHeadingToPoint(double currentHeading, int currentX, int currentY,
                                                 int targetX, int targetY) {

        double currentHeadingInRange; // current heading in 0 - 360 degrees
        if (currentHeading < 0) currentHeadingInRange = currentHeading + (1 + Math.floor(currentHeading/360)) * 360;
        else currentHeadingInRange = currentHeading - Math.floor(currentHeading/360) * 360;

        // calculates heading to target point relative to current point
        double headingToPoint, atan2Value;
        atan2Value = Math.toDegrees(Math.atan2(targetY - currentY, targetX - currentX));
        headingToPoint = 90 - atan2Value + ((atan2Value > 90)? 360:0);

        // calculates and returns the amount to turn to get to target point
        return (headingToPoint >= currentHeadingInRange)? headingToPoint - currentHeadingInRange : 360 + headingToPoint - currentHeadingInRange;
    }

    /**
     * Given a sequence of points, calculate the heading adjustments needed to get from each point
     * to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateHeadingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of heading adjustments between points, of size 0 if (# of points) == 0,
     *         otherwise of size (# of points) - 1
     */
    public static List<Double> calculateHeadings(List<Integer> xCoords, List<Integer> yCoords) {
        ArrayList<Double> headings = new ArrayList<>();
        double currentHeading = 0;
        for (int i = 0; i < xCoords.size() - 1; i++) {
            double headingAdjustment = calculateHeadingToPoint(currentHeading, xCoords.get(i), yCoords.get(i),
                    xCoords.get(i + 1), yCoords.get(i + 1));
            headings.add(headingAdjustment);
            currentHeading += (currentHeading + headingAdjustment < 360)? headingAdjustment: headingAdjustment - 360;
        }
        return headings;
    }

    /**
     * Draw your personal, custom art.
     * 
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     * 
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {
        drawSnowflake(turtle, 150);
    }

    /**
     * Draws a three-sided snowflake of three koch curves.
     * Returns heading to original at the end.
     *
     * @param turtle the turtle context
     * @param sideLength side length of three-sided snowflake
     */
    public static void drawSnowflake(Turtle turtle, int sideLength) {
        turtle.turn(150);
        for (int i = 0; i < 3; i++) {
            turtle.turn(240);
            drawKochCurve(turtle,3, sideLength);
        }
        turtle.turn(210);
    }

    /**
     * Recursively draws a koch curve in the turtle's current direction.
     * The curve is not exactly at the given length because the forward
     * function does not take double values.
     *
     * @param level iterations of the koch curve
     * @param length length of curve in initial direction
     */
    private static void drawKochCurve(Turtle turtle, int level, int length) {
        if (level == 0) {
            turtle.forward((int) Math.ceil(length/3.0));
            turtle.turn(60);
            turtle.forward((int) Math.ceil(length/3.0));
            turtle.turn(240);
            turtle.forward((int) Math.ceil(length/3.0));
            turtle.turn(60);
            turtle.forward((int) Math.ceil(length/3.0));
        } else {
            drawKochCurve(turtle,level - 1, (int) Math.ceil(length/3.0));
            turtle.turn(60);
            drawKochCurve(turtle,level - 1, (int) Math.ceil(length/3.0));
            turtle.turn(240);
            drawKochCurve(turtle,level - 1, (int) Math.ceil(length/3.0));
            turtle.turn(60);
            drawKochCurve(turtle,level - 1, (int) Math.ceil(length/3.0));
        }
    }

    /**
     * Main method.
     * 
     * This is the method that runs when you run "java TurtleSoup".
     * 
     * @param args unused
     */
    public static void main(String args[]) {
        DrawableTurtle turtle = new DrawableTurtle();

        drawPersonalArt(turtle);

        // draw the window
        turtle.draw();
    }

}
