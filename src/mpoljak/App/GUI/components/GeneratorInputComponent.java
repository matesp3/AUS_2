package mpoljak.App.GUI.components;

import mpoljak.App.GUI.models.GeneratorModel;

import javax.swing.*;
import java.awt.*;

public class GeneratorInputComponent extends JPanel {
    private JTextField parcelNrInput;
    private JTextField propertyNrInput;
    private JTextField probabilityInput;

    private JLabel titleLabel;
    private JLabel parcelLabel;
    private JLabel propertyLabel;
    private JLabel probabilityLabel;

    private boolean enabledState;

    public GeneratorInputComponent(int prefWidth, int prefHeight, Color bgColor) {
        this.setPreferredSize(new Dimension(prefWidth, prefHeight));
        this.setBackground(bgColor);
        this.setBorder(BorderFactory.createEtchedBorder());
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        GridBagConstraints con = new GridBagConstraints();
        con.weightx = 0.5;
        con.weighty = 0.3;

        con.insets = new Insets(5,0, 7, 0);
        con.gridwidth = 2;
        con.gridx = 0;
        con.gridy = 0;
        this.titleLabel = new JLabel("Data generator parameters:");
        this.add(titleLabel, con);

        con.insets = new Insets(5,5,0,0);
        con.gridwidth = 1;
        con.anchor = GridBagConstraints.WEST;

        con.gridx = 0;
        con.gridy = 1;
        this.parcelLabel = new JLabel("Parcels count: ");
        this.add(parcelLabel, con);

        con.gridx = 1;
        con.gridy = 1;
        this.parcelNrInput = new JTextField();
        this.parcelNrInput.setPreferredSize(new Dimension(60, 22));
        this.add(parcelNrInput, con);

        con.gridx = 0;
        con.gridy = 2;
        this.propertyLabel = new JLabel("Properties count: ");
        this.add(propertyLabel, con);

        con.gridx = 1;
        con.gridy = 2;
        this.propertyNrInput = new JTextField();
        this.propertyNrInput.setPreferredSize(new Dimension(60, 22));
        this.add(propertyNrInput, con);

        con.gridx = 0;
        con.gridy = 3;
        this.probabilityLabel = new JLabel("Overlay probability: ");
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
     * Enables/disables component's children
     * @param enabled if component's children should be enabled or not
     */
    public void setComponentEnable(boolean enabled) {
        if (this.enabledState == enabled)
            return;
        this.titleLabel.setEnabled(enabled);
        this.parcelLabel.setEnabled(enabled);
        this.parcelNrInput.setEnabled(enabled);
        this.propertyLabel.setEnabled(enabled);
        this.propertyNrInput.setEnabled(enabled);
        this.probabilityLabel.setEnabled(enabled);
        this.probabilityInput.setEnabled(enabled);
        this.enabledState = enabled;
        if (!enabled) {
            this.parcelNrInput.setText(null);
            this.propertyNrInput.setText(null);
            this.probabilityInput.setText(null);
        }
    }

    /**
     * Gets data, which input fields contain
     * @return model representation of visualized information
     */
    public GeneratorModel getModel() {
        try {
            return new GeneratorModel(Integer.parseInt(this.parcelNrInput.getText()),
                    Integer.parseInt(this.propertyNrInput.getText()),
                    Double.parseDouble(this.probabilityInput.getText())
            );
        } catch (Exception e) {
            return null;
        }
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
