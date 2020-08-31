package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {

	private EventsDao dao;
	private Graph<String, DefaultWeightedEdge> grafo;
	private List<String> vertici;
	private List<Adiacenza> adiacenze;
	
	public Model() {
		this.dao = new EventsDao();
	}
	
	public List<String> getCategorie(){
		return this.dao.getCategorie();
	}
	
	public List<Integer> getGiorni(){
		return this.dao.getGiorni();
	}
	
	public void creaGrafo(String categoria, Integer giorno) {
		this.grafo = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.vertici = this.dao.getVertici(categoria, giorno);
		Graphs.addAllVertices(this.grafo, vertici);
		this.adiacenze = this.dao.getAdiacenze(categoria, giorno);
		for(Adiacenza a : this.adiacenze) {
			if(this.grafo.containsVertex(a.getS1()) && this.grafo.containsVertex(a.getS2()) && a.getPeso() != null) {
				Graphs.addEdgeWithVertices(this.grafo, a.getS1(), a.getS2(), a.getPeso());
			}
		}
		System.out.println("Grafo creato con "+ this.grafo.vertexSet().size()+" vertici e con "+ this.grafo.edgeSet().size()+" archi\n");
	}
	
	public int numVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int numArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Double getMediano() {
		Double max = 0.0;
		Double min = 0.0;
		double result = 0.0 ;
		for(DefaultWeightedEdge edge1 : this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(edge1) > max) {
				max = this.grafo.getEdgeWeight(edge1);
			} else if(this.grafo.getEdgeWeight(edge1) < min) {
				min = this.grafo.getEdgeWeight(edge1);
			}
		}
		result = (max+min)/2;
		System.out.println(result);
		return result;
		
	}
		
	public List<Adiacenza> getResult(){
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		for(DefaultWeightedEdge edge : this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(edge) < this.getMediano()) {
				result.add(new Adiacenza(this.grafo.getEdgeSource(edge),this.grafo.getEdgeTarget(edge),this.grafo.getEdgeWeight(edge)));
			}
		}
		return result;
	}
	
	
	
	
}
