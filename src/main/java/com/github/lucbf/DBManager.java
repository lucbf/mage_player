package com.github.lucbf;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.Universe;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.sql.*;

public final class DBManager{
    private Connection conn;

    private DBManager(HytaleLogger.Api logger) {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String jdbcurl = "jdbc:hsqldb:file:" + Universe.get().getPath() + "/mage_player;shutdown=true";
        String user = "SA";
        String password = "";

        try {
            this.conn = DriverManager.getConnection(jdbcurl, user, password);
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS mana_players (id INTEGER PRIMARY KEY, mana_type INTEGER)");
        } catch (SQLException e) {
            logger.log(e.toString());
        }
    }

    @NullableDecl
    ManaType getManaFromPlayer(int player_hash) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("SELECT mana_type FROM mana_players WHERE ID = ?");
        pstmt.setInt(1, player_hash);

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            switch (rs.getInt("mana_type")) {
                case 0: {
                    return ManaType.FIRE;
                }
                case 1: {
                    return ManaType.ICE;
                }
                case  2: {
                    return ManaType.ANY;
                }
            }
        } else {
            return null;
        }
        return null;
    }

    void alterStatusPlayer(int player_hash, ManaType mt) throws SQLException {
        ManaType mtquery = getManaFromPlayer(player_hash);
        if (mt != ManaType.ANY && mtquery != mt && mtquery != null) {
            mt = ManaType.ANY;
        }

        PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM mana_players WHERE id = ?");
        pstmt.setInt(1, player_hash);
        ResultSet rs = pstmt.executeQuery();

        int quantidade = 0;

        if (rs.next()) {
            quantidade = rs.getInt(1);
        }

        if (quantidade == 1) {
            pstmt = conn.prepareStatement("UPDATE mana_players SET mana_type = ? WHERE id = ?");
            pstmt.setInt(1, mt.getVal());
            pstmt.setInt(2, player_hash);
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException();
            }
        } else {
            pstmt = conn.prepareStatement("INSERT INTO mana_players (id, mana_type) VALUES (?, ?)");
            pstmt.setInt(1, player_hash);
            pstmt.setInt(2, mt.getVal());
            pstmt.executeUpdate();
        }
    }

    public static DBManager DB_MANAGER = new DBManager(HytaleLogger.getLogger().atInfo());
}