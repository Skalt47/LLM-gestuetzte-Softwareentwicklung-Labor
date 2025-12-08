package db.migration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V2__ImportDinoData extends BaseJavaMigration {

  @Override
  public void migrate(Context context) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    try (
      InputStream in = getClass()
        .getClassLoader()
        .getResourceAsStream("data/DinoData.json")
    ) {
      if (in == null) throw new IllegalStateException(
        "data/DinoData.json not found on classpath"
      );

      List<Map<String, Object>> rows = mapper.readValue(
        in,
        new TypeReference<>() {}
      );
      String sql =
        """
        INSERT INTO dinosaurs (species, group_code, lifespan_years, length_m, speed_kmh, intelligence, attack, defense)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        ON CONFLICT (species) DO UPDATE SET
          group_code = EXCLUDED.group_code,
          lifespan_years = EXCLUDED.lifespan_years,
          length_m = EXCLUDED.length_m,
          speed_kmh = EXCLUDED.speed_kmh,
          intelligence = EXCLUDED.intelligence,
          attack = EXCLUDED.attack,
          defense = EXCLUDED.defense
        """;

      try (
        PreparedStatement ps = context.getConnection().prepareStatement(sql)
      ) {
        for (Map<String, Object> r : rows) {
          ps.setString(1, (String) r.get("species"));
          ps.setString(2, (String) r.get("group"));
          ps.setObject(
            3,
            r.get("lifespan_years") == null
              ? null
              : ((Number) r.get("lifespan_years")).intValue()
          );
          ps.setObject(
            4,
            r.get("length_m") == null
              ? null
              : ((Number) r.get("length_m")).doubleValue()
          );
          ps.setObject(
            5,
            r.get("speed_kmh") == null
              ? null
              : ((Number) r.get("speed_kmh")).intValue()
          );
          ps.setObject(
            6,
            r.get("intelligence") == null
              ? null
              : ((Number) r.get("intelligence")).intValue()
          );
          ps.setObject(
            7,
            r.get("attack") == null
              ? null
              : ((Number) r.get("attack")).intValue()
          );
          ps.setObject(
            8,
            r.get("defense") == null
              ? null
              : ((Number) r.get("defense")).intValue()
          );
          ps.addBatch();
        }
        ps.executeBatch();
      }
    }
  }
}
