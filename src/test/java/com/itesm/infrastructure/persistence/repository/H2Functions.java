package com.itesm.infrastructure.persistence.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class H2Functions {

    public static int countReportsByStatus(Connection conn, int statusId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM Reports WHERE id_status = ?")) {
            ps.setInt(1, statusId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }

    public static int countUsersByRole(Connection conn, int roleId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM Users WHERE id_role = ?")) {
            ps.setInt(1, roleId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }
}
