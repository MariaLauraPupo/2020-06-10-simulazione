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
		for(Arco a: dao.getArchi(g, idMap)) {
			if(grafo.containsVertex(a.getA1()) && grafo.containsVertex(a.getA2())) {
				DefaultWeightedEdge e = this.grafo.addEdge(a.getA1(), a.getA2());
				if(e == null) {
					Graphs.addEdgeWithVertices(grafo, a.getA1(), a.getA2());
				}
			}
		}
		System.out.println("vertici " + grafo.vertexSet().size());
		System.out.println("archi " + grafo.edgeSet().size());

	}

	public List<String> getAllGeneri() {
		return dao.getAllGeneri();
	}

}
