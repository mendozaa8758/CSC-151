import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RavensRosterGUI extends JFrame {

    // Player class to store player information
    static class Player {
        private String name;
        private int number;
        private int weight;
        private String position;

        public Player(String name, int number, int weight, String position) {
            this.name = name;
            this.number = number;
            this.weight = weight;
            this.position = position;
        }

        public String getName() { return name; }
        public int getNumber() { return number; }
        public int getWeight() { return weight; }
        public String getPosition() { return position; }
    }

    private DefaultTableModel tableModel;
    private JTable playerTable;
    private Player[] players;

    // CSV file path for saving/loading roster
    private static final String CSV_FILE = "ravens_roster.csv";

    public RavensRosterGUI() {
        // Try to load from CSV; fall back to hard-coded data if file not found
        // Written by: [Your Name] – Loads players from CSV file or falls back to defaults
        Player[] loaded = loadPlayersFromCSV();
        if (loaded != null && loaded.length > 0) {
            players = loaded;
        } else {
            initializePlayers();
            // Save default data to CSV so future runs can load from file
            savePlayersToCSV();
        }

        setTitle("Baltimore Ravens - Next Game Roster");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel headerPanel = createHeaderPanel();
        JPanel tablePanel = createTablePanel();
        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadPlayerData();
        showWelcomeDialog();
    }

    // Written by: [Your Name] – This method writes player data to a CSV file
    private void savePlayersToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
            // Write header row
            writer.println("Name,Number,Weight,Position");
            for (Player p : players) {
                writer.printf("%s,%d,%d,%s%n",
                        p.getName(), p.getNumber(), p.getWeight(), p.getPosition());
            }
            System.out.println("Roster saved to " + CSV_FILE);
        } catch (IOException e) {
            // Note: Silently fails during init; could show dialog if needed later
            System.err.println("Error saving roster: " + e.getMessage());
        }
    }

    // Written by: [Your Name] – This method reads player data from the CSV file
    private Player[] loadPlayersFromCSV() {
        File file = new File(CSV_FILE);
        if (!file.exists()) {
            return null;
        }

        List<Player> playerList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0].trim();
                    int number = Integer.parseInt(parts[1].trim());
                    int weight = Integer.parseInt(parts[2].trim());
                    String position = parts[3].trim();
                    playerList.add(new Player(name, number, weight, position));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading roster: " + e.getMessage());
            return null;
        }

        return playerList.toArray(new Player[0]);
    }

    private void initializePlayers() {
        players = new Player[]{
            new Player("Lamar Jackson", 8, 212, "QB"),
            new Player("Derrick Henry", 22, 247, "RB"),
            new Player("Mark Andrews", 89, 256, "TE"),
            new Player("Zay Flowers", 4, 182, "WR"),
            new Player("Rashod Bateman", 7, 190, "WR"),
            new Player("Justice Hill", 43, 198, "RB"),
            new Player("Tyler Linderbaum", 64, 296, "C"),
            new Player("Roquan Smith", 0, 232, "LB"),
            new Player("Kyle Hamilton", 14, 220, "S"),
            new Player("Marlon Humphrey", 44, 197, "CB"),
            new Player("Justin Tucker", 9, 183, "K"),
            new Player("Patrick Ricard", 42, 300, "FB"),
            new Player("Isaiah Likely", 80, 245, "TE"),
            new Player("Ronnie Stanley", 79, 315, "OT"),
            new Player("Kyle Van Noy", 53, 250, "LB"),
        };
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(26, 25, 95));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel titleLabel = new JLabel("BALTIMORE RAVENS - NEXT GAME ROSTER", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Player Information Display", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.WHITE);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(new Color(26, 25, 95));
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        headerPanel.add(textPanel, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());

        String[] columnNames = {"Number", "Player Name", "Position", "Weight (lbs)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        playerTable = new JTable(tableModel);
        playerTable.setFont(new Font("Arial", Font.PLAIN, 14));
        playerTable.setRowHeight(25);
        playerTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        playerTable.getTableHeader().setBackground(new Color(26, 25, 95));
        playerTable.getTableHeader().setForeground(Color.WHITE);

        playerTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        playerTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        playerTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        playerTable.getColumnModel().getColumn(3).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(playerTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(26, 25, 95), 2));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        JButton viewDetailsButton = new JButton("View Player Details");
        styleButton(viewDetailsButton, new Color(26, 25, 95));
        viewDetailsButton.addActionListener(e -> viewPlayerDetails());

        JButton searchButton = new JButton("Search Player");
        styleButton(searchButton, new Color(26, 25, 95));
        searchButton.addActionListener(e -> searchPlayer());

        JButton statsButton = new JButton("Roster Statistics");
        styleButton(statsButton, new Color(26, 25, 95));
        statsButton.addActionListener(e -> showStatistics());

        // Save to CSV button
        // Written by: [Your Name] – Button to trigger CSV export
        JButton saveButton = new JButton("Save to CSV");
        styleButton(saveButton, new Color(0, 120, 0));
        saveButton.addActionListener(e -> {
            savePlayersToCSV();
            JOptionPane.showMessageDialog(this,
                    "Roster saved to " + CSV_FILE,
                    "Save Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        JButton exitButton = new JButton("Exit");
        styleButton(exitButton, new Color(200, 50, 50));
        exitButton.addActionListener(e -> exitApplication());

        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(statsButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(exitButton);

        return buttonPanel;
    }

    // Helper method to reduce repeated button styling code
    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private void loadPlayerData() {
        tableModel.setRowCount(0);
        for (Player player : players) {
            Object[] rowData = {
                "#" + player.getNumber(),
                player.getName(),
                player.getPosition(),
                player.getWeight()
            };
            tableModel.addRow(rowData);
        }
    }

    private void showWelcomeDialog() {
        JOptionPane.showMessageDialog(this,
                "Welcome to the Baltimore Ravens Roster System!\n\n" +
                "This application displays the players for the next game.\n" +
                "Total Players: " + players.length,
                "Welcome",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewPlayerDetails() {
        int selectedRow = playerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a player from the table first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Player selectedPlayer = players[selectedRow];
        String details = String.format(
                "Player Details:\n\nName: %s\nNumber: #%d\nPosition: %s\nWeight: %d lbs",
                selectedPlayer.getName(), selectedPlayer.getNumber(),
                selectedPlayer.getPosition(), selectedPlayer.getWeight());

        JOptionPane.showMessageDialog(this, details,
                "Player Details - " + selectedPlayer.getName(),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchPlayer() {
        String searchName = JOptionPane.showInputDialog(this,
                "Enter player name to search:", "Search Player",
                JOptionPane.QUESTION_MESSAGE);

        if (searchName == null || searchName.trim().isEmpty()) return;

        boolean found = false;
        for (int i = 0; i < players.length; i++) {
            if (players[i].getName().toLowerCase().contains(searchName.toLowerCase())) {
                playerTable.setRowSelectionInterval(i, i);
                playerTable.scrollRectToVisible(playerTable.getCellRect(i, 0, true));

                String details = String.format(
                        "Player Found!\n\nName: %s\nNumber: #%d\nPosition: %s\nWeight: %d lbs",
                        players[i].getName(), players[i].getNumber(),
                        players[i].getPosition(), players[i].getWeight());

                JOptionPane.showMessageDialog(this, details, "Search Result",
                        JOptionPane.INFORMATION_MESSAGE);
                found = true;
                break;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(this,
                    "No player found with the name: " + searchName,
                    "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showStatistics() {
        int totalPlayers = players.length;
        double totalWeight = 0;
        int maxWeight = 0;
        int minWeight = Integer.MAX_VALUE;
        String heaviestPlayer = "";
        String lightestPlayer = "";

        for (Player player : players) {
            int weight = player.getWeight();
            totalWeight += weight;
            if (weight > maxWeight) { maxWeight = weight; heaviestPlayer = player.getName(); }
            if (weight < minWeight) { minWeight = weight; lightestPlayer = player.getName(); }
        }

        double averageWeight = totalWeight / totalPlayers;
        String stats = String.format(
                "Roster Statistics:\n\nTotal Players: %d\nAverage Weight: %.1f lbs\n\n" +
                "Heaviest Player:\n%s - %d lbs\n\nLightest Player:\n%s - %d lbs",
                totalPlayers, averageWeight, heaviestPlayer, maxWeight, lightestPlayer, minWeight);

        JOptionPane.showMessageDialog(this, stats, "Roster Statistics",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void exitApplication() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit?", "Exit Confirmation",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                    "Thank you for using the Ravens Roster System!\nGo Ravens!",
                    "Goodbye", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            RavensRosterGUI frame = new RavensRosterGUI();
            frame.setVisible(true);
        });
    }
}
