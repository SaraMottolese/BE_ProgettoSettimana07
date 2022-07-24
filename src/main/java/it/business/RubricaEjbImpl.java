package it.business;

import it.data.Contatto;
import it.data.Telefono;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import openejb.shade.org.apache.xpath.operations.String;

/**
 * Session Bean implementation class rubricaEjb
 */
@Stateless
@LocalBean
public class RubricaEjbImpl implements RubricaEjbRemote, RubricaEjbLocal {
	@PersistenceContext(name = "corsoEpicodeProgettoSettimana7")
	EntityManager em;

	/**
	 * Default constructor.
	 */
	public RubricaEjbImpl() {
		// TODO Auto-generated constructor stub
	}

	// co questo metodo andiamo ad inserire un nuovo contatto hgrazie al persist
	public void inserisci(Contatto e) {
		em.persist(e);
	}

	// ricerca del contatto dal cognome con una dynamic query
	public Contatto getContattoByCognome(String cognome) {
		Contatto contatto = new Contatto();
		Query query = em.createQuery("SELECT c FROM Contatto c WHERE c.cognome like :cognome");
		query.setParameter("cognome", "%" + cognome + "%");
		contatto = (Contatto) query.getSingleResult();
		return contatto;
	}

	// ricerca del contatto dal numero
	public Contatto getContattoByNumero(String numero) {
		Contatto contatto = new Contatto();
		Telefono numeroTel = new Telefono();
		numeroTel = em.find(Telefono.class, numero);

		contatto = numeroTel.getContatto();
		return contatto;
	}

	// il contatto viene richiamato grazie all'id
	public Contatto getContattoByID(Long id) {
		Contatto contatto = new Contatto();
		contatto = em.find(Contatto.class, id);

		return contatto;
	}

	// metodo per prendere tutti i contatti all'interno della rubrica
	@SuppressWarnings("unchecked")
	public List<Contatto> getAllContatti() {
		Query queryCont = em.createQuery("SELECT c FROM Contatto c");
		List<Contatto> contatti = queryCont.getResultList();
		return contatti;
	}

	// metodo per eliminare il contatto
	public void elimina(Long id) {
		Contatto contatto = new Contatto();
		contatto = getContattoByID(id);
		em.remove(contatto);
	}

	// metodo per eliminare i numeri associati ad un contatto
	public int eliminaNumPerContatto(Contatto contatto) {

		int numeroElimina = 0;
		for (Telefono numero : contatto.getNumTelefoni()) {

			Query query = em.createQuery("DELETE FROM NumTelefono t where  t.numTelefono like :numero");
			query.setParameter("numero", numero.getNumTelefono());
			numeroElimina = query.executeUpdate();
		}

		return numeroElimina;

	}

	// metodo che modifica le info del contatto
	public Contatto update(Contatto contatto) {
		eliminaNumPerContatto(getContattoByID(contatto.getId()));

		return em.merge(contatto);
	}

}
