package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<String> getCategorie(){
		String sql = "SELECT DISTINCT e.offense_category_id AS id " + 
				"FROM EVENTS e " +
				"ORDER BY id " ;
		List<String> cat = new ArrayList<String>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				cat.add(res.getString("id"));
			}
			conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return cat;
	}
	
	public List<Integer> getGiorni(){
		String sql = "SELECT DISTINCT DAY(e.reported_date) AS giorno " + 
				"FROM EVENTS e " + 
				"ORDER BY giorno " ;
		List<Integer> giorni = new ArrayList<Integer>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				giorni.add(res.getInt("giorno"));
			}
			conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return giorni;
	}
	
	public List<String> getVertici(String categoria, Integer giorno){
		String sql = "SELECT DISTINCT e.offense_type_id as id " + 
				"FROM EVENTS e " + 
				"WHERE e.offense_category_id = ? " + 
				"AND DAY(e.reported_date) = ? " ;
		List<String> vertici = new ArrayList<String>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, categoria);
			st.setInt(2, giorno);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				vertici.add(res.getString("id"));
			}
			conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return vertici;
	}
	
	public List<Adiacenza> getAdiacenze(String categoria, Integer giorno){
		String sql = "SELECT e1.offense_type_id AS id1, e2.offense_type_id AS id2, COUNT(e1.precinct_id) AS peso " + 
				"FROM EVENTS e1, EVENTS e2 " + 
				"WHERE e1.offense_category_id = ? " + 
				"AND e2.offense_category_id = ? " + 
				"AND DAY(e1.reported_date) =  ? " + 
				"AND DAY(e2.reported_date) = ? " + 
				"AND e1.offense_type_id > e2.offense_type_id " + 
				"AND e1.precinct_id = e2.precinct_id " ;
		List<Adiacenza> adiacenze = new ArrayList<Adiacenza>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, categoria);
			st.setString(2, categoria);
			st.setInt(3, giorno);
			st.setInt(4, giorno);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				adiacenze.add(new Adiacenza(res.getString("id1"), res.getString("id2"), res.getDouble("peso")));
			}
			conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return adiacenze;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
