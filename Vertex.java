package gem;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public final class Vertex {
    
    private final Circle circle;
    private final Text text;
    private final List<Line> lines = new ArrayList<>();
    
    private static final double NODE_RADIUS         = 50;
    private static final Color  NODE_COLOR          = Color.YELLOWGREEN;
    private static final Color  NODE_OUTLINE_COLOR  = Color.BLACK;
    
    public Vertex(double x, double y, String s) {
        circle = new Circle(x, y, NODE_RADIUS, NODE_COLOR);
        circle.setStroke(NODE_OUTLINE_COLOR);
        text = new Text(s);
        relocateText();
    }
    
    public Circle getCircle() { return circle; }
    public Text getText() { return text; }
    public List<Line> getLines() { return lines; }
    
    /*
        Connects two vertices
    */
    public void connect(Vertex other) {
        Line line = new Line(
                this.circle.getCenterX(), this.circle.getCenterY(),
                other.circle.getCenterX(), other.circle.getCenterY());
        this.lines.add(line);
        other.lines.add(line);
    }
    
    /*
        Moves a circle and readjusts the text
    */
    public void moveVertex(double x, double y) {
        relocateLines(x, y);
        circle.setCenterX(x);
        circle.setCenterY(y);
        relocateText();
    }
    
    /*
        Moves all lines connected to this vertex to the new position of the vertex
    */
    private void relocateLines(double x, double y) {
        for(Line line : lines) {
            if(line.getStartX() == circle.getCenterX() && line.getStartY() == circle.getCenterY()) {
                line.setStartX(x);
                line.setStartY(y);
            }
            else {
                line.setEndX(x);
                line.setEndY(y);
            }
        }
    }
    
    /*
        Moves the text to the center of the circle
    */
    private void relocateText() {
        double x = circle.getCenterX();
        double y = circle.getCenterY();
        double textWidth = text.getBoundsInLocal().getWidth();
        double textHeight = text.getBoundsInLocal().getHeight();
        text.relocate(x - textWidth / 2, y - textHeight / 2);
    }
    
    /*
        Calculates the distance between two vertices
    */
    public double distanceTo(Vertex other) {
        double dX = Math.abs(this.circle.getCenterX() - other.getCircle().getCenterX());
        double dY = Math.abs(this.circle.getCenterY() - other.getCircle().getCenterY());
        return Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
    }
    
    @Override
    public String toString() {
        return "[Vertex] position: (" + (int)circle.getCenterX()
                + "," + (int)circle.getCenterY()
                + "), text: " + text.getText();
    }
}