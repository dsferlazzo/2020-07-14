package it.polito.tdp.PremierLeague.model;

public class TeamReporter {
	
	private Team t;
	private int reporter;
	public TeamReporter(Team t, int reporter) {
		super();
		this.t = t;
		this.reporter = reporter;
	}
	public Team getT() {
		return t;
	}
	public int getReporter() {
		return reporter;
	}
	
	public void changeReporter(int n) {
		if(n==1)
			reporter++;
		else if(n==-1)
			reporter--;
	}

}
