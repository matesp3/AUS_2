package mpoljak.App.GUI.components;

import mpoljak.dataStructures.IParams;

/**
 * Abstract factory.
 */
public interface IFormFactory {

    /**
     * Form with specific fields of specific overriding data family.
     * @return
     */
    public GuiForm createAddForm();

    /**
     * Creates form which fills up with <code>dataParams</code> attributes instance.
     * @param dataParams values provided to display in form.
     * @return instance of specific form or <code>null</code> if params not provided.
     */
    public GuiForm createEditForm(IParams dataParams);
}
