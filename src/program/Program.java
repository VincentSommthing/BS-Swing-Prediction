package program;

import beatmap.BeatmapV2;
import beatmap.BeatmapV2_6;
import beatmap.BeatmapV3;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import defaultSwingPredictor.*;
import swingPredictor.Predictor;

import java.io.File;
import java.util.Set;
import java.util.List;

public class Program {
    private static final Set<String> V2VERSIONS = Set.of("2.0.0", "2.2.0", "2.4.0", "2.5.0");
    private static final Set<String> V2_6VERSIONS = Set.of("2.6.0");
    private static final Set<String> V3VERSIONS = Set.of("3.0.0", "3.1.0", "3.2.0", "3.3.0");
    public static void main(String[] args) {
        String testV2 = "/Users/gamer/Desktop/Real Gaming/Program/beat saber map generation/26d33/EasyStandard.dat";
        String testV3 = "/Users/gamer/Desktop/Real Gaming/Program/beat saber map generation/387a0 /ExpertPlusStandard.dat";
        double bpmV3 = 181.0;
        String testV3Bomb = "/Users/gamer/Desktop/Real Gaming/Program/beat saber map generation/3d86c/ExpertPlusStandard.dat";
        double bpmV3Bomb = 125.5;

        BeatmapV3 beatmap = getBeatmapFromPath(testV3);

        DefaultCostFn costFn = new DefaultCostFn();
        DefaultSwingProposer proposer = new DefaultSwingProposer();

        Predictor<DefaultSwing> predictor = new Predictor<DefaultSwing>(proposer, costFn);

        long t0 = System.nanoTime();

        List<DefaultSwing> swings = predictor.predict(beatmap, bpmV3);

        long t1 = System.nanoTime();
        System.out.println("Calculation time: " + String.valueOf((t1 - t0) * 1e-6) + " ms.");
    }

    /**
     * Creates a BeatmapV3 object given a path to a beatmap JSON file
     * @param path path to file
     * @return beatmap object
     */
    public static BeatmapV3 getBeatmapFromPath(String path) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // get JSON
            JsonNode json = objectMapper.readTree(new File(path));

            // get version
            String version = "";
            if (json.has("_version")) {
                version = json.get("_version").asText();
            } else if (json.has("version")) {
                version = json.get("version").asText();
            }

            // create beatmap object
            if (V2VERSIONS.contains(version)) {
                BeatmapV2 beatmapV2 = objectMapper.convertValue(json, BeatmapV2.class);
                // convert to v3
                return new BeatmapV3(beatmapV2);

            } else if (V2_6VERSIONS.contains(version)) {
                BeatmapV2_6 beatmapV2_6 = objectMapper.convertValue(json, BeatmapV2_6.class);
                // convert to v3
                return new BeatmapV3(beatmapV2_6);

            } else if (V3VERSIONS.contains(version)) {
                return objectMapper.convertValue(json, BeatmapV3.class);

            } else {
                throw new Exception("Unsupported version: " + version);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
