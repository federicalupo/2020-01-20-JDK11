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
	private int dimensioneMax;
	private List<Artista> migliore;
	private int pesoPrecedente;

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
	
	public Artista verificaId(Integer id) {
		
		//se c'è id ritorna oggetto altrimenti null
		Artista a = this.idMap.get(id);

		return a;
		
	}
	
	/**RICORSIVA
	 * 
	 * parto da artista
	 * dammi i vicini
	 * per ogni vicino: se hanno stesso peso e non sono già nella lista => aggiungi
	 * 
	 * ListaPiuLunga 
	 * MaxnumeroComponenti = listapiulunga.size
	 * 
	 * se la lista trovata è piu lunga => diventa la migliore
	 * 
	 *
	 * 
	 * 
	 * @param a
	 */
	
	public List<Artista> cercaPercorso(Artista a) {
		
		List<Artista> artistiParziale = new LinkedList<>();
		migliore = new LinkedList<>();
		
		this.dimensioneMax = migliore.size(); //0
		
		artistiParziale.add(a);
		ricorsiva(artistiParziale,a); //nessun livello
		
		return migliore;
	}
	
	private void ricorsiva(List<Artista> parziale, Artista a) {
		
		if(parziale.size()>this.dimensioneMax) {
			this.dimensioneMax = parziale.size();
			this.migliore= new LinkedList<>(parziale);
		}
		
		List<Artista> vicini = Graphs.neighborListOf(this.grafo, a );
		
		
		for(Artista temp : vicini) {
			
			
			if(parziale.size()==1) {
				pesoPrecedente = (int)this.grafo.getEdgeWeight(this.grafo.getEdge(a, temp));
			}
				
			int peso = (int)this.grafo.getEdgeWeight(this.grafo.getEdge(a, temp));
			
			
			if(! parziale.contains(temp) && peso==pesoPrecedente) {
				parziale.add(temp);
				ricorsiva(parziale, temp);
				
				//se faccio backtracking ma ci sono piu rami con stesso peso???
				parziale.remove(temp);
			}
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
