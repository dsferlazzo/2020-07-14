package it.polito.tdp.PremierLeague.model;

public class TeamScore implements Comparable<TeamScore> {
	
	private Team t;
	private int score;
	public TeamScore(Team t, int score) {
		super();
		this.t = t;
		this.score = score;
	}
	public Team getT() {
		return t;
	}
	public int getScore() {
		return score;
	}
	@Override
	public int compareTo(TeamScore o) {
		return this.getScore()-o.getScore();
	}
	
	

}
