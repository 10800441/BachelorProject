import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/****************************************************
 **                Bachelor project                **
 ** DrawGraph.java                                 **
 ** Source: http://stackoverflow.com/questions/8693342/drawing-a-simple-line-graph-in-java
 ** Adjusted to work for a TSP instantiation       **
 ****************************************************
 */

public class DrawGraph extends JPanel {

    private int width = 200;
    private int heigth = 200;
    private int padding = 25;
    private int labelPadding = 25;
    private Color lineColor = new Color(44, 102, 230, 180);
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private int pointWidth = 8;
    private static DoublyLinkedListImpl<City> head;
    private static DoublyLinkedListImpl<City> tail;
    private static ArrayList<City> grid;


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / getMaxX();
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / getMaxY();

        List<Point> graphPoints = new ArrayList<>();
        for (City city: grid){
            int x1 = (int) (city.x * xScale + padding + labelPadding);
            int y1 = (int) (city.y * yScale + padding);
            graphPoints.add(new Point(x1, y1));
        }


        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.


        for (int i = 0; i < getMaxY() + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / (int) getMaxY() + padding + labelPadding);
            int y1 = y0;
             if (grid.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = "" + i;
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // and for x axis
        for (int i = 0; i < getMaxX(); i++) {
            if (grid.size() > 1) {
                int x0 = padding + labelPadding + (i * (getWidth() - padding * 2 - labelPadding)/getMaxX());
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - pointWidth;
                if ((i % ((int) ((grid.size() / 20.0)) + 1)) == 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                    g2.setColor(Color.black);
                    String xLabel = i + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }
                g2.drawLine(x0, y0, x1, y1);
            }
        }

        // create x and y axes
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        // Closing the tour
//        int startx = (int) (head.elementAt(1).x * xScale + padding + labelPadding);
//        int starty = (int) (head.elementAt(1).y * yScale + padding);
//        int endx = (int) (tail.elementAt(SOLUTION.size()).x * xScale + padding + labelPadding);
//        int endy = (int) (SOLUTION.elementAt(SOLUTION.size()).y * yScale + padding);
//        g2.drawLine(startx, starty, endx, endy);

        if(head != null){
            for (int i = 1; i <= head.size(); i++) {
                int x1 = graphPoints.get(head.elementAt(i).id).x;
                int y1 = graphPoints.get(head.elementAt(i).id).y;
                int x2 = graphPoints.get(tail.elementAt(i).id).x;
                int y2 = graphPoints.get(tail.elementAt(i).id).y;
                g2.drawLine(x1, y1, x2, y2);
            }
        }

        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        for (int i = 0; i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - pointWidth / 2;
            int y = graphPoints.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }
    }

    private int getMaxX() {
        int maxScore = 0;
        for (City city: grid) {
            if(city.x > maxScore) maxScore = city.x;
        }
        return maxScore+1;
    }

    private double getMaxY() {
        int maxScore = 0;
        for (City city: grid) {
            if(city.y > maxScore) maxScore = city.y;
        }
        return maxScore+1;
    }

    public static void createAndShowGui(Tour solution, ArrayList<City> solGrid) {
        if(solution != null){
            head = solution.head;
            tail = solution.tail;
        }

        grid = solGrid;

        DrawGraph mainPanel = new DrawGraph();
        mainPanel.setPreferredSize(new Dimension(200, 200));
        JFrame frame = new JFrame("DrawGraph");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.setSize(10,10);

        frame.setVisible(true);
    }


    public static void main(String[] args) {
        Tour T = null;
        ArrayList<City> A = new ArrayList<City>();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui(T,A);
            }
        });
    }
}