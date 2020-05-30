package it.polito.tdp.artsmia.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	private ArtsmiaDAO dao;
	private Graph<Artista, DefaultWeightedEdge> grafo;
	private Map<Integer, Artista> idMap;

	public Model() {
		dao = new ArtsmiaDAO();
	}
	
	public List<String> listaRuoli(){
		return dao.listaRuoli();
	}
	
	public void creaGrafo(String ruolo) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap = new HashMap<>();
		
		//vertici
		Graphs.addAllVertices(this.grafo, dao.listaArtisti(ruolo,idMap));
		
		//archi
		
		for(Arco a : dao.listaArchi(ruolo, idMap)) {
			
			if(!this.grafo.containsEdge(a.getA1(), a.getA2())){
				
				Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			}
			
		}
		
		
	}
	
	public List<Arco> archiDecrescenti(){
		
		List<Arco> archi = new LinkedList<>();
		
	
		for(DefaultWeightedEdge d:this.grafo.edgeSet()) {
			
			Arco a = new Arco(this.grafo.getEdgeSource(d), this.grafo.getEdgeTarget(d), (int)this.grafo.getEdgeWeight(d));
			archi.add(a);
		}

		//riordino
		
		Collections.sort(archi);
		return archi;
	}
	
	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
}
