package ui.misc;

import model.entries.WikiEntry;
import ui.additionmenu.familyaddition.AddedItemBox;
import ui.additionmenu.familyaddition.AdditionPanel;
import ui.displaypage.EntryHyperLink;
import utils.fieldnames.PropertyNames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

// Menu that displays updatable Grid of Descendants

public class DescendantMenu extends JPanel {
    private final JPanel addedItemFrame;
    private final List<JComponent> addedItems;
    private final AdditionPanel additionPanel;
    private GridBagConstraints constraints;
    JScrollPane pane;
    private final int rowCount;


    private int defaultConstraints;

    // Constructor for Descendant Panel used in FamilyAdditionPanel
    public DescendantMenu(int rowCount) {
        defaultConstraints = GridBagConstraints.NONE;
        this.rowCount = rowCount;
        addedItems = new ArrayList<>();
        setLayout(new BorderLayout());
        GridBagLayout layout = new GridBagLayout();
        setupConstraints();
        layout.setConstraints(this, constraints);

        addedItemFrame = new JPanel(layout);
        additionPanel = new AdditionPanel();
        additionPanel.addButtonActionListener(e -> addDescendant(additionPanel.getText()));


        pane = new JScrollPane(addedItemFrame);
        pane.setPreferredSize(new Dimension(240, 100));
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(additionPanel, BorderLayout.NORTH);
        add(pane, BorderLayout.CENTER);

    }

    // Constructor used for FamilyDisplayPanel
    public DescendantMenu(int rowCount, List<WikiEntry> entries) {
        this(rowCount);
        remove(additionPanel);
        defaultConstraints = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        pane.setPreferredSize(new Dimension(300, 400));
        for (WikiEntry entry : entries) {
            addDescendant(entry);
        }
    }

    //getters
    public List<JComponent> getAddedItems() {
        return new ArrayList<>(addedItems);
    }

    // MODIFIES: this
    // EFFECTS: creates and initializes constarings
    private void setupConstraints() {
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
//        constraints.weighty = 1;

    }

    // MODIFIES: this
    // EFFECTS: adds AddedItemBox to view and addedItems
    public void addDescendant(String name) {
        AddedItemBox newBox = new AddedItemBox(name);
        if (addedItems.contains(newBox)) {
            UserQuery.showErrorMessage("Already Added");
            return;
        }

        addedItems.add(newBox);
        newBox.addPropertyChangeListener(PropertyNames.DESCENDANT_DELETED, e -> deleteItem(e.getSource()));
        additionPanel.clearText();
        updateComponents();
    }

    // MODIFIES: this
    // EFFECTS: adds EntryHyperLink to view and addedItems
    public void addDescendant(WikiEntry entry) {
        EntryHyperLink link = new EntryHyperLink(entry);
        if (!addedItems.contains(link)) {
            addedItems.add(link);
            updateComponents();
        }
    }

    // MODIFIES: this
    // EFFECTS: removes given AddedItemBox from view and addedItems
    private void deleteItem(Object source) {
        AddedItemBox addBox = (AddedItemBox) source;
        addedItems.remove(addBox);
        updateComponents();

    }

    // MODIFIES: this
    // EFFECTS: redraws view based on addedItems
    public void updateComponents() {
        constraints.fill = defaultConstraints;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 0;
        addedItemFrame.removeAll();

        for (JComponent box : addedItems) {
            addedItemFrame.add(box, constraints);
            constraints.gridx = (constraints.gridx + 1) % rowCount;
            constraints.gridy = constraints.gridx == 0 ? constraints.gridy + 1 : constraints.gridy;
        }

        constraints.gridx = rowCount - 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        addedItemFrame.add(new JLabel(""), constraints);

        revalidate();
        repaint();
    }

}
