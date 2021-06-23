package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Arco;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public void/*List<Actor>*/ listAllActors(Map<Integer,Actor> map){
		String sql = "SELECT * FROM actors";
//		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!map.containsKey(res.getInt("id"))) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				map.put(res.getInt("id"), actor);
				}
//				result.add(actor);
			}
			conn.close();
//			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
//			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<String> getAllGeneri(){
		String sql = "SELECT  genre AS genere "
				+"FROM movies_genres "
				+"GROUP BY genre";
		List<String> result = new LinkedList<String>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				String nomeGenere = new String(res.getString("genere"));
				
				result.add(nomeGenere);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Actor> getActorByGenere(String g, Map<Integer,Actor> map){
		String sql =  "SELECT a.first_name AS nome, a.last_name AS cognome, a.id AS actorId, a.gender as gender "
				+ "FROM actors a, movies_genres mg, roles r "
				+ "WHERE a.id = r.actor_id AND r.movie_id = mg.movie_id AND mg.genre = ? "
				+ "GROUP BY a.id "
				+ "ORDER BY a.first_name ASC";
		
		List<Actor> result = new LinkedList<Actor>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, g);
			ResultSet res = st.executeQuery();
			while (res.next()) {
		
				result.add(map.get(res.getInt("actorId")));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	public List<Arco> getArchi(String g, Map<Integer, Actor> map){
		String sql = "SELECT  a1.id AS a1Id, a2.id AS a2Id, COUNT(*) AS peso "
				+ "FROM actors a1, actors a2, movies_genres mg, roles r1, roles r2 "
				+ "WHERE a1.id = r1.actor_id AND a2.id = r2.actor_id AND r1.movie_id = r2.movie_id AND r1.movie_id = mg.movie_id AND mg.genre = ? AND a1.id <> a2.id "
				+ "GROUP BY a1.id, a2.id";
		List<Arco> result = new LinkedList<Arco>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, g);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor a1 = map.get(res.getInt("a1Id"));
				Actor a2 = map.get(res.getInt("a2Id"));
				if(a1!=null && a2!=null) {
					Arco arco = new Arco(a1,a2,res.getInt("peso"));
					result.add(arco);
				}
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
