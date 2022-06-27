package it.polito.tdp.PremierLeague.model;


import java.time.LocalDateTime;

public class Event implements Comparable<Event> {
	
	private int teamVincente;
	private int teamPerdente;
	private  LocalDateTime date;
	private boolean pareggio;
	
	public Event(Match m) {
		this.teamPerdente = 0;
		this.teamVincente = 0;
		pareggio = false;
		this.date = m.getDate();
		if(m.getResultOfTeamHome()==1) {
			this.teamVincente = m.getTeamHomeID();
			this.teamPerdente = m.getTeamAwayID();
		}
		else if(m.getResultOfTeamHome()==-1) {
			this.teamPerdente = m.getTeamHomeID();
			this.teamVincente = m.getTeamAwayID();
		}
		else {
			pareggio = true;
			this.teamVincente = m.getTeamHomeID();
			this.teamPerdente = m.getTeamAwayID();
		}
	}
	
	public int getTeamVincente() {
		return teamVincente;
	}

	public int getTeamPerdente() {
		return teamPerdente;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public boolean isPareggio() {
		return pareggio;
	}

	@Override
	public int compareTo(Event o) {
		return this.getDate().compareTo(o.getDate());
	}
	
	

}
