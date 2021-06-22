package it.polito.tdp.PremierLeague.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {

	private PremierLeagueDAO dao;
	private Map<Integer, Player> idMap;
	private Graph<Player, DefaultWeightedEdge> grafo;
	
	public Model() {
		dao = new PremierLeagueDAO();
		
	}
	
	public void creaGrafo(double minG) {
		
		idMap = new HashMap<>();
		dao.listAllPlayers(idMap, minG);
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		for(Player p: idMap.values()) {
			grafo.addVertex(p);
		}
		
		List<Adiacenza> result = dao.getAdiacenze(idMap);
		for(Adiacenza a: result) {
			if(a.getPeso() > 0) {
				Graphs.addEdge(grafo, a.getP1(), a.getP2(), a.getPeso());
			}else {
				Graphs.addEdge(grafo, a.getP2(), a.getP1(), (-1)*a.getPeso());
			}
		}
	}
	
	public int getNVertici() {
		return grafo.vertexSet().size();
	}
	
	public int getNEdges() {
		return grafo.edgeSet().size();
	}
	

	private List<Adiacenza> battuti = null;
	public Player getTopPlayer() {
		
		battuti = new ArrayList<>();
		if(grafo != null) {
			Player migliore = null;
			int numAvversariMax = 0;
			
			for(Player p: grafo.vertexSet()) {
				if(grafo.outDegreeOf(p) > numAvversariMax) {
					migliore = p;
					numAvversariMax = grafo.outDegreeOf(p);
					
					for(DefaultWeightedEdge de: grafo.outgoingEdgesOf(p))
						battuti.add(new Adiacenza(p, Graphs.getOppositeVertex(grafo, de, p), grafo.getEdgeWeight(de)));
				}
			}
			
			return migliore;
		}else
			return null;
	}
	
	public List<Adiacenza> getBattuti(){
		if(battuti != null) {
			Collections.sort(battuti);
			return battuti;
		}
		
		return null;
	}
	
	private List<Player> squadraMigliore;
	private int sommaTitolarita = 0;
	
	public List<Player> getDreamTeam(int k){
		List<Player> parziale = new ArrayList<>();
		
		squadraMigliore = new ArrayList<>();
		
		cerca(k, parziale, new ArrayList<Player>(grafo.vertexSet()), 0);
		
		return squadraMigliore;
	}
	
	public void cerca(int k, List<Player> parziale, List<Player> players, int somma) {
		if(parziale.size() == k) {
			
			if(somma > sommaTitolarita) {
				squadraMigliore = new ArrayList<>(parziale);
				sommaTitolarita = somma;
			}
			return;
		}
		
		for(Player p: players) {
			
			int pesiUscenti = 0;
			int pesiEntranti = 0;
			
			if(parziale.isEmpty() || !parziale.contains(p)) {
				pesiUscenti = calcolaSommaPesiUscenti(p);
				pesiEntranti = calcolaSommaPesiEntranti(p);
				
				if(pesiUscenti > pesiEntranti) {
					parziale.add(p);
					
					List<Player> remains = new ArrayList<>(players);
					remains.removeAll(Graphs.successorListOf(grafo, p));
					somma += pesiUscenti - pesiEntranti;
					cerca(k, parziale, remains, somma);
					
					somma -= pesiUscenti - pesiEntranti;
					parziale.remove(p);
				}
			}
			
		}
	}

	private int calcolaSommaPesiEntranti(Player p) {
		
		int somma = 0;
		for(DefaultWeightedEdge de: grafo.incomingEdgesOf(p)) {
			somma += grafo.getEdgeWeight(de);
		}
		
		return somma;
	}

	private int calcolaSommaPesiUscenti(Player p) {
		int somma = 0;
		for(DefaultWeightedEdge de: grafo.outgoingEdgesOf(p)) {
			somma += grafo.getEdgeWeight(de);
		}
		
		return somma;
	}
}
