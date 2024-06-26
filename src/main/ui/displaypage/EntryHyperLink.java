package ui.displaypage;

import model.entries.WikiEntry;
import ui.clickeditemhandler.ClickedItemHandler;
import utils.fieldnames.PropertyNames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

// Descendant label in FamilyDisplayPage

public class EntryHyperLink extends JLabel {
    private final WikiEntry entry;

    // EFFECTS: constructs EntryHyperLink for entry
    public EntryHyperLink(WikiEntry entry) {
        super();
        this.entry = entry;
        setName(entry.getName());
        setupText();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickActivity();
            }
        });
    }

    // getters
    public WikiEntry getEntry() {
        return entry;
    }

    // MODIFIES: ClickedItemHandler
    // EFFECTS: sets clickedItem to entry
    private void clickActivity() {
        ClickedItemHandler.getInstance().setClickedItem(entry);
        firePropertyChange(PropertyNames.ITEM_CLICKED, true, false);
    }

    // MODIFIES: this
    // EFEFCTS: creates hyperlink label
    private void setupText() {
        setText("<html><font color='blue'><u>" + entry.getName() + "</u></font></html>");
        setFont(new Font("Inter", Font.BOLD | Font.ITALIC, 30));
        setPreferredSize(new Dimension(getPreferredSize().width + 20,getPreferredSize().height + 20));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
}
