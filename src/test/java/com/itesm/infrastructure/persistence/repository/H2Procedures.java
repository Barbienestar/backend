package com.itesm.infrastructure.persistence.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class H2Procedures {

    public static ResultSet getMonthlyReports(Connection conn, int idHospital) throws SQLException {
        String sql = """
                WITH current_month_reports AS (
                    SELECT COALESCE(SUM(daily_reports), 0) AS total
                    FROM Reports_Snapshot
                    WHERE id_hospital = ?
                      AND YEAR(snapshot_date) = YEAR(NOW())
                      AND MONTH(snapshot_date) = MONTH(NOW())
                ),
                last_month_reports AS (
                    SELECT COALESCE(SUM(daily_reports), 0) AS total
                    FROM Reports_Snapshot
                    WHERE id_hospital = ?
                      AND YEAR(snapshot_date) = YEAR(DATEADD(MONTH, -1, NOW()))
                      AND MONTH(snapshot_date) = MONTH(DATEADD(MONTH, -1, NOW()))
                )
                SELECT
                    CAST(cm.total AS INT) AS current_month_report_count,
                    CASE
                        WHEN lm.total = 0 AND cm.total = 0 THEN CAST(0.00 AS DECIMAL(15, 4))
                        WHEN lm.total = 0 AND cm.total > 0 THEN CAST(1.00 AS DECIMAL(15, 4))
                        ELSE CAST(((cm.total - lm.total) / lm.total) AS DECIMAL(15, 4))
                    END AS comparison_to_last_month
                FROM current_month_reports cm
                CROSS JOIN last_month_reports lm
                """;

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idHospital);
        stmt.setInt(2, idHospital);
        return stmt.executeQuery(); 
    }
}