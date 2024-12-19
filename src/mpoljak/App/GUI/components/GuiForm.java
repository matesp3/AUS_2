package mpoljak.App.GUI.components;

import mpoljak.App.GUI.controllers.IController;
import mpoljak.dataStructures.IParams;
import mpoljak.dataStructures.IPredicate;

import javax.swing.*;
import java.awt.*;

public abstract class GuiForm extends JFrame {
    protected static final Color DEFAULT_BG = new Color(189, 243, 224);
    private final IParams originalParams;
    private final boolean isEditable;
    private boolean isDeclined;
    public boolean isSubmitted;

    public GuiForm(boolean isEditable, IParams originalParams) {
        this.isEditable = isEditable;
        this.isDeclined = false;
        this.isSubmitted = false;
        if (this.isEditable)
            this.originalParams = originalParams.cloneInstance(); // used prototype to disable modifying original params
        else
            this.originalParams = null;
    }
    /**
     * Template method.
     * @param controller instance that modifies application database.
     * @return successfulness
     */
    public boolean processForm(IController controller) {
        if (controller == null)
            return false;
        if (this.isDeclined) // nothing to do
            return true;
        if (this.isEditable) {
            return controller.updateInDb(this.originalParams, this.getUserInputs());
        }
        else
            return controller.addToDb(this.getUserInputs());
    }

    protected abstract IParams getUserInputs();

    protected void setDeclinedState() {
        this.isDeclined = true;
    }

    protected void setSubmittedState() {
        this.isSubmitted = true;
    }

    protected boolean isDeclined() {
        return this.isDeclined;
    }

    protected boolean isSubmitted() {
        return this.isSubmitted;
    }
}
