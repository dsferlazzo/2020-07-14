package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Simulatore {
	
	//PARAMETRI DI OUTPUT
	private double reporterMedi;
	private int partiteCritiche;
	
	//PARAMETRI DELLA SIMULAZIONE
	int N;	//NUMERO DI REPORTER INIZIALI PER OGNI SQUADRA
	int K;	//SOGLIA DI CRITICITA
	private List<Integer> repPartita;
	
	
	//STATO DEL MONDO
	private List<Match> partite;
	private List<TeamReporter> teams;
	private Map<Integer, Team> mapTeamID;
	private Model model;
	
	//CODA DEGLI EVENTI
	private PriorityQueue<Event> queue;
	
	
	public void init(List<Match> partite,List<Team> teams,Model model, int N, int K) {
		this.partiteCritiche=0;
		this.K = K;
		this.N = N;
		this.partite = partite;
		this.repPartita = new ArrayList<Integer>();
		this.model = model;
		this.teams = new ArrayList<TeamReporter>();
		
		this.mapTeamID = new HashMap<Integer, Team>();
		
		for(Team t : teams) {
			this.teams.add(new TeamReporter(t, N));
			this.mapTeamID.put(t.getTeamID(), t);
		}
		
		
		this.queue = new PriorityQueue<Event>();
		for(Match m : partite)
			this.queue.add(new Event(m));
		
	}
	
	public String run() {
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			this.handleEvent(e);
		}
		
		int somma = 0;
		for(Integer i : this.repPartita)
			somma+=i;
		this.reporterMedi = somma/this.repPartita.size();
		String result = "Simulazione di campionato effettuata\nPartitiche critiche: " + this.partiteCritiche
				+ "\nReporter medi per partita: " + this.reporterMedi;
		return result;
	}

	private void handleEvent(Event e) {
		Team vincente = this.mapTeamID.get(e.getTeamVincente());
		Team perdente = this.mapTeamID.get(e.getTeamPerdente());
		
		TeamReporter repV = null;
		TeamReporter repP = null;
		for(TeamReporter tr: this.teams) {
			if(tr.getT().equals(vincente))
				repV = tr;
			if(tr.getT().equals(perdente))
				repP = tr;
		}
		this.repPartita.add(repV.getReporter()+repP.getReporter());
		if(repV.getReporter()+repP.getReporter()<K)
			this.partiteCritiche++;
		
		if(!e.isPareggio()) {
			//HANDLE VITTORIA TEAM VINCENTE, HANDLE SCONFITTA TEAM PERDENTE
			this.handleVittoria(repV);
			this.handleSconfitta(repP);
		}
		
	}

	private void handleSconfitta(TeamReporter repP) {
		double random = Math.random();
		if(random<0.2) {
			repP.changeReporter(-1);
			//ORA DEVO CERCARE I TEAM PERDENTI, ED INCREMEMENTARE I LORO REPORTER
			List<Team> peggiori = this.model.getteamPeggiori(repP.getT());
			if(peggiori.size()==0)
				return;		//SE NON HO TEAM PEGGIORI, NON CAMBIO NULLA
			
			double random1 = Math.random()*peggiori.size();
			Team t = peggiori.get((int) random1);
			for(TeamReporter tr : this.teams) {
				if(tr.getT().equals(t))
					tr.changeReporter(1);
			}
		}
	}

	private void handleVittoria(TeamReporter repV) {
		double random = Math.random();
		if(random<0.5) {
			repV.changeReporter(-1);
			//ORA DEVO CERCARE I TEAM VINCENTI, ED INCREMEMENTARE I LORO REPORTER
			List<Team> migliori = this.model.getTeamMigliori(repV.getT());
			if(migliori.size()==0)
				return;		//SE NON HO TEAM MIGLIORI, NON CAMBIO NULLA
			
			double random1 = Math.random()*migliori.size();
			Team t = migliori.get((int) random1);
			for(TeamReporter tr : this.teams) {
				if(tr.getT().equals(t))
					tr.changeReporter(1);
			}
		}
		
	}
	

}
