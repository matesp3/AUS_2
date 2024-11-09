package mpoljak.App.GUI.components;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OperationsAreaComponent extends JPanel {
    private ArrayList<JButton> lButtons;

    private boolean disabledState; // when some operation has been chosen

    public OperationsAreaComponent(int prefWidth, int prefHeight, Color background, Color btnBackground) {
        int btnWidth = 98;
        int btnHeight = 25;

        this.disabledState = false;
        this.lButtons = new ArrayList<>(6);

//        JPanel btnPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);
        this.setBackground(background);
        this.setPreferredSize(new Dimension(prefWidth, prefHeight));
        this.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1, true));
//        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createCompoundBorder(), "Operations"));

        Insets insets = new Insets(10, 0, 0, 0);
        GridBagConstraints con = new GridBagConstraints();
        con.weightx = 0.25;
        con.weighty = 0.5;

        con.gridx = 0;
        con.gridy = 0;
        con.anchor = GridBagConstraints.WEST;
        JLabel labelTitle = new JLabel("CHOOSE OPERATION:");
        this.add(labelTitle, con);

        con.anchor = GridBagConstraints.BASELINE;
        con.gridx = 0;
        con.gridy = 1;
        JButton insertBtn = this.createButton(btnWidth, btnHeight, "Add data", btnBackground);
        insertBtn.addActionListener(e -> {
            this.btnExecution("INSERT", insertBtn);
        });
        this.add(insertBtn, con);
        this.lButtons.add(insertBtn);

        con.gridx = 1;
        con.gridy = 1;
        JButton searchBtn = this.createButton(btnWidth, btnHeight, "Find data", btnBackground);
        searchBtn.addActionListener(e -> {
            this.btnExecution("SEARCH", searchBtn);
        });
        this.add(searchBtn, con);
        this.lButtons.add(searchBtn);

        con.gridx = 0;
        con.gridy = 2;
        JButton editBtn = this.createButton(btnWidth, btnHeight, "Edit data", btnBackground);
        editBtn.addActionListener(e -> {
            this.btnExecution("EDIT", editBtn);
        });
        this.add(editBtn, con);
        this.lButtons.add(editBtn);

        con.gridx = 1;
        con.gridy = 2;
        JButton deleteBtn = this.createButton(btnWidth, btnHeight, "Delete data", btnBackground);
        deleteBtn.addActionListener(e -> {
            this.btnExecution("DELETE", deleteBtn);
        });
        this.add(deleteBtn, con);
        this.lButtons.add(deleteBtn);

        con.gridx = 0;
        con.gridy = 3;
        JButton generateBtn = this.createButton(-1, -1, "Generate data", btnBackground);
        generateBtn.addActionListener(e -> {
            this.btnExecution("GENERATE", generateBtn);
        });
        this.add(generateBtn, con);
        this.lButtons.add(generateBtn);

        con.gridx = 1;
        con.gridy = 3;
        JButton printBtn = this.createButton(-1, -1, "Get all data", btnBackground);
        printBtn.addActionListener(e -> {
            this.btnExecution("PRINT", printBtn);
        });
        this.add(printBtn, con);
        this.lButtons.add(printBtn);
    }

    private JButton createButton(int width, int height, String text, Color background) {
        JButton button = new JButton();
        button.setText(text);
        if (height > 0 && width > 0)
            button.setPreferredSize(new Dimension(width, height));
        button.setBackground(background);
        return button;
    }

    private void btnExecution(String action, JButton clickedBtn) {
        if (!this.disabledState) {
            this.disabledState = true;
            System.out.println(action);
            this.disableOtherButtons(clickedBtn);
        } else {
            this.disabledState = false;
            this.enableAllButtons();
        }
    }

    private void enableAllButtons() {
        for (JButton btn : lButtons)
            btn.setEnabled(true);
    }

    private void disableOtherButtons(JButton clickedBtn) {
        for (JButton btn : lButtons) {
            btn.setEnabled(btn == clickedBtn);
        }
    }
}
