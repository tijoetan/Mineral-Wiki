package persistence;

import model.chemicalstructure.Formula;
import model.entries.Family;
import model.entries.Mineral;
import model.entries.WikiEntry;
import model.enums.CrystalStructure;
import model.modelexceptions.DuplicationException;
import model.modelexceptions.ItemNotFoundException;
import model.modelexceptions.UnknownElementException;
import model.tableentry.FamilyTable;
import model.tableentry.MineralTable;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.FillWikiEntry;
import utils.JsonFieldNames;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

// Reads WikiEntryTable from JSON
public class TableReader {
    private final String source;
    private final FamilyTable familyTable;
    private final MineralTable mineralTable;

    public TableReader(String source, FamilyTable familyTable, MineralTable mineralTable) {
        this.source = source;
        this.familyTable = familyTable;
        this.mineralTable = mineralTable;
    }

    public JSONObject readFile() throws IOException {
        StringBuilder sourceStream = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(sourceStream::append);
        }
        return new JSONObject(sourceStream.toString());
    }

    public void setupTables() throws IOException {
        JSONObject readFile = readFile();
        JSONObject mineralJson = readFile.getJSONObject("minerals");
        JSONObject familyJson = readFile.getJSONObject("families");
        setUpMineralTable(mineralJson);
        setUpFamilyTable(familyJson);
    }

    public void setUpMineralTable(JSONObject mineralJson) {
        Arrays.stream(JSONObject.getNames(mineralJson))
                .forEach(s -> {
                    try {
                        mineralTable.addEntry(setupMineral(mineralJson.getJSONObject(s)));
                    } catch (DuplicationException e) {
                        // ;
                    }
                });
    }

    private Mineral setupMineral(JSONObject mineralData) {
        Mineral mineral = new Mineral(mineralData.getString(JsonFieldNames.NAME));
        FillWikiEntry.fillMineral(mineral,
                getFormula(mineralData.getString(JsonFieldNames.FORMULA)),
                CrystalStructure.valueOf(mineralData.getString(JsonFieldNames.CRYSTAL_STRUCTURE)),
                mineralData.getFloat(JsonFieldNames.HARDNESS),
                mineralData.getFloat(JsonFieldNames.DENSITY),
                mineralData.getFloat(JsonFieldNames.INDEX_OF_REFRACTION),
                mineralData.getString(JsonFieldNames.DESCRIPTION));
        return mineral;
    }

    private static Formula getFormula(String mineralFormulaName) {
        try {
            return mineralFormulaName.isEmpty() ? new Formula() : new Formula(mineralFormulaName);
        } catch (UnknownElementException e) {
            return new Formula();
        }
    }

    public Family setUpFamily(JSONObject familyJson) {
        Family family = new Family(familyJson.getString(JsonFieldNames.NAME));
        FillWikiEntry.fillFamily(family,
                getFormula(familyJson.getString(JsonFieldNames.FORMULA)),
                getRelatedMinerals(familyJson.getJSONArray(JsonFieldNames.MINERALS_OF_FAMILY)),
                familyJson.getString(JsonFieldNames.DESCRIPTION));
        return family;

    }

    private void setUpFamilyTable(JSONObject familyJson) {
        Arrays.stream(JSONObject.getNames(familyJson))
                .forEach(s -> {
                    try {
                        familyTable.addEntry(setUpFamily(familyJson.getJSONObject(s)));
                    } catch (DuplicationException e) {
                        //
                    }
                });
    }

    private List<WikiEntry> getRelatedMinerals(JSONArray mineralsWithFamilyName) {
        List<WikiEntry> relatedMinerals = new ArrayList<>();
        for (int i = 0; i < mineralsWithFamilyName.length(); i++) {
            try {
                WikiEntry mineral = mineralTable.getRequestedEntry(mineralsWithFamilyName.getString(i));
                relatedMinerals.add(mineral);
            } catch (ItemNotFoundException e) {
                //
            }
        }
        return relatedMinerals;
    }
}
