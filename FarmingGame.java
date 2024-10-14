import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FarmingGame {
    static Scanner scanner = new Scanner(System.in);
    static int money = 500; 
    static int day = 1;
    static Map<String, Crop> crops = new HashMap<>();

    public static void main(String[] args) {
        initializeCrops();
        System.out.println("Welcome to the Farming Game!");

        while (true) {
            System.out.println("\n--- Day " + day + " ---");
            System.out.println("Money: $" + money);
            displayOptions();

            String choice = scanner.nextLine();
            if (choice.equals("5")) {
                System.out.println("Thanks for playing! Goodbye.");
                break;
            }

            handleChoice(choice);
            day++;
        }
    }

    static void initializeCrops() {
        crops.put("1", new Crop("Wheat", 20, 50, 3));
        crops.put("2", new Crop("Corn", 30, 80, 5));
        crops.put("3", new Crop("Carrot", 15, 40, 2));
    }

    static void displayOptions() {
        System.out.println("What would you like to do?");
        System.out.println("1. Plant crops");
        System.out.println("2. Water crops");
        System.out.println("3. Harvest crops");
        System.out.println("4. View crop status");
        System.out.println("5. Exit game");
        System.out.print("Enter choice: ");
    }

    static void handleChoice(String choice) {
        switch (choice) {
            case "1" -> plantCrops();
            case "2" -> waterCrops();
            case "3" -> harvestCrops();
            case "4" -> viewCropStatus();
            default -> System.out.println("Invalid choice. Try again.");
        }
    }

    static void plantCrops() {
        System.out.println("Available crops:");
        crops.forEach((key, crop) -> System.out.println(key + ". " + crop.name + " - Cost: $" + crop.cost));

        System.out.print("Choose a crop to plant: ");
        String cropChoice = scanner.nextLine();
        Crop selectedCrop = crops.get(cropChoice);

        if (selectedCrop == null) {
            System.out.println("Invalid crop choice.");
            return;
        }

        if (money < selectedCrop.cost) {
            System.out.println("Not enough money to plant this crop.");
        } else if (selectedCrop.daysToHarvest > 0) {
            System.out.println(selectedCrop.name + " is already planted.");
        } else {
            selectedCrop.daysToHarvest = selectedCrop.growthTime;
            money -= selectedCrop.cost;
            System.out.println("You planted " + selectedCrop.name + ".");
        }
    }

    static void waterCrops() {
        boolean watered = false;
        for (Crop crop : crops.values()) {
            if (crop.daysToHarvest > 0) {
                crop.daysToHarvest--;
                watered = true;
            }
        }
        if (watered) {
            System.out.println("You watered your crops. They are closer to harvest!");
        } else {
            System.out.println("No crops need watering.");
        }
    }

    static void harvestCrops() {
        int earnings = 0;
        for (Crop crop : crops.values()) {
            if (crop.daysToHarvest == 0) {
                earnings += crop.sellPrice;
                crop.daysToHarvest = -1; 
                System.out.println("You harvested " + crop.name + " and earned $" + crop.sellPrice + ".");
            }
        }

        if (earnings > 0) {
            money += earnings;
        } else {
            System.out.println("No crops are ready for harvest.");
        }
    }

    static void viewCropStatus() {
        System.out.println("Crop Status:");
        for (Crop crop : crops.values()) {
            if (crop.daysToHarvest > 0) {
                System.out.println(crop.name + " - " + crop.daysToHarvest + " days left to harvest.");
            } else if (crop.daysToHarvest == 0) {
                System.out.println(crop.name + " is ready to harvest!");
            } else {
                System.out.println(crop.name + " is not planted.");
            }
        }
    }
}

class Crop {
    String name;
    int cost;
    int sellPrice;
    int growthTime;
    int daysToHarvest = -1; 

    Crop(String name, int cost, int sellPrice, int growthTime) {
        this.name = name;
        this.cost = cost;
        this.sellPrice = sellPrice;
        this.growthTime = growthTime;
    }
}
