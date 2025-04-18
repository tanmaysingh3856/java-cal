import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Calculator implements ActionListener {
    Boolean isOperatorClicked = false;
    JFrame jf;
    JLabel displayLabel;
    JTextArea historyArea;
    JScrollPane historyScroll;
    StringBuilder history;
    JPanel buttonPanel;
    private String currentExpression = "";
    private boolean startNewNumber = false;
    
    // Calculator buttons
    JButton[] numberButtons;
    JButton plusButton, minButton, mulButton, divButton;
    JButton dotButton, equalButton, clearButton, deleteButton;
    
    double newValue;
    double oldValue;
    int calculation;

    public Calculator() {
        jf = new JFrame("Modern Calculator");
        jf.setSize(400, 600);
        jf.setLayout(new BorderLayout(10, 10));
        jf.getContentPane().setBackground(new Color(240, 240, 240));
        jf.getRootPane().setBorder(new EmptyBorder(10, 10, 10, 10));

        // Display setup
        displayLabel = new JLabel("0");
        displayLabel.setPreferredSize(new Dimension(380, 60));
        displayLabel.setBackground(Color.WHITE);
        displayLabel.setOpaque(true);
        displayLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        displayLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        displayLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 15)));

        // History setup
        history = new StringBuilder();
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        historyArea.setBackground(new Color(250, 250, 250));
        historyScroll = new JScrollPane(historyArea);
        historyScroll.setPreferredSize(new Dimension(380, 100));

        // Button panel setup
        buttonPanel = new JPanel(new GridLayout(5, 4, 5, 5));
        buttonPanel.setBackground(new Color(240, 240, 240));

        // Initialize number buttons
        numberButtons = new JButton[10];
        for (int i = 0; i < 10; i++) {
            numberButtons[i] = createButton(String.valueOf(i), new Color(252, 252, 252));
        }

        // Initialize operator buttons
        deleteButton = createButton("⌫", new Color(255, 200, 200));
        clearButton = createButton("C", new Color(255, 200, 200));
        plusButton = createButton("+", new Color(230, 230, 250));
        minButton = createButton("-", new Color(230, 230, 250));
        mulButton = createButton("×", new Color(230, 230, 250));
        divButton = createButton("÷", new Color(230, 230, 250));
        dotButton = createButton(".", new Color(252, 252, 252));
        equalButton = createButton("=", new Color(200, 230, 255));

        // Add buttons to panel
        buttonPanel.add(clearButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(divButton);
        buttonPanel.add(mulButton);

        buttonPanel.add(numberButtons[7]);
        buttonPanel.add(numberButtons[8]);
        buttonPanel.add(numberButtons[9]);
        buttonPanel.add(minButton);

        buttonPanel.add(numberButtons[4]);
        buttonPanel.add(numberButtons[5]);
        buttonPanel.add(numberButtons[6]);
        buttonPanel.add(plusButton);

        buttonPanel.add(numberButtons[1]);
        buttonPanel.add(numberButtons[2]);
        buttonPanel.add(numberButtons[3]);
        buttonPanel.add(equalButton);

        buttonPanel.add(numberButtons[0]);
        buttonPanel.add(dotButton);
        buttonPanel.add(new JButton(""));
        buttonPanel.add(new JButton(""));

        // Add components to frame
        JPanel topPanel = new JPanel(new BorderLayout(0, 5));
        topPanel.setBackground(new Color(240, 240, 240));
        topPanel.add(displayLabel, BorderLayout.NORTH);
        topPanel.add(historyScroll, BorderLayout.CENTER);

        jf.add(topPanel, BorderLayout.NORTH);
        jf.add(buttonPanel, BorderLayout.CENTER);

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }

    private JButton createButton(String label, Color bgColor) {
        JButton button = new JButton(label);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        button.addActionListener(this);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        String buttonText = clickedButton.getText();

        // Number buttons
        if (Character.isDigit(buttonText.charAt(0))) {
            if (startNewNumber) {
                currentExpression = currentExpression + buttonText;
                startNewNumber = false;
            } else {
                if (currentExpression.isEmpty() && displayLabel.getText().equals("0")) {
                    currentExpression = buttonText;
                } else if (currentExpression.isEmpty()) {
                    currentExpression = displayLabel.getText() + buttonText;
                } else {
                    currentExpression = currentExpression + buttonText;
                }
            }
            displayLabel.setText(currentExpression);
        }
        // Operator buttons
        else if (buttonText.matches("[+\\-×÷]")) {
            isOperatorClicked = true;
            startNewNumber = true;
            oldValue = Double.parseDouble(displayLabel.getText().split("[+\\-×÷]")[0]);
            if (!currentExpression.contains(buttonText)) {
                currentExpression = currentExpression + " " + buttonText + " ";
                displayLabel.setText(currentExpression);
            }
            switch (buttonText) {
                case "+": calculation = 1; break;
                case "-": calculation = 2; break;
                case "×": calculation = 3; break;
                case "÷": calculation = 4; break;
            }
        }
        // Special buttons
        else {
            switch (buttonText) {
                case ".":
                    String[] parts = currentExpression.split("[+\\-×÷]");
                    String lastNumber = parts[parts.length - 1].trim();
                    if (!lastNumber.contains(".")) {
                        currentExpression = currentExpression + ".";
                        displayLabel.setText(currentExpression);
                    }
                    break;
                case "C":
                    displayLabel.setText("0");
                    currentExpression = "";
                    history.setLength(0);
                    historyArea.setText("");
                    break;
                case "⌫":
                    if (!currentExpression.isEmpty()) {
                        if (currentExpression.endsWith(" ")) {
                            currentExpression = currentExpression.substring(0, currentExpression.length() - 3);
                        } else {
                            currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
                        }
                        if (currentExpression.isEmpty()) {
                            currentExpression = "0";
                        }
                        displayLabel.setText(currentExpression);
                    }
                    break;
                case "=":
                    if (!currentExpression.isEmpty()) {
                        calculateResult();
                        currentExpression = displayLabel.getText();
                    }
                    break;
            }
        }
    }

    private void calculateResult() {
        try {
            String[] parts = currentExpression.split(" ");
            if (parts.length >= 3) {
                double firstNumber = Double.parseDouble(parts[0]);
                String operator = parts[1];
                double secondNumber = Double.parseDouble(parts[2]);

                switch (operator) {
                    case "+": newValue = firstNumber + secondNumber; break;
                    case "-": newValue = firstNumber - secondNumber; break;
                    case "×": newValue = firstNumber * secondNumber; break;
                    case "÷":
                        if (secondNumber != 0) {
                            newValue = firstNumber / secondNumber;
                        } else {
                            displayLabel.setText("Error");
                            currentExpression = "";
                            return;
                        }
                        break;
                }

                String result = formatResult(newValue);
                displayLabel.setText(result);

                // Add to history
                String calculation = currentExpression + " = " + result + "\n";
                history.insert(0, calculation);
                historyArea.setText(history.toString());
            }
        } catch (Exception e) {
            displayLabel.setText("Error");
            currentExpression = "";
        }
    }

    private String formatResult(double number) {
        String result = String.valueOf(number);
        return result.endsWith(".0") ? result.substring(0, result.length() - 2) : result;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new Calculator());
    }
}