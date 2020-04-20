package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> esami;
	private double bestMedia = 0;
	private Set<Esame> bestSoluzione = null;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.esami = dao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		Set<Esame> parziale = new HashSet<>();
		
		cerca1(parziale, 0, numeroCrediti);
		
		return bestSoluzione;
	}

	/* 
	 * Approccio 1
	 * Complessita' pari a 2^N, perche' le possibilita'
	 * sono "metto" o "non metto" l'esame, elevato al numero
	 * di esami presenti
	 */
	private void cerca1(Set<Esame> parziale, int L, int m) {
		
		// casi di terminazione
		
		int crediti = sommaCrediti(parziale);
		if(crediti > m)
			return;
		
		if(crediti==m) {
			double media = calcolaMedia(parziale);
			if(media > bestMedia) {
				bestSoluzione = new HashSet(parziale);
				bestMedia = media;
			}
		}
		
		// sicuramente crediti < m
		
		if(L==esami.size())
			return;
		
		// generiamo i sotto-problemi
		// esame(L) e' da aggiungere o no? Provo entrambe le cose
		
		// Provo ad aggiungerlo
		parziale.add(esami.get(L));
		cerca1(parziale, L+1, m);
		parziale.remove(esami.get(L));
		
		// Provo a non aggiungerlo
		cerca1(parziale, L+1, m);
		
	}

	public double calcolaMedia(Set<Esame> parziale) {
		int somma = 0;
		int crediti = 0;
		
		for(Esame e : parziale) {
			somma += e.getCrediti()*e.getVoto();
			crediti += e.getCrediti();
		}
		return somma/crediti;
	}

	private int sommaCrediti(Set<Esame> parziale) {
		int somma = 0;
		for(Esame e : parziale)
			somma += e.getCrediti();
		return somma;
	}
	
	/* 
	 * Approccio 2
	 * Complessita' pari a N!, perche' N esami da disporre
	 * in tutte le combinazioni possibili
	 */
	private void cerca2(Set<Esame> parziale, int L, int m) {
		
		int crediti = sommaCrediti(parziale);
		if(crediti > m)
			return;
		
		if(crediti==m) {
			double media = calcolaMedia(parziale);
			if(media > bestMedia) {
				bestSoluzione = new HashSet(parziale);
				bestMedia = media;
			}
		}
		
		if(L==esami.size())
			return;
		
		for(Esame e : esami) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca2(parziale, L+1, m);
				parziale.remove(e);
			}
		}
	}
}
