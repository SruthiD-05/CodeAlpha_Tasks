import java.io.*;
import java.util.*;
class Stock {
    String symbol;
    double price;
    Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }
}
class Transaction {
    String type;
    String symbol;
    int quantity;
    double price;
    Transaction(String type, String symbol, int quantity, double price) {
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
    }
    public String toString() {
        return type + "," + symbol + "," + quantity + "," + price;
    }
}
class Portfolio {
    Map<String, Integer> holdings = new HashMap<>();
    List<Transaction> transactions = new ArrayList<>();
    double balance;
    double initialBalance;

    Portfolio(double balance) {
        this.balance = balance;
        this.initialBalance = balance;
    }
    void buy(Stock stock, int qty) {
        double cost = stock.price * qty;

        if (cost > balance) {
            System.out.println("Insufficient balance!");
            return;
        }
        balance -= cost;
        holdings.put(stock.symbol,
                holdings.getOrDefault(stock.symbol, 0) + qty);
        transactions.add(new Transaction("BUY", stock.symbol, qty, stock.price));
        System.out.println("Stock purchased successfully!");
    }
    void sell(Stock stock, int qty) {
        if (!holdings.containsKey(stock.symbol) ||
                holdings.get(stock.symbol) < qty) {
            System.out.println("Not enough shares!");
            return;
        }
        balance += stock.price * qty;
        holdings.put(stock.symbol,
                holdings.get(stock.symbol) - qty);
        transactions.add(new Transaction("SELL", stock.symbol, qty, stock.price));
        System.out.println("Stock sold successfully!");
    }
    void showPortfolio(Map<String, Stock> market) {
        System.out.println("\n===== PORTFOLIO =====");
        double totalValue = balance;
        System.out.println("Available Balance: ₹" + balance);
        for (String symbol : holdings.keySet()) {
            int qty = holdings.get(symbol);
            double value = market.get(symbol).price * qty;
            totalValue += value;
            System.out.println(symbol + " | Qty: " + qty + " | Value: ₹" + value);
        }
        double profitLoss = totalValue - initialBalance;
        System.out.println("Total Portfolio Value: ₹" + totalValue);
        System.out.println("Profit/Loss: ₹" + profitLoss);
    }
    void showTransactions() {
        System.out.println("\nTRANSACTION HISTORY\n");
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }
    void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("portfolio.txt"))) {
            writer.println(balance);
            for (String symbol : holdings.keySet()) {
                writer.println(symbol + "," + holdings.get(symbol));
            }
            writer.println("TRANSACTIONS");
            for (Transaction t : transactions) {
                writer.println(t.toString());
            }
            System.out.println("Portfolio saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }
    void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("portfolio.txt"))) {
            balance = Double.parseDouble(reader.readLine());
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("TRANSACTIONS")) break;
                String[] parts = line.split(",");
                holdings.put(parts[0], Integer.parseInt(parts[1]));
            }
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                transactions.add(new Transaction(
                        parts[0],
                        parts[1],
                        Integer.parseInt(parts[2]),
                        Double.parseDouble(parts[3])
                ));
            }
            System.out.println("Previous portfolio loaded!");
        } catch (IOException e) {
            System.out.println("No previous portfolio found. Starting fresh.");
        }
    }
}
public class StockTradingPlatform {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, Stock> market = new HashMap<>();
        market.put("TCS", new Stock("TCS", 3500));
        market.put("INFY", new Stock("INFY", 1500));
        market.put("WIPRO", new Stock("WIPRO", 500));
        market.put("HDFC", new Stock("HDFC", 1700));
        Portfolio portfolio = new Portfolio(1020030000);
        portfolio.loadFromFile();
        while (true) {
            System.out.println("\nSTOCK TRADING PLATFORM");
            System.out.println("1. View Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transactions");
            System.out.println("6. Save Portfolio");
            System.out.println("7. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("\n MARKET DATA \n");
                    for (Stock s : market.values()) {
                        System.out.println(s.symbol + " : ₹" + s.price);
                    }
                    break;
                case 2:
                    System.out.print("Enter Stock Symbol: ");
                    String buySymbol = sc.next().toUpperCase();
                    if (!market.containsKey(buySymbol)) {
                        System.out.println("Stock not found!");
                        break;
                    }
                    System.out.print("Enter Quantity: ");
                    int buyQty = sc.nextInt();
                    portfolio.buy(market.get(buySymbol), buyQty);
                    break;
                case 3:
                    System.out.print("Enter Stock Symbol: ");
                    String sellSymbol = sc.next().toUpperCase();
                    if (!market.containsKey(sellSymbol)) {
                        System.out.println("Stock not found!");
                        break;
                    }
                    System.out.print("Enter Quantity: ");
                    int sellQty = sc.nextInt();
                    portfolio.sell(market.get(sellSymbol), sellQty);
                    break;
                case 4:
                    portfolio.showPortfolio(market);
                    break;
                case 5:
                    portfolio.showTransactions();
                    break;
                case 6:
                    portfolio.saveToFile();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
