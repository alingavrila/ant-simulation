package de.hhn.it.simulation;

import de.hhn.it.ui.ImageFileGraphic;
import de.hhn.it.ui.SimulationGraphic;

import java.util.ArrayList;

/**
 *@author Ailn Gavrila
 */

public class Food {

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
    public Food(double x, double y, double rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.foodUnits = 0;
        this.text = "";
        this.simulationGraphic = new ImageFileGraphic("food.png");
        this.visible = true;
    }

    public boolean getVisible(){
        return this.visible;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
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