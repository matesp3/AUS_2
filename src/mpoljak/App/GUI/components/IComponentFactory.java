package mpoljak.App.GUI.components;

import mpoljak.dataStructures.IParams;

public interface IComponentFactory {
    public GuiForm createAddForm();
    public GuiForm createEditForm(IParams dataParams);
//    public AbstractTableModel createTableModel(List<IParams> lDataParams);
}
