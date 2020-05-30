package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Arco;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artista;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<String> listaRuoli(){
		String sql ="select distinct role " + 
				"from authorship " + 
				"order by role ";
		
		List<String> ruoli= new LinkedList<>();
		

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				ruoli.add(res.getString("role"));
			}
			conn.close();
			return ruoli;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Artista> listaArtisti(String ruolo, Map<Integer, Artista> idMap){
		String sql="select distinct artists.`artist_id` as id, name " + 
				"from artists, authorship " + 
				"where artists.`artist_id`=authorship.`artist_id` and " + 
				"role =? ";
		
		List<Artista> artisti = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Artista a = new Artista(res.getInt("id"), res.getString("name"));
				artisti.add(a);
				idMap.put(a.getId(),a);
			}
			conn.close();
			return artisti;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Arco> listaArchi(String ruolo, Map<Integer, Artista> idMap){
		String sql="select  a1.artist_id as id1, a2.`artist_id` as id2, count(distinct e1.exhibition_id) as peso " + 
				"from exhibition_objects as e1, exhibition_objects as e2, authorship a1, authorship a2 " + 
				"where e1.`exhibition_id` = e2.`exhibition_id` and " + 
				" e1.`object_id` != e2.`object_id` and " + 
				" e1.`object_id` = a1.`object_id`  and " + 
				" e2.`object_id` = a2.`object_id` and  " + 
				" a1.`role`=? and a2.`role`=? " + 
				" and a1.`artist_id`< a2.`artist_id` " + 
				"group by a1.`artist_id`, a2.`artist_id` ";
		
		//oggetti diversi, artista1< artista2 (prima avevo messo il contrario)
		//altrimenti si potrebbe avere oggetto1<oggetto2 con artista1!=artista2
		//oggetto3<oggetto4 con artista2!= artista1 => quindi raggruppando artista1-artista2 / artista2-artista1 li vede come altra coppia
		List<Arco> archi = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			st.setString(2, ruolo);
			
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				int id1=res.getInt("id1");
				int id2=res.getInt("id2");
				int peso = res.getInt("peso");
				
				Arco a = new Arco(idMap.get(id1), idMap.get(id2), peso);
				archi.add(a);
			}
			conn.close();
			return archi;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
}
