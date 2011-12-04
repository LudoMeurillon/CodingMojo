package fr.codingmojo;

import static fr.codingmojo.mockito.CodingMojoMatchers.some;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestMatcher {
	
	public enum Status{
		FRAUDEUR, HONNETE, NON_CONTROLE;
	}

	public class Personne{
		private String 	nom;
		public 	String 	prenom;
		private Integer _age;
		
		public Personne(String prenom, String nom, Integer age){
			this.nom = nom;
			this.prenom = prenom;
			this._age = age;
		}
		
		private Integer getAge(){
			return _age;
		}
	}
	
	public interface Train{
		Status checkin(Personne personne);
	}
	
	@Mock Train train;
	
	@Test
	public void test(){
		doReturn(Status.HONNETE		).when(train).checkin(some(Personne.class).with("prenom").eq("Ludovic"));
		doReturn(Status.FRAUDEUR	).when(train).checkin(some(Personne.class).with("nom").eq("Sarkozy"));
		doReturn(Status.FRAUDEUR	).when(train).checkin(some(Personne.class).with("getAge").eq(67));
		doReturn(Status.NON_CONTROLE).when(train).checkin(some(Personne.class).with("getAge").isNull());
		doReturn(Status.NON_CONTROLE).when(train).checkin(some(Personne.class).with("nom").isNull());
		
		assertEquals(Status.FRAUDEUR, 		train.checkin(new Personne("Nicolas", "Sarkozy",   55)));
		assertEquals(Status.HONNETE,  		train.checkin(new Personne("Ludovic", "Meurillon", 27)));
		assertEquals(Status.NON_CONTROLE, 	train.checkin(new Personne("Michel", "Sardou",   null)));
		assertEquals(Status.NON_CONTROLE, 	train.checkin(new Personne("Michel", null,   67)));
		assertNull(train.checkin(new Personne("Lucky","Luke",150)));
		assertNull(train.checkin(null));
	}
}
