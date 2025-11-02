package ticketmachine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

/**
 * Classe de test pour la classe TicketMachine.
 * On vérifie ici que les principales fonctionnalités marchent bien.
 */
class TicketMachineTest {

	private static final int PRICE = 50; // Une constante pour le prix du ticket
	private TicketMachine machine;       // L'objet à tester

	@BeforeEach
	public void setUp() {
		// On initialise la machine avant chaque test
		machine = new TicketMachine(PRICE);
	}

	@Test
	// On vérifie que le prix affiché correspond au paramètre passé lors de
	// l'initialisation
	// S1 : le prix affiché correspond à l’initialisation.
	void priceIsCorrectlyInitialized() {
		// Paramètres : valeur attendue, valeur réelle, message si erreur
		assertEquals(PRICE, machine.getPrice(), "Initialisation incorrecte du prix");
	}

	@Test
	// S2 : la balance change quand on insère de l’argent
	void insertMoneyChangesBalance() {
		// GIVEN : une machine vierge (initialisée dans @BeforeEach)
		// WHEN : on insère de l'argent
		machine.insertMoney(10);
		machine.insertMoney(20);
		// THEN : la balance doit être la somme des montants insérés
		assertEquals(10 + 20, machine.getBalance(), "La balance n'est pas correctement mise à jour");
	}

	@Test
	// S3 : on ne peut pas insérer un montant négatif
	void cannotInsertNegativeAmount() {
		// WHEN et THEN : on s'attend à une exception
		Exception e = assertThrows(IllegalArgumentException.class, () -> {
			machine.insertMoney(-10);
		}, "Un montant négatif ne devrait pas être accepté");
		assertTrue(e.getMessage().contains("positif"));
	}

	@Test
	// S4 : on n’imprime pas le ticket si le montant est insuffisant
	void noPrintIfNotEnoughMoney() {
		machine.insertMoney(PRICE - 5); // On met un peu moins que le prix
		boolean result = machine.printTicket();
		assertFalse(result, "Le ticket ne devrait pas être imprimé si le montant est insuffisant");
	}

	@Test
	// S5 : on imprime le ticket si la somme est suffisante
	void printIfEnoughMoney() {
		machine.insertMoney(PRICE);
		boolean result = machine.printTicket();
		assertTrue(result, "Le ticket devrait être imprimé avec le bon montant");
	}

	@Test
	// S6 : après impression, la balance doit être décrémentée du prix du ticket
	void balanceDecreasesAfterPrinting() {
		machine.insertMoney(80); // plus que le prix
		machine.printTicket();   // imprime un ticket à 50
		assertEquals(30, machine.getBalance(), "La balance devrait être réduite après impression");
	}

	@Test
	// S7 : le total doit s’incrémenter seulement après impression
	void totalUpdatesOnlyAfterPrinting() {
		int totalBefore = machine.getTotal();
		machine.insertMoney(PRICE);
		assertEquals(totalBefore, machine.getTotal(), "Le total ne doit pas changer avant impression");

		machine.printTicket();
		assertEquals(totalBefore + PRICE, machine.getTotal(), "Le total doit augmenter après impression");
	}

	@Test
	// S8 : le remboursement doit rendre le bon montant et remettre la balance à 0
	void refundResetsBalanceAndReturnsCorrectAmount() {
		machine.insertMoney(70);
		int refund = machine.refund();

		assertEquals(70, refund, "Le montant remboursé doit être correct");
		assertEquals(0, machine.getBalance(), "La balance doit être réinitialisée après remboursement");
	}

	@Test
	// S9 : on ne peut pas créer une machine avec un prix négatif
	void cannotCreateMachineWithNegativePrice() {
		assertThrows(IllegalArgumentException.class, () -> {
			new TicketMachine(-20);
		}, "Le prix du ticket ne peut pas être négatif");
	}
}
