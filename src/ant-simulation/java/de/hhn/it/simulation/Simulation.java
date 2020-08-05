package de.hhn.it.simulation;

import de.hhn.it.ui.AntApplication;
import de.hhn.it.ui.AntModelGraphic;
import de.hhn.it.ui.UiManager;
import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Dies Klasse bildet das zentrale Element der Simulation. Sie koordiniert die
 * Bewegungen, das Erscheinen und Verschwinden der Simulationsteilnehmer. Die
 * Methode {@link Simulation#doSimulationStep()} wird vom Taktgeber der
 * grafischen Oberfläche in regelmäßigen Abständen aufgerufen.
 *
 * @see AntApplication#SIMULATION_FRAME_LENGTH
 * @author Alin Gavrila
 */
public class Simulation {

    private List<Ant> antList = new ArrayList<>();
    private final int antTotal = 25;
    private List<Food> foodList = new ArrayList<>();
    private int totalFood = 5;
    private List<NaturalEnemy> naturalEnemyList = new ArrayList<>();
    private int totalNaturalEnemies = 1;
    private List<AntHill> antHillList = new ArrayList<>();
    private final int totalAntHills = 2;
    private UiManager uiManager;
    private Timer timer;
    private double randomPoz, pozX, pozY;
    private int foodUnitInHill;
    private int randomRed, randomGreen, randomBlue;

    /**
     * @param uiManager uiManager
     * Constructor
     */
    public Simulation(UiManager uiManager) {

        this.uiManager = uiManager;

        /**
         * Bauen von Anthills und Ameisen
         */
        createAntHillAndAnt();
        for (int i = 0; i < antList.size(); i++) {
            uiManager.add(antList.get(i));
        }

        /**
         * Bauen von Futter
         */
        createFood();
        for (Food food : foodList) {
            uiManager.add(food);
        }


        /**
         * Bauen von NaturalEnemy
         */
        createNaturalEnemy();
        for (NaturalEnemy naturalEnemy : naturalEnemyList) {
            uiManager.add(naturalEnemy);
        }

        //lauft immer nach 10 Sekunden
        timer = new Timer();
        timer.schedule(new Run(), 10000, 10000);
    }


    /**
     * Classe Run
     * Baut neue Ameisen nach jeden 10 Sekunden wenn im antHill mehr als 10 FoodUnits sind
     */
    class Run extends TimerTask{
        public void run(){
                Platform.runLater(() -> {
                    for (int i = 0; i < totalAntHills; i++) {
                        foodUnitInHill = antHillList.get(i).getFoodUnits();
                        if (foodUnitInHill / 10 > 1) {
                            int randomAnt = ThreadLocalRandom.current().nextInt(1, foodUnitInHill / 10);
                            int antListSize = antList.size();
                            for (int j = antListSize; j < antListSize + randomAnt; j++) {
                                antHillList.get(i).createChildren(antList);
                                antList.get(j).rememberAnthill(antHillList.get(i));
                                Color antColor = antHillList.get(i).getColor();
                                antList.get(j).setGraphic(new AntModelGraphic(antColor, antColor, antColor, antColor, antColor, antColor));
                                uiManager.add(antList.get(j));
                            }
                            //System.out.println(antList.size());
                        }
                    }
                    if(totalFood == 0)
                        timer.cancel();
                });
            }
        }

    /**
     * Methode fuer Bauen von AntHill und Ant
     */
    private void createAntHillAndAnt(){
        for(int j=0; j<totalAntHills; j++) {
            randomRed = ThreadLocalRandom.current().nextInt(0,255);
            randomGreen = ThreadLocalRandom.current().nextInt(0,255);
            randomBlue = ThreadLocalRandom.current().nextInt(0,255);
            antHillList.add(new AntHill(ThreadLocalRandom.current().nextDouble(10,uiManager.getSimulationSurfaceWidth()-50),
                    ThreadLocalRandom.current().nextDouble(10,uiManager.getSimulationSurfaceHeight()-50),0,Color.rgb(randomRed,randomGreen,randomBlue)));
            antHillList.get(j).setFoodUnits(0);
            uiManager.add(antHillList.get(j));
            int antListSize = antList.size();
            for (int i = antListSize; i < antListSize + antTotal; i++) {
                randomPoz = ThreadLocalRandom.current().nextDouble(0, 360);
                pozX = ThreadLocalRandom.current().nextDouble(uiManager.getSimulationSurfaceWidth() - 25);
                pozY = ThreadLocalRandom.current().nextDouble(uiManager.getSimulationSurfaceHeight() - 25);
                this.antList.add(new Ant(pozX, pozY, randomPoz));
                antList.get(i).rememberAnthill(antHillList.get(j));
                Color antColor = antHillList.get(j).getColor();
                antList.get(i).setGraphic(new AntModelGraphic(antColor, antColor, antColor, antColor, antColor, antColor));
            }
        }
    }

    /**
     * Methode fuer Bauen von Futter
     */
    private void createFood(){
        for(int i = 0; i< totalFood; i++) {
            foodList.add(new Food(ThreadLocalRandom.current().nextDouble(25, uiManager.getSimulationSurfaceWidth() - 100),
                    ThreadLocalRandom.current().nextDouble(25, uiManager.getSimulationSurfaceHeight() - 25), 0));
            foodList.get(i).setFoodUnits(100);
        }
    }

    /**
     * Methode fuer Bauen von NaturalEnemy
     */
    private void createNaturalEnemy() {
        for (int i = 0; i < totalNaturalEnemies; i++) {
            naturalEnemyList.add(new NaturalEnemy(ThreadLocalRandom.current().nextDouble(25, uiManager.getSimulationSurfaceWidth() - 100),
                    ThreadLocalRandom.current().nextDouble(25, uiManager.getSimulationSurfaceHeight() - 25),
                    ThreadLocalRandom.current().nextDouble(0, 360)));
        }
    }
    /**
     * @param food f
     * @param ant a
     * Methode checkFood uberprueft ob Food noch Foodunits hat, und wenn es keine mehr hat, dann loeschen wir es
     */
    private void checkFood(Food food, Ant ant){
        if(ant.isNearToSomething(food.getX(),food.getY()) && !ant.checkHasFood() && food.getFoodUnits() > 0){
            ant.takeFood();
            ant.rememberFutter(food);
            food.setFoodUnits(food.getFoodUnits() - 1);
            ant.setGoRandom(false);
            ant.goToDirection(ant.getAntHill().getX(), ant.getAntHill().getY());
            ant.setGoingToHill(true);
        }
        if (food.getFoodUnits() == 0 && food.getVisible()) {
            for (Ant foodAnt: antList)
                if(foodAnt.getFood() == food)
                    foodAnt.setGoRandom(true);
            food.setVisible(false);
            uiManager.remove(food);
            totalFood -= 1;
        }
    }

    /**
     * @param otherAnt o
     * @param ant a
     * Mit diese Methode kommen auch andere Ameisen zum Futter wenn sie sich mit anderen Ameisen auf dem Weg treffen, die Futter tragen
     */
    private void directOtherAnt(Ant otherAnt, Ant ant) {
        if((otherAnt != ant) && (ant.getAntHill() == otherAnt.getAntHill())) {
            if(ant.checkHasFood() && !otherAnt.checkHasFood() && ant.isNearToSomething(otherAnt.getX(), otherAnt.getY())) {
                if(ant.getFood().getFoodUnits() > 0) {
                    otherAnt.setGoRandom(false);
                    otherAnt.rememberFutter(ant.getFood());
                    otherAnt.goToDirection(ant.getFood().getX(), ant.getFood().getY());
                }
            }
        }
    }

    /**
     * @param ant a
     * Wennn die Ameise nahe an der Hill mit dem Futter ist, dann wachst die Anzahl der Foodunits im Anthill und wenn es
     * noch Futter gibt, geht wieder und nimmt noch
     */
    private void putFoodDown(Ant ant) {
        if (ant.isNearToSomething(ant.getAntHill().getX(), ant.getAntHill().getY()) && ant.checkHasFood()) {
            ant.setGoingToHill(false);
            ant.getAntHill().setFoodUnits(ant.getAntHill().getFoodUnits() + 1);
            ant.putFood();
            if (ant.getFood().getFoodUnits() > 0) {
                ant.setGoRandom(false);
                ant.goToDirection(ant.getFood().getX(), ant.getFood().getY());
            }
        }
    }
        /**
         * Führt einen Simulationsschritt durch.
         */
        public void doSimulationStep() {
            for (Ant ant: antList) {
                ant.doSimulationStep(uiManager.getSimulationSurfaceHeight(), uiManager.getSimulationSurfaceWidth());
                if(totalFood > 0) {
                    for (Food food : foodList) {
                        checkFood(food, ant);
                    }
                }
                putFoodDown(ant);
                for(Ant otherAnt: antList) {
                    directOtherAnt(otherAnt, ant);
                }
            }
            for (int i = 0; i < totalNaturalEnemies; i++) {
                naturalEnemyList.get(i).doSimulationStep(uiManager.getSimulationSurfaceHeight(), uiManager.getSimulationSurfaceWidth());
            }
        }
}
