package program;

import beatmap.BeatmapV2;
import beatmap.BeatmapV2_6;
import beatmap.BeatmapV3;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Set;

public class Program {
    private static final Set<String> V2VERSIONS = Set.of("2.0.0", "2.2.0", "2.4.0", "2.5.0");
    private static final Set<String> V2_6VERSIONS = Set.of("2.6.0");
    private static final Set<String> V3VERSIONS = Set.of("3.0.0");
    public static void main(String[] args) {
        String testStr = "/Users/gamer/Desktop/Real Gaming/Program/beat saber map generation/26d33/EasyStandard.dat";
        BeatmapV3 beatmap = getBeatmapFromPath(testStr);
        System.out.println(beatmap);
    }

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
