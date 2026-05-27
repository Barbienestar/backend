package com.itesm.infrastructure.persistence.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.itesm.application.dto.MonthlyReportsResponse;

public class H2Procedures {
    public static Optional<MonthlyReportsResponse> getMonthlyReports(Connection conn, int idHospital) throws SQLException {

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

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idHospital); // current_month_reports WHERE
            stmt.setInt(2, idHospital); // last_month_reports WHERE

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Integer currentMonthReportCount = rs.getInt("current_month_report_count");
                    BigDecimal comparisonToLastMonth = rs.getBigDecimal("comparison_to_last_month");

                    return Optional.of(new MonthlyReportsResponse(currentMonthReportCount, comparisonToLastMonth));
                }
            }
        }

        return Optional.empty();
    }
}