package gem;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class GEM extends Application {
    
    private static final List<Vertex> vertices = new ArrayList<>();
    
    private double startScale, startRotate;
    private boolean moveInProgress = false;
    private int touchPointId;
    private Point2D prevPos;
    
    private void init(Stage stage) {
        final Group root = new Group();
        final Group graph = new Group();
        Scene scene = new Scene(root, 1280, 720);
        
        // Create vertices
        Vertex vertex1 = new Vertex(300, 300, "NODE 1");
        Vertex vertex2 = new Vertex(500, 300, "NODE 2");
        Vertex vertex3 = new Vertex(400, 200, "NODE 3");
        vertices.add(vertex1);
        vertices.add(vertex2);
        vertices.add(vertex3);
        
        // Connect vertices to each other (adding lines between them)
        vertex1.connect(vertex2);
        vertex1.connect(vertex3);
        vertex2.connect(vertex3);
        
        // Add lines to graph
        List<Line> lineList = new ArrayList<>();
        for(Vertex v : vertices) {
            lineList.removeAll(v.getLines());
            lineList.addAll(v.getLines());
        }
        for(Line line : lineList) {
            graph.getChildren().add(line);
        }
        
        // Add circles and texts to graph
        graph.getChildren().addAll(
                vertex1.getCircle(), vertex2.getCircle(), vertex3.getCircle(),
                vertex1.getText(), vertex2.getText(), vertex3.getText());
        
        // Add graph to scene
        root.getChildren().add(graph);
        stage.setScene(scene);
        
        // Create button and add it to scene (REMOVE THIS LATER)
        Button button = new Button("UPDATE");
        button.setLayoutX(10);
        button.setLayoutY(10);
        root.getChildren().add(button);
        
        // Button action (REMOVE THIS LATER)
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                randomUpdate();
            }
        });
        
        
        graph.setOnTouchPressed(new EventHandler<TouchEvent>() {
            @Override public void handle(TouchEvent event) {
                if(moveInProgress == false) {
                    moveInProgress = true;
                    touchPointId = event.getTouchPoint().getId();
                    prevPos = new Point2D(event.getTouchPoint().getSceneX(), event.getTouchPoint().getSceneY());
                }
                event.consume();
            }
        });
        graph.setOnTouchMoved(new EventHandler<TouchEvent>() {
            @Override public void handle(TouchEvent event) {
                if(moveInProgress == true && event.getTouchPoint().getId() == touchPointId) {
                    Point2D currPos = new Point2D(event.getTouchPoint().getSceneX(), event.getTouchPoint().getSceneY());
                    double[] translationVector = new double[2];
                    translationVector[0] = currPos.getX() - prevPos.getX();
                    translationVector[1] = currPos.getY() - prevPos.getY();
                    
                    graph.setTranslateX(graph.getTranslateX() + translationVector[0]);
                    graph.setTranslateY(graph.getTranslateY() + translationVector[1]);
                    
                    prevPos = currPos;
                }
                event.consume();
            }
        });
        graph.setOnTouchReleased(new EventHandler<TouchEvent>() {
            @Override public void handle(TouchEvent event) {
                if(event.getTouchPoint().getId() == touchPointId) {
                    moveInProgress = false;
                }
                event.consume();
            }
        });
        
        
        graph.setOnZoomStarted(new EventHandler<ZoomEvent>() {
            @Override public void handle(ZoomEvent event) {
                startScale = graph.getScaleX();
                event.consume();
            }
        });
        graph.setOnZoom(new EventHandler<ZoomEvent>() {
            @Override public void handle(ZoomEvent event) {
                graph.setScaleX(startScale * event.getTotalZoomFactor());
                graph.setScaleY(startScale * event.getTotalZoomFactor());
                event.consume();
            }
        });
        
        
        graph.setOnRotationStarted(new EventHandler<RotateEvent>() {
            @Override public void handle(RotateEvent event) {
                startRotate = graph.getRotate();
                event.consume();
            }
        });
        graph.setOnRotate(new EventHandler<RotateEvent>() {           
            @Override public void handle(RotateEvent event) {
                graph.setRotate(startRotate + event.getTotalAngle());
                
                /*
                    Rotating text in real-time
                    Move loop to group.setOnRotationFinished() to rotate
                        text only when graph-rotation is finished
                */
                for(Vertex v : vertices) {
                    v.getText().setRotate(0 - graph.getRotate());
                }
                
                event.consume();
            }
        });
    }
    
    private void randomUpdate() {
        Vertex vertex = vertices.get((int)(Math.random() * 3));
        double x = Math.random() * 1280;
        double y = Math.random() * 720;
        vertex.moveVertex(x, y);
        
        // TESTING: calculates the distance between vertex 1 and 2
        /*
        Vertex v1 = vertices.get(0);
        Vertex v2 = vertices.get(1);
        System.out.println("Distance between node1 & node2: " + v1.distanceTo(v2));
        */
        
        debug();
    }
    
    private void debug() {
        System.out.println();
        for(Vertex v : vertices) {
            System.out.println(v);
        }
    }
    
    @Override public void start(Stage stage) throws Exception {
        init(stage);
        stage.show();
    }
    
    public static void main(String[] args) {
        System.out.println("JavaFX version: " + com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
        launch(args);
    }
}
