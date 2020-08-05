package de.hhn.it.simulation;

import de.hhn.it.ui.ImageFileGraphic;
import de.hhn.it.ui.SimulationGraphic;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 *@author Ailn Gavrila
 */

public class NaturalEnemy {

    private SimulationGraphic simulationGraphic;

    private int foodUnits;

    private String text;

    private double x, y, rotation;

    private boolean visible;

    /**
     * @param x x
     * @param y y
     * @param rotation r
     * Constructor
     */
    public NaturalEnemy(double x, double y, double rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.foodUnits = 0;
        this.text = "";
        this.simulationGraphic = new ImageFileGraphic("bug.png");
        this.visible = true;
    }

    public boolean getVisible(){
        return this.visible;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }

    /**
     * Fuehrt einen Simulationsschritt durch.
     */
    public void doSimulationStep(double height, double width) {

        if (y <= 10 || y >= height)
            this.rotation = this.rotation+180;

        else if(x < 10)
            this.rotation = this.rotation+180;

        if(x >= width) {
            this.rotation = this.rotation+180;
        }
        else {
            this.rotation = ThreadLocalRandom.current().nextDouble(this.rotation - 5, this.rotation + 5);
        }
        double cosX = Math.cos(Math.toRadians(360 - this.rotation)) * 4;
        double sinY = Math.sin(Math.toRadians(360 - this.rotation)) * 4;
        x += cosX;
        y += sinY;
    }

    /**
     * @return Beschreibungstext des Ameisenhaufens, kann {@code null} sein.
     */
    public String getText() {
        return (text + " mit " + this.foodUnits + " Futtereinheiten");
    }

    /**
     * Gibt die Anzahl der aktuell im Ameisenhaufen vorhandenen Futtereinheiten zurueck.
     * @return die aktuelle Anzahl der Futtereinheiten des Ameisenhaufens
     */
    public int getFoodUnits() {
        return foodUnits;
    }


    /**
     * Setzt die Anzahl der aktuell im Ameisenhaufen vorhandenen Futtereinheiten.
     * Hinweis: Eine negative Anzahl an Futtereinheiten wird ignoriert.
     * @param foodUnits die neue Anzahl der Futtereinheiten
     */
    public void setFoodUnits(int foodUnits) {
        if(foodUnits >= 0) {
            this.foodUnits = foodUnits;
        }
    }

    /**
     * Liefert die grafische Repräsentation des AntHill. Es
     * wird immer die gleiche {@link SimulationGraphic} Instanz zurueckgegeben.
     *
     * @return die grafische Repräsentation des AntHill, nie
     * {@code null}
     */
    public SimulationGraphic getSimulationGraphic() {
        return simulationGraphic;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRotation() {
        return rotation;
    }


}