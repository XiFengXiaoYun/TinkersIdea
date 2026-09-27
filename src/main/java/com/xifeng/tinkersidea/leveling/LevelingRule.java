package com.xifeng.tinkersidea.leveling;

import com.google.gson.*;
import com.xifeng.tinkersidea.TinkersIdea;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LevelingRule {
    private final List<String> tools;
    private final List<StatsModifier> statsModifiers;

    public LevelingRule(List<String> tools, List<StatsModifier> statsModifiers) {
        this.tools = tools;
        this.statsModifiers = statsModifiers;
    }

    public boolean has(String tool) {
        return tools.contains(tool);
    }

    public List<StatsModifier> getStatsModifiers() {
        return Collections.unmodifiableList(statsModifiers);
    }

    public List<String> getTools() {
        return Collections.unmodifiableList(tools);
    }

    public List<StatsModifier> matchModifier(int level) {
        List<StatsModifier> list = new ArrayList<>();
        for(StatsModifier mod : statsModifiers) {
            if(level % mod.threshold == 0) {
                list.add(mod);
            }
        }
        return list;
    }

    public static void loadFromJson(Path jsonDir, List<LevelingRule> ruleLists) {
        if (jsonDir == null) return;

        if (!Files.exists(jsonDir)) {
            try {
                Files.createDirectories(jsonDir);
                TinkersIdea.logger.info("Created config directory: {}", jsonDir);
            } catch (IOException e) {
                TinkersIdea.logger.error("Failed to create directory: {}", jsonDir, e);
                return;
            }
        }

        Path filePath = jsonDir.resolve("leveling_rules.json");
        if (!Files.exists(filePath)) {
            createExampleJson(filePath);
        }

        try (FileReader reader = new FileReader(filePath.toFile())) {
            JsonParser parser = new JsonParser();
            JsonElement root = parser.parse(reader);

            if (root != null && root.isJsonArray()) {
                JsonArray rootArray = root.getAsJsonArray();
                ruleLists.clear();

                for (JsonElement element : rootArray) {
                    if (!element.isJsonObject()) continue;
                    JsonObject ruleObj = element.getAsJsonObject();

                    List<String> tools = new ArrayList<>();
                    if (ruleObj.has("tools") && ruleObj.get("tools").isJsonArray()) {
                        for (JsonElement toolElem : ruleObj.getAsJsonArray("tools")) {
                            tools.add(toolElem.getAsString());
                        }
                    }

                    List<StatsModifier> modifiers = new ArrayList<>();
                    if (ruleObj.has("statsModifiers") && ruleObj.get("statsModifiers").isJsonArray()) {
                        for (JsonElement modElem : ruleObj.getAsJsonArray("statsModifiers")) {
                            if (!modElem.isJsonObject()) continue;
                            JsonObject modObj = modElem.getAsJsonObject();

                            String stats = modObj.has("stats") ? modObj.get("stats").getAsString() : "unknown";
                            int threshold = modObj.has("threshold") ? modObj.get("threshold").getAsInt() : 1;
                            double base = modObj.has("base") ? modObj.get("base").getAsDouble() : 0.0;
                            int type = modObj.has("type") ? modObj.get("type").getAsInt() : 1;

                            StatsModifier modifier = new StatsModifier(stats, base, threshold);
                            modifier.type = type;
                            modifiers.add(modifier);
                        }
                    }

                    ruleLists.add(new LevelingRule(tools, modifiers));
                }
                TinkersIdea.logger.info("Successfully loaded {} leveling rules from JSON.", ruleLists.size());
            } else {
                TinkersIdea.logger.error("Invalid JSON format: Root must be a JsonArray.");
            }
        } catch (Exception e) {
            TinkersIdea.logger.error("Failed to load leveling rules from JSON file: {}", filePath, e);
        }
    }

    private static void createExampleJson(Path filePath) {
        String exampleJson = "[\n" +
                "  {\n" +
                "    \"tools\": [\n" +
                "      \"tconstruct:pickaxe\",\n" +
                "      \"tconstruct:hammer\"\n" +
                "    ],\n" +
                "    \"statsModifiers\": [\n" +
                "      {\n" +
                "        \"stats\": \"Attack\",\n" +
                "        \"threshold\": 5,\n" +
                "        \"base\": 1.0,\n" +
                "        \"type\": 2\n" +
                "      },\n" +
                "      {\n" +
                "        \"stats\": \"Durability\",\n" +
                "        \"threshold\": 2,\n" +
                "        \"base\": 50.0,\n" +
                "        \"type\": 1\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"tools\": [\n" +
                "      \"tconstruct:broadsword\"\n" +
                "    ],\n" +
                "    \"statsModifiers\": [\n" +
                "      {\n" +
                "        \"stats\": \"Attack\",\n" +
                "        \"threshold\": 3,\n" +
                "        \"base\": 2.0,\n" +
                "        \"type\": 2\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]";

        try {
            Files.write(filePath, exampleJson.getBytes(StandardCharsets.UTF_8));
            TinkersIdea.logger.info("Created example leveling rules JSON at: {}", filePath.toString());
        } catch (IOException e) {
            TinkersIdea.logger.error("Failed to create example JSON file", e);
        }
    }
}
