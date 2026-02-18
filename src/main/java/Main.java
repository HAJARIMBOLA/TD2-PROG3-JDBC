import java.time.Instant;

public class Main {

    public static void main(String[] args) {

        DataRetriever dataRetriever = new DataRetriever();

        try {

            System.out.println("TEST STOCK VALUE");
            StockValue sv = dataRetriever.getStockValueAt(Instant.now(), 1);
            System.out.println("Quantité actuelle : " + sv.getQuantity() + " " + sv.getUnit());


            System.out.println("TEST DISH COST");
            double cost = dataRetriever.getDishCost(1);
            System.out.println("Coût du plat ID=1 : " + cost);


            System.out.println("TEST GROSS MARGIN");
            double margin = dataRetriever.getGrossMargin(1);
            System.out.println("Marge brute du plat ID=1 : " + margin);


            System.out.println("STOCK STATISTICS PAR JOUR");
            dataRetriever.getStockStatistics(
                    "day",
                    Instant.parse("2026-01-01T00:00:00Z"),
                    Instant.parse("2026-01-05T23:59:59Z")
            );


            System.out.println("STOCK STATISTICS PAR SEMAINE");
            dataRetriever.getStockStatistics(
                    "week",
                    Instant.parse("2026-01-01T00:00:00Z"),
                    Instant.parse("2026-01-31T23:59:59Z")
            );


            System.out.println("STOCK VALUE À UNE DATE");
            StockValue sv2 = dataRetriever.getStockValueAt(
                    Instant.parse("2026-01-06T12:00:00Z"),
                    1
            );
            System.out.println("Quantité à la date donnée : " + sv2.getQuantity() + " " + sv2.getUnit());


            System.out.println("STOCK STATISTICS PAR MOIS");
            dataRetriever.getStockStatistics(
                    "month",
                    Instant.parse("2026-01-01T00:00:00Z"),
                    Instant.parse("2026-03-31T23:59:59Z")
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}