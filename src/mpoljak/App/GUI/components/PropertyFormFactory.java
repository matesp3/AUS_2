package mpoljak.App.GUI.components;

import mpoljak.data.geo.PropertyParams;
import mpoljak.dataStructures.IParams;

public class PropertyFormFactory implements IFormFactory {
    private static final int FORM_WIDTH = 400;
    private static final int FORM_HEIGHT = 570;

    @Override
    public GuiForm createAddForm() {
        return new PropertyForm(null, FORM_WIDTH, FORM_HEIGHT, GuiForm.DEFAULT_BG, true);
    }

    @Override
    public GuiForm createEditForm(IParams dataParams) {
        if (dataParams == null)
            return null;
        PropertyParams propParams = (PropertyParams) dataParams;
        return new PropertyForm(propParams, FORM_WIDTH, FORM_HEIGHT, GuiForm.DEFAULT_BG, false);
    }
}
