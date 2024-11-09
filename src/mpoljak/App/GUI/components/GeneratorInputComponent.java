package mpoljak.App.GUI.components;

import mpoljak.App.GUI.models.GeneratorModel;

import javax.swing.*;
import java.awt.*;

public class GeneratorInputComponent extends JPanel {
    private JTextField parcelNrInput;
    private JTextField propertyNrInput;
    private JTextField probabilityInput;

    private boolean enabledState;

    public GeneratorInputComponent(int prefWidth, int prefHeight, Color bgColor) {
        this.setPreferredSize(new Dimension(prefWidth, prefHeight));
        this.setBackground(bgColor);
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        GridBagConstraints con = new GridBagConstraints();
        con.weightx = 0.5;
        con.weighty = 0.3;

        con.insets = new Insets(5,0, 3, 0);
        con.gridx = 0;
        con.gridy = 0;
        JLabel titleLabel = new JLabel("Data generator");
        this.add(titleLabel, con);

        con.insets = new Insets(5,5,0,0);
        con.anchor = GridBagConstraints.WEST;

        con.gridx = 0;
        con.gridy = 1;
        JLabel parcelLabel = new JLabel("Parcels count: ");
        this.add(parcelLabel, con);

        con.gridx = 1;
        con.gridy = 1;
        this.parcelNrInput = new JTextField();
        this.parcelNrInput.setPreferredSize(new Dimension(60, 22));
        this.add(parcelNrInput, con);

        con.gridx = 0;
        con.gridy = 2;
        JLabel propertyLabel = new JLabel("Properties count: ");
        this.add(propertyLabel, con);

        con.gridx = 1;
        con.gridy = 2;
        this.propertyNrInput = new JTextField();
        this.propertyNrInput.setPreferredSize(new Dimension(60, 22));
        this.add(propertyNrInput, con);

        con.gridx = 0;
        con.gridy = 3;
        JLabel probabilityLabel = new JLabel("Overlay probability: ");
        this.add(probabilityLabel, con);

        con.gridx = 1;
        con.gridy = 3;
        con.insets = new Insets(5,5,5,0);
        this.probabilityInput = new JTextField();
        this.probabilityInput.setPreferredSize(new Dimension(40, 22));
        this.add(probabilityInput, con);

        this.enabledState = true;
    }

    /**
     * Disables all input text fields and removes input data from them.
     */
    public void disableComponent() {
        if (!this.enabledState)
            return;
        this.parcelNrInput.setText(null);
        this.parcelNrInput.setEnabled(false);
        this.propertyNrInput.setText(null);
        this.propertyNrInput.setEnabled(false);
        this.probabilityInput.setText(null);
        this.probabilityInput.setEnabled(false);
        this.enabledState = false;
    }

    /**
     * Enables all input text fields.
     */
    public void enableComponent() {
        if (this.enabledState)
            return;
        this.parcelNrInput.setEnabled(true);
        this.propertyNrInput.setEnabled(true);
        this.probabilityInput.setEnabled(true);
        this.enabledState = true;
    }

    /**
     * Gets data, which input fields contain
     * @return model representation of visualized information
     */
    public GeneratorModel getModel() {
        return new GeneratorModel(Integer.parseInt(this.parcelNrInput.getText()),
                                  Integer.parseInt(this.propertyNrInput.getText()),
                                  Double.parseDouble(this.probabilityInput.getText())
                                );
    }

    /**
     * Fills input fields with data from model
     * @param model contains data to visualize
     */
    public void setModel(GeneratorModel model) {
        this.parcelNrInput.setText(String.valueOf(model.getParcelsCount()));
        this.propertyNrInput.setText(String.valueOf(model.getPropertiesCount()));
        this.probabilityInput.setText(String.valueOf(model.getOverlayProbability()));
    }
}
