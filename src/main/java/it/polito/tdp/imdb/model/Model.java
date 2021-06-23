package it.polito.tdp.imdb.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	private SimpleWeightedGraph<Actor, DefaultWeightedEdge> grafo;
	private ImdbDAO dao;
	private Map<Integer,Actor> idMap;
	
	public Model() {
		this.dao = new ImdbDAO();
		this.idMap = new HashMap<Integer,Actor>(); 
		//riempio l'idMap
		dao.listAllActors(idMap);
	}
	
	public void creaGrafo(String g) {
		this.grafo = new SimpleWeightedGraph<Actor, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		//aggiungo i vertici
		Graphs.addAllVertices(grafo, dao.getActorByGenere(g, idMap));
		
		//aggiungo gli archi 
	}
	
	
	
	
	
	
	

	public List<String> getAllGeneri() {
		return dao.getAllGeneri();
	}

}
