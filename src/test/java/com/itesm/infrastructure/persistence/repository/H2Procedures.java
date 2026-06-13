package com.itesm.infrastructure.persistence.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class H2Procedures {

    public static ResultSet getMonthlyReports(
            Connection conn, int idHospital, LocalDate firstDate, LocalDate secondDate) throws SQLException {
        String sql =
                """
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

    public static ResultSet getHospitalStockAverages(
            Connection conn, int idHospital, LocalDate firstDate, LocalDate secondDate) throws SQLException {
        String sql =
                """
                SELECT
                    CAST(COALESCE(AVG(mh.stock), 0) AS DECIMAL(15, 4)) AS last_month_avg,
                    CAST(COALESCE(AVG(mh.stock), 0) AS DECIMAL(15, 4)) AS current_month_avg
                FROM Medicines_Hospitals mh
                WHERE mh.id_hospital = ?
                """;
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idHospital);
        return stmt.executeQuery();
    }

    public static ResultSet getHospitalStockReport(
            Connection conn, int idHospital, LocalDate firstDate, LocalDate secondDate) throws SQLException {
        String sql =
                """
                SELECT
                    CAST(COUNT(*) AS INT) AS low_stock_count,
                    CAST(GROUP_CONCAT(m.generic_name SEPARATOR ', ') AS VARCHAR) AS bottom_medicines
                FROM Medicines_Hospitals mh
                JOIN Medicines m ON mh.id_medicine = m.id
                WHERE mh.id_hospital = ?
                  AND mh.stock <= 9
                """;
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idHospital);
        return stmt.executeQuery();
    }
}
