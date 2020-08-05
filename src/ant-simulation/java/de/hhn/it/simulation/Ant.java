package de.hhn.it.simulation;

import de.hhn.it.ui.AntModelGraphic;
import javafx.scene.paint.Color;

import java.util.concurrent.ThreadLocalRandom;
import static de.hhn.it.simulation.Helper.radianToDegree;
import static java.lang.Math.atan;


/**
 * Mit der Klasse {@link Ant} werden Ameisen in der Simulation funktional
 * abgebildet. Ameisen definieren ihre Position und Rotation (in Grad).
 *
 * @see AntModelGraphic
 * @author Ailn Gavrila
 */
public class Ant {


    private AntModelGraphic graphic;

    private String antText;

    private Food food;

    private AntHill antHill;

    private double x, y, rotation;

    private boolean hasFood = false;

    private boolean goRandom = true;
    private boolean goingToHill = false;


    /**
     * @param x x
     * @param y y
     * @param rotation r
     * Constructor
     */
    public Ant(double x, double y, double rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.antText = "";
        graphic = new AntModelGraphic();
    }

    /**
     * @param height h
     * @param width w
     * Fuhrt einen Simulationsschritt durch und laesst die Ameise jeweils einen Schritt random machen
     */
    public void doSimulationStep(double height, double width) {

        if (y <= 10 || y >= height)
            this.rotation = this.rotation+180;

        else if(x < 10)
            this.rotation = this.rotation+180;

        if(x >= width) {
            this.rotation = this.rotation+180;
        }
        if(goRandom && !goingToHill) {
            this.rotation = ThreadLocalRandom.current().nextDouble(this.rotation - 5, this.rotation + 5);
        }
            double cosX = Math.cos(Math.toRadians(360 - this.rotation)) * 4;
            double sinY = Math.sin(Math.toRadians(360 - this.rotation)) * 4;
            x += cosX;
            y += sinY;
    }

    public void setGraphic(AntModelGraphic graphic) {
        this.graphic = graphic;
    }

    public AntModelGraphic getGraphic() {
        return this.graphic;
    }

    /**
     * @param goRandom
     * Ameise random gehen oder nicht
     */
    public void setGoRandom(boolean goRandom){
        this.goRandom = goRandom;
    }


    /**
     * @param coordinateX x
     * @param coordinateY y
     * @return true wenn es in der Naehe vono etwas ist and false wenn nicht
     */
    public boolean isNearToSomething(double coordinateX, double coordinateY){
        if(x >= (coordinateX - 20) && (x <= coordinateX + 20) && (y >= (coordinateY - 20)) && (y <= (coordinateY + 20))){
            return true;
        }
        return false;
    }

    /**
     * @return true wenn diese Ameise Nahrung transportiert
     */
    public boolean checkHasFood() {
        return this.hasFood;
    }

    /**
     * Die Ameise nimmt Nahrung von Futter und die Ameise transportiert Nahrung
     */
    public void takeFood(){
        this.hasFood = true;
    }

    /**
     * Ameise tut das Futter im Anthill und hat nicht mehr Food
     */
    public void putFood(){
        this.hasFood = false;
    }

    /**
     * @param food
     * Die Ameise weiß wo sich das Futter befindet
     */
    public void rememberFutter(Food food){
        this.food = food;
    }

    public void rememberAnthill(AntHill antHill){this.antHill = antHill;}
    /**
     * @return die Lage der Futter
     */
    public Food getFood() { return this.food; }

    public void goToDirection(double coordinateX, double coordinateY){
        if(this.x > coordinateX)
            this.rotation = 180-radianToDegree(atan((coordinateY - this.y)/(coordinateX - this.x)));
        else this.rotation = 360-radianToDegree(atan((coordinateY - this.y)/(coordinateX - this.x)));
    }

    public double getRotation() {
        return rotation;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setGoingToHill(boolean goingToHill) {
        this.goingToHill = goingToHill;
    }

    /**
     * @return
     * Wo die Ameise gehoert
     */
    public AntHill getAntHill() {
        return antHill;
    }

    /**
     * @return {@code true} wenn diese Ameise Nahrung transportiert.
     */
    public boolean isCarryingFood() {
        return false;
    }

    /**
     * Liefert die grafische Repräsentation der Ameise. Es wird immer die
     * gleiche {@link AntModelGraphic} Instanz zurückgegeben.
     *
     * @return die grafische Repräsentation dieser Ameise, nie {@code null}
     */
    public AntModelGraphic getSimulationGraphic() {
        return graphic;
    }

    /**
     * @return Beschreibungstext, kann {@code null} sein.
     */
    public String getText() {
        // Frei nach Joachim Rigelnatz: "Die Ameisen"
        return this.antText;
    }
}