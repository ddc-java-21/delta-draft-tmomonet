package edu.cnm.deepdive.deltadraft.service.scraper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class PlayerScraper {

  // 1. Download CSV (OkHttp example)
  OkHttpClient client = new OkHttpClient();
  Request request = new Request.Builder()
      .url("https://www.baseball-reference.com/leagues/majors/2025-advanced-batting.csv")
      .build();

    client.newCall(request).enqueue(new Callback() {
    @Override
    public void onFailure (Call call, IOException e){
      e.printStackTrace();
    }

    @Override
    public void onResponse (Call call, Response response) throws IOException {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }

      InputStream csvStream = response.body().byteStream();

      // 2. Parse CSV (example using OpenCSV)
      try (CSVReader reader = new CSVReader(new InputStreamReader(csvStream))) {
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
          // nextLine[] is an array of values from the line
          // e.g. nextLine[0] = Player Name, nextLine[1] = Team, etc.
        }
      }
    }
  });

}