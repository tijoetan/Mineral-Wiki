package ui.displaypage;

import model.entries.Mineral;
import ui.misc.Images;
import utils.StringUtils;
import utils.fieldnames.AttributeNames;

import javax.swing.*;
import java.awt.*;

// Display page for minerals

public class MineralDisplayPage extends DisplayPage {

    private final Mineral mineral;
    private JTable stats;
    private JPanel enumPanel;

    // EFFECTS: constructs display page for given mineral
    public MineralDisplayPage(Mineral mineral) {
        super(mineral);
        this.mineral = mineral;
        setupSidePanel();
    }

    //getters
    public Mineral getMineral() {
        return this.mineral;
    }

    // MODIFIES: this
    // EFFECTS: sets up panel with quantitative table, formula and enumeration panel
    @Override
    public void setupSidePanel() {

        setupFormulaField();

        enumPanel = new JPanel(new BorderLayout());

        JPanel cleavagePanel = setupCleavagePanel();
        JPanel crystalPanel = setupCrystalPanel();


        enumPanel.add(cleavagePanel, BorderLayout.WEST);
        enumPanel.add(crystalPanel, BorderLayout.EAST);


        setupStats();

        placeItemsOnSidePanel();
    }

    // MODIFIES: this
    // EFFECTS: lays out sidePanel components
    private void placeItemsOnSidePanel() {
        sidePanel = new JPanel();
        sidePanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        sidePanel.add(formulaPanel, constraints);
        constraints.gridy++;
        JScrollPane pane = new JScrollPane(stats);
        pane.setPreferredSize(new Dimension(200, 180));
        sidePanel.add(pane, constraints);
        constraints.gridy++;
        sidePanel.add(enumPanel, constraints);
        add(sidePanel, BorderLayout.WEST);
    }

    // EFFECTS: sets up enumeration panel panels with correct value and image
    private JPanel setupEnumPanel(String attributeName,
                                  String attributeValue,
                                  ImageIcon displayImage) {
        JPanel enumPanel = new JPanel();
        enumPanel.setLayout(new BoxLayout(enumPanel, BoxLayout.Y_AXIS));
        enumPanel.setBorder(BorderFactory.createDashedBorder(Color.BLACK));
        JLabel cleavageLabel = new JLabel(attributeName);
        cleavageLabel.setFont(new Font("Inter", Font.PLAIN, 25));
        cleavageLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cleavageLabel.setHorizontalAlignment(SwingConstants.LEFT);
        cleavageLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));


        JLabel attributeLabel = new JLabel(StringUtils.getSentenceCase(attributeValue));
        attributeLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        attributeLabel.setFont(new Font("Inter", Font.ITALIC, 40));
        attributeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        enumPanel.add(cleavageLabel);
        enumPanel.add(attributeLabel);
        JLabel imageLabel = new JLabel(displayImage);
        enumPanel.add(imageLabel);
        return enumPanel;
    }

    // EFFECTS: sets up cleavage Panel with correct image and label
    private JPanel setupCleavagePanel() {
        return setupEnumPanel(
                AttributeNames.CLEAVAGE,
                mineral.getCleavage().getLiteralString(),
                Images.getInstanceCleavageImage(mineral.getCleavage()));
    }

    // EFFECTS: sets up crystal structure panel with correct image and label
    private JPanel setupCrystalPanel() {
        return setupEnumPanel(
                AttributeNames.CRYSTAL_STRUCTURE,
                mineral.getCrystalStructure().getLiteralString(),
                Images.getInstanceCrystalImage(mineral.getCrystalStructure()));
    }

    // EFFECTS: produces table filled with quantitative properties of mineral
    private void setupStats() {
        stats = new JTable(getQuantitativeTable(),
                new String[]{"Property", "Value"});
        stats.setFillsViewportHeight(true);
        stats.setRowHeight(60);
    }

    // EFFECTS: produces 2D array with quantitative properties and corresponding values
    public String[][] getQuantitativeTable() {
        String[] ior = new String[]{AttributeNames.IOR + " (Dimensionless)",
                String.valueOf(mineral.getIndexOfRefraction())};
        String[] hardness = new String[]{AttributeNames.HARDNESS + " (Mohs)",
                String.valueOf(mineral.getHardness())};
        String[] density = new String[]{AttributeNames.DENSITY + " (g/cm^3)",
                String.valueOf(mineral.getDensity())};

        return new String[][]{hardness, ior, density};
    }

}
