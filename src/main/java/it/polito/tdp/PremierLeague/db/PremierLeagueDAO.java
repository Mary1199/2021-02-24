package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.Team;


public class PremierLeagueDAO {
	
	public void listAllPlayers(Map<Integer, Player> idMap){
		String sql = "SELECT * FROM Players";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(!idMap.containsKey(res.getInt("PlayerID"))) {
					Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				    idMap.put(player.getPlayerID(), player);
				}
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Team> listAllTeams(){
		String sql = "SELECT * FROM Teams";
		List<Team> result = new ArrayList<Team>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Team team = new Team(res.getInt("TeamID"), res.getString("Name"));
				result.add(team);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Match> listAllMatches(){
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID";
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
				
				
				result.add(match);

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Player> listAllGiocatori(Match match, Map<Integer, Player> idMap){
		String sql = "SELECT PlayerID FROM actions WHERE MatchID = ?";
	    List<Player> result = new ArrayList<Player>();
	    Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, match.getMatchID());
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
	           if(idMap.containsKey(res.getInt("PlayerID"))) {
	        	   result.add(idMap.get(res.getInt("PlayerID")));
	           }
			}
			conn.close();
			return result;
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

		
		public List<Adiacenza> getAdiacenze(Match match, Map<Integer, Player> idMap){
			String sql = "SELECT a1.PlayerID AS v1, a2.PlayerID AS v2, ((a1.TotalSuccessfulPassesAll + a1.Assists)/a1.TimePlayed - (a2.TotalSuccessfulPassesAll + a2.Assists)/a2.TimePlayed) AS peso "
					+ "FROM actions AS a1, actions AS a2 "
					+ "WHERE a1.TeamID <> a2.TeamID AND a1.MatchID = ? AND a1.MatchID = a2.MatchID AND a1.PlayerID > a2.PlayerID";
			List<Adiacenza> result = new ArrayList<Adiacenza>();
			try  {
				Connection conn = DBConnect.getConnection() ;
				PreparedStatement st = conn.prepareStatement(sql) ;
				st.setInt(1, match.getMatchID());

				ResultSet res = st.executeQuery() ;

				while(res.next()) {
					 if(idMap.containsKey(res.getInt("v1")) && idMap.containsKey(res.getInt("v2"))) {
					result.add(new Adiacenza(idMap.get(res.getInt("v1")), idMap.get(res.getInt("v2")), res.getDouble("peso")));
					 }
					 }
				st.close();
				res.close();
				conn.close();
				return result;

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null ;
			}
  }
}
