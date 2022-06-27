package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Team, DefaultWeightedEdge> grafo;
	
	public List<Team> getAllTeams(){
		this.dao = new PremierLeagueDAO();
		return this.dao.listAllTeams();
	}
	
	public List<TeamScore> getTeamScores(){
		this.dao = new PremierLeagueDAO();
		return this.dao.getTeamScores();
	}
	
	public List<Match> getAllMatches(){
		this.dao = new PremierLeagueDAO();
		return this.dao.listAllMatches();
	}
	
	public String creaGrafo() {
		this.grafo = new SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//AGGIUNGO I VERTICI
		Graphs.addAllVertices(grafo, this.getAllTeams());
		
		//AGGIUNGO GLI ARCHI
		List<TeamScore> listTS = this.getTeamScores();
		for(TeamScore ts1 : listTS) {
			for(TeamScore ts2: listTS) {
				if(ts1.getT().getTeamID()!=ts2.getT().getTeamID() && ts1.getScore()>ts2.getScore())
					Graphs.addEdgeWithVertices(grafo, ts1.getT(), ts2.getT(), ts1.getScore()-ts2.getScore());
			}
		}
		
		String result = "Grafo creato\n#VERTICI: " + this.grafo.vertexSet().size() + "\n#ARCHI: " + this.grafo.edgeSet().size();
		return result;
		
	}
	
	public String getClassifica(Team t) {
		
		if(this.grafo==null)
			return "Il grafo non è ancora stato creato";
		
		List<TeamScore> listTS = this.getTeamScores();
		List<TeamScore> migliori = new ArrayList<TeamScore>();
		List<TeamScore> peggiori = new ArrayList<TeamScore>();
		int score=0;
		for(TeamScore ts: listTS) {
			if(ts.getT().equals(t))
				score=ts.getScore();
		}
		for(TeamScore ts : listTS)
			if(!t.equals(ts.getT())) {
				if(score>ts.getScore())
					peggiori.add(new TeamScore(ts.getT(), score-ts.getScore()));
				else if(score<ts.getScore())
					migliori.add(new TeamScore(ts.getT(), ts.getScore()-score));
			}
		
		Collections.sort(migliori);
		Collections.sort(peggiori);
		
		String result = "TEAM MIGLIORI:\n";
		for(TeamScore ts : migliori) {
			result += ts.getT() + " (" + ts.getScore() + ")\n";
		}
		result += "\nTEAM PEGGIORI:\n";
		for(TeamScore ts : peggiori) {
			result += ts.getT() + " (" + ts.getScore() + ")\n";
		}
		
		return result;
	}
	
	public List<Team> getTeamMigliori(Team t){
		List<TeamScore> listTS = this.getTeamScores();
		List<Team> migliori = new ArrayList<Team>();
		int score=0;
		for(TeamScore ts: listTS) {
			if(ts.getT().equals(t))
				score=ts.getScore();
		}
		for(TeamScore ts : listTS)
			if(!t.equals(ts.getT())) {
				if(score<ts.getScore())
					migliori.add(ts.getT());
			}
		return  migliori;
	}
	
	public List<Team> getteamPeggiori(Team t){
		List<TeamScore> listTS = this.getTeamScores();
		List<Team> peggiori = new ArrayList<Team>();
		int score=0;
		for(TeamScore ts: listTS) {
			if(ts.getT().equals(t))
				score=ts.getScore();
		}
		for(TeamScore ts : listTS)
			if(!t.equals(ts.getT())) {
				if(score>ts.getScore())
					peggiori.add(ts.getT());
			}
		return  peggiori;
	}
	
	public String effettuaSimulazione(int N, int K) {
		Simulatore s = new Simulatore();
		s.init(getAllMatches(), getAllTeams(), this, N, K);
		
		return s.run();
	}
	
}
