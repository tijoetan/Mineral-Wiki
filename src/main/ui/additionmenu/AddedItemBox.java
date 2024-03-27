package ui.additionmenu;

import utils.StringUtils;
import utils.fieldnames.Constants;
import utils.fieldnames.PropertyNames;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class AddedItemBox extends JPanel {
    private JButton deleteButton;
    private JLabel itemName;
    private String name;

    public AddedItemBox(String name) {
        this.name = name;
        itemName = new JLabel("<html>" + StringUtils.wrapString(
                StringUtils.trimTo(name, 16),
                10, Constants.WRAP_FOR_GUI) + "</html>");
        System.out.println(itemName.getPreferredSize().height);
        deleteButton = new JButton("x");
        deleteButton.addActionListener(e -> firePropertyChange(PropertyNames.DESCENDANT_DELETED,
                true,
                false));
        deleteButton.setBackground(Color.LIGHT_GRAY);
        add(itemName);
        add(deleteButton);
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, Color.BLACK));
        setBackground(Color.GRAY);
        setPreferredSize(new Dimension(120, 50));

    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof AddedItemBox) {
            AddedItemBox otherBox = (AddedItemBox) other;
            return otherBox.getName().equals(name);
        }
        return false;
    }

}
