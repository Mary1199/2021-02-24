package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private Graph<Player, DefaultWeightedEdge> grafo;
	private Map<Integer, Player> idMap;
	private PremierLeagueDAO dao;
	
	public Model() {
		dao = new PremierLeagueDAO();
		this.idMap = new HashMap<Integer, Player>();
		this.dao.listAllPlayers(idMap);
	}
	
	public List<Match> getMatches(){
		return dao.listAllMatches();
	}
	public void creaGrafo(Match match) {
		this.grafo = new SimpleDirectedWeightedGraph <>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.listAllGiocatori(match, idMap));
		
		for(Adiacenza a : dao.getAdiacenze(match, idMap)) {
			if(a.getPeso()>=0) {
				if(grafo.containsVertex(a.getV1()) && grafo.containsVertex(a.getV2())) {
					Graphs.addEdgeWithVertices(this.grafo, a.getV1(), a.getV2(), a.getPeso());
				}
			}else {
					if(grafo.containsVertex(a.getV1()) && grafo.containsVertex(a.getV2())) {
						Graphs.addEdgeWithVertices(this.grafo, a.getV2(), a.getV1(), (-1) * a.getPeso());
					}
			}
		}
	}
	
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Graph<Player,DefaultWeightedEdge> getGrafo() {
		return this.grafo;
	}
	
	public GiocatoreMigliore getMigliore() {
		if(grafo == null) {
			return null;
		}

		Player best = null;
		Double maxDelta = (double) Integer.MIN_VALUE;

		for(Player p : this.grafo.vertexSet()) {
			// calcolo la somma dei pesi degli archi uscenti
			double pesoUscente = 0.0;
			for(DefaultWeightedEdge edge : this.grafo.outgoingEdgesOf(p)) {
				pesoUscente += this.grafo.getEdgeWeight(edge);
			}

			// calcolo la somma dei pesi degli archi entranti
			double pesoEntrante = 0.0;
			for(DefaultWeightedEdge edge : this.grafo.incomingEdgesOf(p)) {
				pesoEntrante += this.grafo.getEdgeWeight(edge);
			}

			double delta = pesoUscente - pesoEntrante;
			if(delta > maxDelta) {
				best = p;
				maxDelta = delta;
			}
		}

		return new GiocatoreMigliore (best,maxDelta);
	}
}
