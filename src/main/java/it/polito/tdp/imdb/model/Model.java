package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
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
	
	public /*String*/void creaGrafo(String g) {
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
//		return String.format("Grafo creato %d vertici e %d archi\n", this.grafo.vertexSet().size(),this.grafo.edgeSet().size());

	}

	public List<String> getAllGeneri() {
		return dao.getAllGeneri();
	}
	
	public Set<Actor> getVertici(){
		return this.grafo.vertexSet();
	}
	public Set<DefaultWeightedEdge> getArchi(){
		return this.grafo.edgeSet();
	}
/*	public List<Actor> getAttoriDelGrafo(){
		List<Actor> listInutile = new LinkedList<Actor>();
		for(Actor a : idMap.values()) {
			if(grafo.containsVertex(a)) {
				listInutile.add(a);
			}
		}
		return listInutile;
	}*/
	//metodo per ordinare in ordine alfabetico
	public List<Actor> getActors(){
		List<Actor> actors = new ArrayList<>(grafo.vertexSet());
		Collections.sort(actors, new Comparator<Actor>() {
			@Override
			public int compare(Actor a1, Actor a2) {
				return a1.getLastName().compareTo(a2.getLastName());
			}
		}
				);
		return actors;
	}
	
	public List<Actor> trovaSimili(Actor actor){
		ConnectivityInspector<Actor, DefaultWeightedEdge> ci = new ConnectivityInspector<Actor, DefaultWeightedEdge>(grafo);
		List<Actor> actors = new ArrayList<>(ci.connectedSetOf(actor));
		actors.remove(actor);
		Collections.sort(actors, new Comparator<Actor>() {
			@Override
			public int compare(Actor a1, Actor a2) {
				return a1.getLastName().compareTo(a2.getLastName());
			}
		});
		return actors;
	}

}
