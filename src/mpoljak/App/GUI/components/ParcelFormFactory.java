package mpoljak.App.GUI.components;

import mpoljak.data.geo.ParcelParams;
import mpoljak.dataStructures.IParams;

public class ParcelFormFactory implements IFormFactory {
    private static final int FORM_WIDTH = 400;
    private static final int FORM_HEIGHT = 440;
    @Override
    public GuiForm createAddForm() {
        return new ParcelForm(null, FORM_WIDTH, FORM_HEIGHT, GuiForm.DEFAULT_BG, true);
    }

    @Override
    public GuiForm createEditForm(IParams dataParams) {
        if (dataParams == null)
            return null;
        ParcelParams parcelParams = (ParcelParams) dataParams;
        return new ParcelForm(parcelParams, FORM_WIDTH, FORM_HEIGHT, GuiForm.DEFAULT_BG, false);
    }
}
