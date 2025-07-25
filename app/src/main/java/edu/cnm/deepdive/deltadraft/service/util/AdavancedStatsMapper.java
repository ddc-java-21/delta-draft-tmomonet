//package edu.cnm.deepdive.deltadraft.service.util;
//
//import android.content.Context;
//import com.opencsv.CSVReader;
//import com.opencsv.CSVReaderBuilder;
//import edu.cnm.deepdive.deltadraft.R;
//import edu.cnm.deepdive.deltadraft.service.dao.PlayerDao;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.Reader;
//import java.util.HashMap;
//import java.util.Map;
//
//public class AdavancedStatsMapper {
//  Context context;
//  PlayerDao playerDao;
//
//  Map<String, String[]> advancedStatsMap = new HashMap<>();
//
//try (
//  InputStream input = context.getResources().openRawResource(R.raw.advanced2025);
//  Reader reader = new InputStreamReader(input);
//  CSVReader csv = new CSVReaderBuilder(reader).withSkipLines(1).build() {
//    String[] line;
//    while ((line = csv.readNext()) != null) {
//      String playerId = line[34].trim(); // Adjust index as needed
//      AdvancedStats stats = new AdvancedStats();
//      stats.setBbPct(parseFloatSafe(line[13]));   // BB%
//      stats.setKPct(parseFloatSafe(line[14]));    // K%
//      stats.setBabip(parseFloatSafe(line[11]));   // BABIP
//      // Add more fields as needed
//
//      advancedStatsMap.put(playerId, stats);
//    }
//  }
//
//}
