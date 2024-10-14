import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FarmGame extends JFrame implements ActionListener {
    private JTextArea gameArea;
    private JTextField inputField;
    private JButton submitButton;
    private String currentAction;
    private Map<String, Crop> crops;
    private int money;
    private int cropsGrown;
    private int growthTime; 
    private boolean isGrowing; 
    private Timer growthTimer; 

    public FarmGame() {
        // Initialize game components
        setTitle("Farming Game");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gameArea = new JTextArea();
        gameArea.setEditable(false);
        add(new JScrollPane(gameArea), BorderLayout.CENTER);

        inputField = new JTextField();
        submitButton = new JButton("Submit");
        submitButton.addActionListener(this);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(submitButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Start the game
        initializeGame();
    }

    private void initializeGame() {
        cropsGrown = 0;
        currentAction = "";
        growthTime = 5; // Time for crops to grow (in seconds)
        isGrowing = false;
        money = 1000; 
        crops = new HashMap<>();

        // Define crops with their costs and earnings
        crops.put("wheat", new Crop("Wheat", 10, 15)); // Name, Cost, Earnings
        crops.put("corn", new Crop("Corn", 15, 25));
        crops.put("carrot", new Crop("Carrot", 12, 18));
        
        gameArea.setText("Welcome to the Farming Game!\n\n" +
                         "You have $" + money + " available.\n" +
                         "Available actions:\n" +
                         "- plant [crop_name]\n" +
                         "- water\n" +
                         "- harvest\n" +
                         "- check\n" +
                         "Type your action and press Enter.\n");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String input = inputField.getText().toLowerCase().trim();
        inputField.setText("");

        if (input.startsWith("plant ")) {
            String cropName = input.substring(6); // Extract crop name
            plantCrops(cropName);
        } else {
            switch (input) {
                case "water":
                    waterCrops();
                    break;
                case "harvest":
                    harvestCrops();
                    break;
                case "check":
                    checkCrops();
                    break;
                default:
                    gameArea.append("Unknown action. Please try again.\n");
                    break;
            }
        }
    }

    private void plantCrops(String cropName) {
        Crop crop = crops.get(cropName);
        if (crop != null) {
            if (!isGrowing && money >= crop.cost) {
                cropsGrown++;
                money -= crop.cost;
                isGrowing = true;
                gameArea.append("You planted " + crop.name + "!\n");
                startGrowthTimer();
            } else if (isGrowing) {
                gameArea.append("You already have crops growing. Please wait to harvest.\n");
            } else {
                gameArea.append("Not enough money to plant " + crop.name + ".\n");
            }
        } else {
            gameArea.append("Crop not found. Available crops: " + crops.keySet() + "\n");
        }
    }

    private void waterCrops() {
        if (cropsGrown > 0) {
            gameArea.append("You watered the crops.\n");
        } else {
            gameArea.append("You have no crops to water. Please plant some first.\n");
        }
    }

    private void harvestCrops() {
        if (cropsGrown > 0 && growthTime <= 0) {
            Crop crop = crops.values().iterator().next(); // Get the first crop (for simplicity)
            money += crop.earnings * cropsGrown; // Calculate earnings
            gameArea.append("You harvested " + cropsGrown + " " + crop.name + "(s)! You earned $" + (crop.earnings * cropsGrown) + ".\n");
            cropsGrown = 0; // Reset crops after harvesting
            isGrowing = false; // Reset growing state
            growthTime = 5; // Reset growth time for new crops
        } else if (cropsGrown > 0) {
            gameArea.append("Your crops are not ready for harvest yet. Please wait.\n");
        } else {
            gameArea.append("You have no crops to harvest. Please plant some first.\n");
        }
    }

    private void checkCrops() {
        if (isGrowing) {
            gameArea.append("You have " + cropsGrown + " crops growing. They will be ready in " + growthTime + " seconds.\n");
        } else {
            gameArea.append("You have no crops growing. Please plant some!\n");
        }
        gameArea.append("You have $" + money + " available.\n");
    }

    private void startGrowthTimer() {
        growthTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (growthTime > 0) {
                    growthTime--;
                } else {
                    growthTimer.stop();
                    gameArea.append("Your crops are ready for harvest!\n");
                }
            }
        });
        growthTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FarmGame game = new FarmGame();
            game.setVisible(true);
        });
    }

    // Crop class to hold crop properties
    class Crop {
        String name;
        int cost;
        int earnings;

        Crop(String name, int cost, int earnings) {
            this.name = name;
            this.cost = cost;
            this.earnings = earnings;
        }
    }
}
