import java.time.Instant;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        DataRetriever dataRetriever = new DataRetriever();

        String orderReference = "CMD-001";
        Order order = dataRetriever.findOrderByReference(orderReference);
        if (order == null) {
            System.out.println("Aucune commande trouvée pour la référence : " + orderReference);
            return;
        }

        System.out.println("Commande trouvée : " + order);

        // 2) Exemple : tentative de modification de la commande
        try {
            // ici on simule une modification : changer la liste des plats
            List<DishOrder> dishOrderList = order.getDishOrderList();
            if (dishOrderList != null && !dishOrderList.isEmpty()) {
                DishOrder first = dishOrderList.get(0);
                first.setQuantity(first.getQuantity() + 1);
            }

            // On essaie de sauvegarder la commande modifiée
            // -> selon ta logique, saveOrder doit lever une exception
            //    si order.getPaymentStatus() == PAID
            Order savedOrder = dataRetriever.saveOrder(order);
            System.out.println("Commande modifiée et sauvegardée : " + savedOrder);

        } catch (RuntimeException e) {
            System.out.println("Erreur lors de la modification de la commande : " + e.getMessage());
        }

        // 3) Création d’une vente à partir d’une commande payée
        try {
            // on suppose que le statut de paiement a déjà été mis à jour
            // quelque part dans l’application, par exemple après encaissement

            Sale sale = createSaleFrom(order, dataRetriever);
            System.out.println("Vente créée : " + sale);

        } catch (RuntimeException e) {
            System.out.println("Erreur lors de la création de la vente : " + e.getMessage());
        }
    }

    /**
     * Création d’une vente à partir d’une commande.
     * - La commande doit être en statut PAID
     * - La commande ne doit pas déjà être associée à une vente
     */
    private static Sale createSaleFrom(Order order, DataRetriever dataRetriever) {
        // Vérifier le statut de paiement
        if (order.getPaymentStatus() != PaymentStatusEnum.PAID) {
            throw new RuntimeException(
                    "Une vente ne peut être créée que pour une commande payée."
            );
        }

        // Vérifier qu’aucune vente n’existe déjà pour cette commande
        if (saleExistsForOrder(order.getId(), dataRetriever)) {
            throw new RuntimeException(
                    "Une commande ne peut être associée qu'à une seule vente."
            );
        }

        // Créer l’objet Sale
        Sale sale = new Sale();
        sale.setCreationDatetime(Instant.now());
        sale.setOrder(order);

        // Sauvegarder la vente en base via une méthode que tu écriras dans DataRetriever
        return dataRetriever.saveSale(sale);
    }

    /**
     * Méthode utilitaire : demande à DataRetriever si une vente existe déjà
     * pour l’id de la commande.
     */
    private static boolean saleExistsForOrder(Integer orderId, DataRetriever dataRetriever) {
        return dataRetriever.saleExistsForOrder(orderId);
    }
}