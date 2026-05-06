import java.util.*;

// ╔══════════════════════════════════════════════════════════════════════╗
//  Q1 — FOOD DELIVERY PLATFORM
//  Paste into: https://onlinegdb.com/online_java_compiler → Run
//
//  TASKS:
//  Task 1 (Bug Fix) : getOrderStatistics()  — missing else block
//  Task 2           : getRevenuePerRestaurant()
//  Task 3           : getAverageDeliveryDistancePerCustomer()
//  Task 4           : getHighlyActiveCustomers()
// ╚══════════════════════════════════════════════════════════════════════╝

// ── ENUM: OrderStatus ──────────────────────────────────────────────────
// "I am defining an enum — a fixed set of named constants.
//  It represents every possible state an order can be in."
enum OrderStatus {
    PLACED,            // "Customer just placed the order — this is ACTIVE"
    PREPARING,         // "Restaurant is cooking — this is ACTIVE"
    OUT_FOR_DELIVERY,  // "Delivery agent picked it up — this is ACTIVE"
    DELIVERED,         // "Customer received it — this is CLOSED"
    CANCELED           // "Order was canceled — this is CLOSED"
}

// ── CLASS: Order ───────────────────────────────────────────────────────
// "Order is a data class — it just holds information about one order,
//  like one row in a database table."
class Order {
    int orderId;        // "Unique ID for this order"
    int restaurantId;   // "Which restaurant made it"
    int customerId;     // "Which customer placed it"
    double orderValue;  // "How much money this order is worth"
    double distanceKm;  // "Delivery distance in kilometres"
    OrderStatus status; // "Current state — one of the enum values"

    // "Constructor stores all 6 values when we write new Order(...)"
    Order(int orderId, int restaurantId, int customerId,
          double orderValue, double distanceKm, OrderStatus status) {
        this.orderId      = orderId;      // "this.X = the field; X (right side) = the parameter"
        this.restaurantId = restaurantId;
        this.customerId   = customerId;
        this.orderValue   = orderValue;
        this.distanceKm   = distanceKm;
        this.status       = status;
    }
}

// ── CLASS: OrderStats ──────────────────────────────────────────────────
// "A simple container to return 3 numbers from Task 1 in one object.
//  Instead of returning 3 separate values I wrap them here."
class OrderStats {
    int totalOrders;  // "Count of ALL orders"
    int activeOrders; // "Count of PLACED + PREPARING + OUT_FOR_DELIVERY"
    int closedOrders; // "Count of DELIVERED + CANCELED"

    OrderStats(int totalOrders, int activeOrders, int closedOrders) {
        this.totalOrders  = totalOrders;
        this.activeOrders = activeOrders;
        this.closedOrders = closedOrders;
    }
}

// ── CLASS: OrderManager ────────────────────────────────────────────────
// "The main class. Stores all orders and provides 4 query methods."
class OrderManager {

    // "ArrayList is a dynamic list — it grows as we add more orders"
    List<Order> orders = new ArrayList<>();

    // "addOrder simply appends one order to the end of the list"
    void addOrder(Order order) {
        orders.add(order); // ".add() appends to the end of the ArrayList"
    }

    // "updateOrderStatus finds the order by ID and changes its status"
    void updateOrderStatus(int orderId, OrderStatus newStatus) {
        for (Order o : orders) {            // "loop through every order"
            if (o.orderId == orderId) {     // "find the one with matching ID"
                o.status = newStatus;       // "update its status"
                return;                    // "stop looping — job done"
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 1 — BUG FIX: getOrderStatistics()
    //
    //  SAY: "The bug was the missing else block. The active counter worked
    //        correctly but closed was never incremented — it stayed 0.
    //        The fix is adding else — anything not active must be closed."
    // ════════════════════════════════════════════════════════════════════
    OrderStats getOrderStatistics() {

        // "orders.size() gives me the total count directly — no loop needed"
        int total = orders.size();

        int active = 0; // "initialise both counters at 0 before the loop"
        int closed = 0;

        // "I loop through every order one by one to check its status"
        for (Order o : orders) {

            // "I use == for enum comparison — not .equals(). This is correct in Java."
            // "I check all three active statuses in one if condition"
            if (o.status == OrderStatus.PLACED
             || o.status == OrderStatus.PREPARING
             || o.status == OrderStatus.OUT_FOR_DELIVERY) {

                active++; // "this order is active — increment the active counter by 1"

            } else {
                // "BUG FIX — this else block was missing in the original code"
                // "Anything not active MUST be DELIVERED or CANCELED — both closed"
                closed++; // "so I increment the closed counter here"
            }
        }

        // "I wrap all 3 results in an OrderStats object and return it"
        // "This is the return type the method signature requires"
        return new OrderStats(total, active, closed);
    }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 2 — getRevenuePerRestaurant()
    //
    //  SAY: "I use getOrDefault instead of get() because the first time
    //        I see a restaurant, the key does not exist yet. get() returns
    //        null and adding to null crashes. getOrDefault returns 0.0
    //        safely as the starting value."
    // ════════════════════════════════════════════════════════════════════
    Map<Integer, Double> getRevenuePerRestaurant() {

        // "I create an empty HashMap to accumulate revenue per restaurant"
        // "Key = restaurantId, Value = running total of revenue"
        Map<Integer, Double> revenue = new HashMap<>();

        // "I loop through every order in the system"
        for (Order o : orders) {

            // "FILTER: only DELIVERED orders count toward revenue"
            // "CANCELED and active orders are completely skipped"
            if (o.status == OrderStatus.DELIVERED) {

                // "getOrDefault(key, 0.0): returns existing total if key exists,
                //  or 0.0 if this restaurant is being seen for the first time"
                // "I add this order's value and put the updated total back"
                revenue.put(
                    o.restaurantId,
                    revenue.getOrDefault(o.restaurantId, 0.0) + o.orderValue
                );
            }
            // "Non-DELIVERED orders — the if is false — we skip them entirely"
        }

        // "Return the completed map e.g. {10: 80.0, 11: 40.0}"
        return revenue;
    }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 3 — getAverageDeliveryDistancePerCustomer()
    //
    //  SAY: "I use two maps — sumMap and countMap — because I cannot
    //        calculate a running average in one pass. I need both the
    //        total and the count. I loop over sumMap.keySet() at the end,
    //        not all customers, so customers with no delivered orders
    //        automatically don't appear in the result."
    // ════════════════════════════════════════════════════════════════════
    Map<Integer, Double> getAverageDeliveryDistancePerCustomer() {

        // "First map: running total of delivery distances per customer"
        Map<Integer, Double>  sumMap   = new HashMap<>();

        // "Second map: count of delivered orders per customer"
        // "I need BOTH to calculate average = sum / count"
        Map<Integer, Integer> countMap = new HashMap<>();

        // "Loop through every order"
        for (Order o : orders) {

            // "Only DELIVERED orders count — skip everything else"
            if (o.status == OrderStatus.DELIVERED) {

                // "Add this order's distance to the running sum for this customer"
                // "getOrDefault starts at 0.0 if first time seeing this customer"
                sumMap.put(o.customerId,
                    sumMap.getOrDefault(o.customerId, 0.0) + o.distanceKm);

                // "Increment the delivery count for this customer"
                countMap.put(o.customerId,
                    countMap.getOrDefault(o.customerId, 0) + 1);
            }
        }

        // "Build the final result map"
        Map<Integer, Double> result = new HashMap<>();

        // "sumMap.keySet() contains ONLY customers who had at least 1 delivered order"
        // "Customers with only CANCELED or active orders are automatically absent"
        for (int id : sumMap.keySet()) {
            // "Average = total distance divided by number of delivered orders"
            result.put(id, sumMap.get(id) / countMap.get(id));
        }

        // "Return e.g. {100: 4.0, 101: 2.0}"
        return result;
    }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 4 — getHighlyActiveCustomers()
    //
    //  SAY: "Notice there is NO status filter in the counting loop.
    //        The question says all statuses count including CANCELED.
    //        I only filter at the end — if count is 3 or more, the
    //        customer qualifies. Collections.sort sorts ascending in-place."
    // ════════════════════════════════════════════════════════════════════
    List<Integer> getHighlyActiveCustomers() {

        // "A map to count total orders per customer — key = customerId"
        Map<Integer, Integer> countMap = new HashMap<>();

        // "NO status filter — ALL statuses count, even CANCELED"
        for (Order o : orders) {
            // "Increment this customer's total order count by 1"
            countMap.put(o.customerId,
                countMap.getOrDefault(o.customerId, 0) + 1);
        }

        // "Empty list to hold qualifying customer IDs"
        List<Integer> result = new ArrayList<>();

        // "entrySet() gives each key-value pair as a Map.Entry object"
        // "getKey() = customerId, getValue() = total order count"
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {

            // "Check if this customer placed 3 or more orders in total"
            if (entry.getValue() >= 3) {
                result.add(entry.getKey()); // "add their ID to the result list"
            }
        }

        // "Sort the list ascending — the question asks for a sorted list"
        // "Collections.sort modifies the list in-place"
        Collections.sort(result);

        // "Return e.g. [100, 101]"
        return result;
    }
}

// ── MAIN CLASS: runs all 4 tests ───────────────────────────────────────
public class Q1_FoodDelivery_Commented {

    public static void main(String[] args) {
        testTask1_BugFix();
        testTask2_Revenue();
        testTask3_AvgDistance();
        testTask4_ActiveCustomers();
        System.out.println("\nAll 4 tasks PASSED!");
    }

    static void testTask1_BugFix() {
        System.out.println("Task 1 (Bug Fix): getOrderStatistics...");
        OrderManager om = new OrderManager();

        // "3 active + 2 closed orders"
        om.addOrder(new Order(1, 10, 100, 25.0, 3.2, OrderStatus.PLACED));           // active
        om.addOrder(new Order(2, 10, 101, 55.0, 1.4, OrderStatus.PREPARING));        // active
        om.addOrder(new Order(3, 11, 102, 15.0, 6.0, OrderStatus.OUT_FOR_DELIVERY)); // active
        om.addOrder(new Order(4, 11, 103, 40.0, 2.0, OrderStatus.DELIVERED));        // closed
        om.addOrder(new Order(5, 12, 104, 18.0, 4.5, OrderStatus.CANCELED));         // closed

        OrderStats s = om.getOrderStatistics();

        // "assert: if condition is false → throws AssertionError with message"
        assert s.totalOrders  == 5 : "total expected 5,  got " + s.totalOrders;
        assert s.activeOrders == 3 : "active expected 3, got " + s.activeOrders;
        assert s.closedOrders == 2 : "closed expected 2, got " + s.closedOrders;

        System.out.println("  total=" + s.totalOrders + "  active=" + s.activeOrders + "  closed=" + s.closedOrders + "  PASS");
    }

    static void testTask2_Revenue() {
        System.out.println("Task 2: getRevenuePerRestaurant...");
        OrderManager om = new OrderManager();

        om.addOrder(new Order(1, 10, 100, 25.0, 3.2, OrderStatus.DELIVERED));   // R10 gets 25
        om.addOrder(new Order(2, 10, 101, 55.0, 1.4, OrderStatus.DELIVERED));   // R10 gets 55 → total 80
        om.addOrder(new Order(3, 11, 102, 40.0, 2.0, OrderStatus.DELIVERED));   // R11 gets 40
        om.addOrder(new Order(4, 11, 103, 20.0, 5.0, OrderStatus.CANCELED));    // excluded
        om.addOrder(new Order(5, 12, 104, 18.0, 4.5, OrderStatus.PLACED));      // excluded

        Map<Integer, Double> rev = om.getRevenuePerRestaurant();

        // "Math.abs(a-b) < 0.01 is the safe way to compare doubles"
        assert Math.abs(rev.get(10) - 80.0) < 0.01 : "R10 expected 80.0";
        assert Math.abs(rev.get(11) - 40.0) < 0.01 : "R11 expected 40.0";
        assert !rev.containsKey(12) : "R12 should NOT appear";

        System.out.println("  R10=" + rev.get(10) + "  R11=" + rev.get(11) + "  R12 absent=" + !rev.containsKey(12) + "  PASS");
    }

    static void testTask3_AvgDistance() {
        System.out.println("Task 3: getAverageDeliveryDistancePerCustomer...");
        OrderManager om = new OrderManager();

        om.addOrder(new Order(1, 10, 100, 25.0, 3.0, OrderStatus.DELIVERED)); // C100: 3.0
        om.addOrder(new Order(2, 10, 100, 30.0, 5.0, OrderStatus.DELIVERED)); // C100: 5.0 → avg 4.0
        om.addOrder(new Order(3, 11, 101, 15.0, 2.0, OrderStatus.DELIVERED)); // C101: avg 2.0
        om.addOrder(new Order(4, 11, 102, 40.0, 6.0, OrderStatus.CANCELED));  // C102: excluded

        Map<Integer, Double> avg = om.getAverageDeliveryDistancePerCustomer();

        assert Math.abs(avg.get(100) - 4.0) < 0.01 : "C100 expected 4.0";
        assert Math.abs(avg.get(101) - 2.0) < 0.01 : "C101 expected 2.0";
        assert !avg.containsKey(102) : "C102 should NOT appear";

        System.out.println("  C100=" + avg.get(100) + "  C101=" + avg.get(101) + "  C102 absent=" + !avg.containsKey(102) + "  PASS");
    }

    static void testTask4_ActiveCustomers() {
        System.out.println("Task 4: getHighlyActiveCustomers...");
        OrderManager om = new OrderManager();

        // C100: 3 orders (qualifies), C101: 4 orders (qualifies), C102: 2 orders (doesn't)
        om.addOrder(new Order(1, 10, 100, 10.0, 1.0, OrderStatus.PLACED));
        om.addOrder(new Order(2, 10, 100, 20.0, 2.0, OrderStatus.CANCELED));
        om.addOrder(new Order(3, 10, 100, 30.0, 3.0, OrderStatus.DELIVERED));
        om.addOrder(new Order(4, 11, 101, 10.0, 1.0, OrderStatus.DELIVERED));
        om.addOrder(new Order(5, 11, 101, 10.0, 1.0, OrderStatus.DELIVERED));
        om.addOrder(new Order(6, 11, 101, 10.0, 1.0, OrderStatus.PLACED));
        om.addOrder(new Order(7, 11, 101, 10.0, 1.0, OrderStatus.PREPARING));
        om.addOrder(new Order(8, 12, 102, 10.0, 1.0, OrderStatus.DELIVERED));
        om.addOrder(new Order(9, 12, 102, 10.0, 1.0, OrderStatus.DELIVERED));

        List<Integer> result = om.getHighlyActiveCustomers();

        assert result.size() == 2    : "expected 2 customers";
        assert result.contains(100)  : "100 should be in result";
        assert result.contains(101)  : "101 should be in result";
        assert !result.contains(102) : "102 should NOT be in result";
        assert result.get(0) < result.get(1) : "should be sorted ascending";

        System.out.println("  Highly active (sorted): " + result + "  PASS");
    }
}


--------------------------------------------------------------------------------------------------



import java.util.*;

// ╔══════════════════════════════════════════════════════════════════════╗
//  Q2 — PAYMENT TRANSACTION MONITORING
//  Paste into: https://onlinegdb.com/online_java_compiler → Run
//
//  TASKS:
//  Task 1 (Bug Fix) : getBalance()  — missing accountId filter
//  Task 2           : getAverageTransactionAmountByAccount()
//  Task 3           : getTransactionFees()  — sort by timestamp is KEY
// ╚══════════════════════════════════════════════════════════════════════╝

// ── ENUM: TransactionType ──────────────────────────────────────────────
// "Two types of transactions — CREDIT increases balance, DEBIT decreases it"
enum TransactionType {
    CREDIT, // "money coming IN — balance goes UP"
    DEBIT   // "money going OUT — balance goes DOWN"
}

// ── CLASS: Transaction ─────────────────────────────────────────────────
// "Represents one movement of money in or out of an account"
class Transaction {
    int transactionId; // "unique ID for this transaction"
    int accountId;     // "which account this transaction belongs to"
    TransactionType type; // "CREDIT or DEBIT"
    double amount;     // "always positive in inputs"
    long timestampSec; // "when it happened — unix seconds — used for sorting"

    Transaction(int tid, int aid, TransactionType type, double amount, long ts) {
        this.transactionId = tid;
        this.accountId     = aid;
        this.type          = type;
        this.amount        = amount;
        this.timestampSec  = ts;
    }

    // "Getters — used by Comparator in Task 3 and fee logic"
    public int getAccountId()        { return accountId; }
    public TransactionType getType() { return type; }
    public double getAmount()        { return amount; }
    public long getTimestampSec()    { return timestampSec; }
}

// ── CLASS: AccountManager ──────────────────────────────────────────────
// "Main class — manages all transactions and provides 3 query methods"
class AccountManager {

    // "One shared list holding ALL transactions for ALL accounts"
    // "This is why the bug exists — no filter means all get summed together"
    List<Transaction> transactions = new ArrayList<>();

    // "addTransaction appends to the shared list"
    void addTransaction(Transaction tx) { transactions.add(tx); }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 1 — BUG FIX: getBalance(accountId)
    //
    //  SAY: "The bug is that the accountId parameter was completely ignored.
    //        Every call to getBalance summed ALL transactions in the system.
    //        So both accounts returned the same wrong number — the grand
    //        total of everything. The fix is one line — adding the filter
    //        if (tx.accountId == accountId) before processing each transaction."
    // ════════════════════════════════════════════════════════════════════
    public double getBalance(int accountId) {

        double balance = 0.0; // "start balance at zero"

        // "Loop through ALL transactions in the shared list"
        for (Transaction tx : transactions) {

            // "THE FIX — this line was missing in the original code"
            // "Only process transactions that belong to THIS specific account"
            // "Without this, Account 1 and Account 2 both get the same wrong value"
            if (tx.accountId == accountId) {

                if (tx.type == TransactionType.CREDIT) {
                    balance += tx.amount; // "CREDIT: money comes IN → balance goes UP"
                } else {
                    balance -= tx.amount; // "DEBIT: money goes OUT → balance goes DOWN"
                }
            }
            // "Transactions for OTHER accounts are skipped — the if is false"
        }

        return balance; // "return the balance for this specific account only"
    }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 2 — getAverageTransactionAmountByAccount()
    //
    //  SAY: "I use getOrDefault instead of get() because the first time
    //        I see an accountId the key does not exist yet. get() returns
    //        null and adding a number to null throws NullPointerException.
    //        getOrDefault returns 0.0 safely as the starting point."
    // ════════════════════════════════════════════════════════════════════
    public Map<Integer, Double> getAverageTransactionAmountByAccount() {

        // "First map: running total of amounts per account"
        // "Key = accountId, Value = sum of all transaction amounts"
        Map<Integer, Double>  sumMap   = new HashMap<>();

        // "Second map: count of transactions per account"
        // "I need BOTH to calculate average = sum / count"
        Map<Integer, Integer> countMap = new HashMap<>();

        // "Loop through every transaction — no type filter needed"
        // "Both CREDIT and DEBIT amounts count — amounts are always positive"
        for (Transaction tx : transactions) {

            // "Add this transaction's amount to the running sum for its account"
            // "getOrDefault: returns existing total OR 0.0 if first time seeing this account"
            sumMap.put(tx.getAccountId(),
                sumMap.getOrDefault(tx.getAccountId(), 0.0) + tx.getAmount());

            // "Increment the transaction count for this account"
            // "getOrDefault starts at 0 if first time"
            countMap.put(tx.getAccountId(),
                countMap.getOrDefault(tx.getAccountId(), 0) + 1);
        }

        // "Build the result map — average per account"
        Map<Integer, Double> result = new HashMap<>();

        // "I loop over sumMap.keySet() — ONLY accounts that had transactions"
        // "Accounts with no transactions never entered sumMap → automatically absent"
        for (int id : sumMap.keySet()) {
            // "Average = total amount ÷ number of transactions"
            result.put(id, sumMap.get(id) / countMap.get(id));
        }

        // "Return e.g. {1: 40.0, 2: 33.33}"
        return result;
    }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 3 — getTransactionFees()
    //
    //  SAY: "Before writing any fee logic I need to sort by timestamp.
    //        The question says process in chronological order — the input
    //        may NOT be in order. Account 3 is specifically designed to
    //        catch candidates who skip the sort. My 3 steps are:
    //        1) group by account  2) sort by timestamp  3) apply fees from index 3"
    // ════════════════════════════════════════════════════════════════════
    public Map<Integer, Double> getTransactionFees() {

        // "STEP 1: Group all transactions by accountId"
        // "Key = accountId, Value = list of transactions for that account"
        Map<Integer, List<Transaction>> txByAccount = new HashMap<>();

        for (Transaction tx : transactions) {
            // "computeIfAbsent: if key absent → create new ArrayList, put in map, return it"
            // "If key exists → return existing list"
            // "Then .add(tx) appends this transaction to whichever list was returned"
            txByAccount.computeIfAbsent(tx.getAccountId(), k -> new ArrayList<>()).add(tx);
        }

        // "Result map: {accountId → total fees charged}"
        Map<Integer, Double> fees = new HashMap<>();

        // "Process each account's transactions separately"
        for (Map.Entry<Integer, List<Transaction>> entry : txByAccount.entrySet()) {

            int accountId = entry.getKey();        // "the accountId for this group"
            List<Transaction> accountTxs = entry.getValue(); // "list of transactions for this account"

            // "STEP 2: SORT BY TIMESTAMP — this is CRITICAL"
            // "Transactions may be added out of chronological order"
            // "Comparator.comparingLong sorts by getTimestampSec() numerically"
            // "Transaction::getTimestampSec is a method reference — same as tx -> tx.getTimestampSec()"
            accountTxs.sort(Comparator.comparingLong(Transaction::getTimestampSec));

            // "STEP 3: Apply fee rules using index"
            double totalFee = 0.0;

            // "I use a regular for loop with index because I need the POSITION"
            // "Position determines if a transaction is free or billable"
            for (int i = 0; i < accountTxs.size(); i++) {

                // "Index 0, 1, 2 → FREE (first 3 transactions)"
                // "Index 3, 4, 5... → BILLABLE (from 4th onward)"
                if (i >= 3) {

                    Transaction tx = accountTxs.get(i); // "get the transaction at this index"

                    if (tx.getType() == TransactionType.CREDIT) {
                        totalFee += 1.0; // "CREDIT costs $1"
                    } else {
                        totalFee += 2.0; // "DEBIT costs $2"
                    }
                }
                // "If i < 3 → free, we skip to next iteration automatically"
            }

            // "Store this account's total fees in the result map"
            fees.put(accountId, totalFee);
        }

        // "Return e.g. {1: 3.0, 2: 2.0, 3: 1.0}"
        return fees;
    }
}

// ── MAIN: runs all 3 tests ─────────────────────────────────────────────
public class Q2_Payment_Commented {

    public static void main(String[] args) {
        testTask1_BugFix_basic();
        testTask1_BugFix_multipleAccounts();
        testTask2_AverageAmount();
        testTask3_Fees();
        System.out.println("\nAll tasks PASSED!");
    }

    static void assertAlmost(double expected, double actual, double eps) {
        // "helper method to safely compare doubles — exact equality fails due to floating point"
        assert Math.abs(expected - actual) <= eps :
            "Expected " + expected + " but got " + actual;
    }

    static void testTask1_BugFix_basic() {
        System.out.println("Task 1a (Bug Fix): getBalance basic...");
        AccountManager mgr = new AccountManager();

        // "Account 1: CREDIT 100, DEBIT 30, DEBIT 20, CREDIT 10 → 100-30-20+10 = 60"
        mgr.addTransaction(new Transaction(101, 1, TransactionType.CREDIT, 100.0, 1000));
        mgr.addTransaction(new Transaction(102, 1, TransactionType.DEBIT,   30.0, 1010));
        mgr.addTransaction(new Transaction(103, 1, TransactionType.DEBIT,   20.0, 1020));
        mgr.addTransaction(new Transaction(104, 1, TransactionType.CREDIT,  10.0, 1030));

        assertAlmost(60.0, mgr.getBalance(1), 0.0001);
        System.out.println("  balance=" + mgr.getBalance(1) + "  PASS");
    }

    static void testTask1_BugFix_multipleAccounts() {
        System.out.println("Task 1b (Bug Fix): getBalance multiple accounts...");
        AccountManager mgr = new AccountManager();

        // "TWO accounts mixed in one list — this is what exposes the bug"
        // "Account 1: +50 -10 = 40.0  |  Account 2: +80 -5.5 -14.5 = 60.0"
        mgr.addTransaction(new Transaction(201, 1, TransactionType.CREDIT,  50.0, 2000));
        mgr.addTransaction(new Transaction(202, 2, TransactionType.CREDIT,  80.0, 2005));
        mgr.addTransaction(new Transaction(203, 1, TransactionType.DEBIT,   10.0, 2010));
        mgr.addTransaction(new Transaction(204, 2, TransactionType.DEBIT,    5.5, 2015));
        mgr.addTransaction(new Transaction(205, 2, TransactionType.DEBIT,   14.5, 2020));

        assertAlmost(40.0, mgr.getBalance(1), 0.0001);
        assertAlmost(60.0, mgr.getBalance(2), 0.0001);
        System.out.println("  A1=" + mgr.getBalance(1) + "  A2=" + mgr.getBalance(2) + "  PASS");
    }

    static void testTask2_AverageAmount() {
        System.out.println("Task 2: getAverageTransactionAmountByAccount...");
        AccountManager mgr = new AccountManager();

        // "Account 1: (100+30+20+10)/4 = 40.0  |  Account 2: (80+5.5+14.5)/3 = 33.33"
        mgr.addTransaction(new Transaction(101, 1, TransactionType.CREDIT, 100.0, 1000));
        mgr.addTransaction(new Transaction(102, 1, TransactionType.DEBIT,   30.0, 1010));
        mgr.addTransaction(new Transaction(103, 1, TransactionType.DEBIT,   20.0, 1020));
        mgr.addTransaction(new Transaction(104, 1, TransactionType.CREDIT,  10.0, 1030));
        mgr.addTransaction(new Transaction(201, 2, TransactionType.CREDIT,  80.0, 2005));
        mgr.addTransaction(new Transaction(202, 2, TransactionType.DEBIT,    5.5, 2015));
        mgr.addTransaction(new Transaction(203, 2, TransactionType.DEBIT,   14.5, 2020));

        Map<Integer, Double> avg = mgr.getAverageTransactionAmountByAccount();

        assertAlmost(40.0,   avg.get(1), 0.01);
        assertAlmost(33.333, avg.get(2), 0.1);
        System.out.println("  A1 avg=" + avg.get(1) + "  A2 avg=" + String.format("%.2f", avg.get(2)) + "  PASS");
    }

    static void testTask3_Fees() {
        System.out.println("Task 3: getTransactionFees...");
        AccountManager mgr = new AccountManager();

        // "A1: 5 txns → first 3 free → DEBIT($2)+CREDIT($1) = $3"
        mgr.addTransaction(new Transaction(1, 1, TransactionType.CREDIT, 100.0, 1000));
        mgr.addTransaction(new Transaction(2, 1, TransactionType.DEBIT,   20.0, 1010));
        mgr.addTransaction(new Transaction(3, 1, TransactionType.CREDIT,  10.0, 1020));
        mgr.addTransaction(new Transaction(4, 1, TransactionType.DEBIT,    5.0, 1030)); // $2
        mgr.addTransaction(new Transaction(5, 1, TransactionType.CREDIT,   7.0, 1040)); // $1

        // "A2: 4 txns → first 3 free → DEBIT($2) = $2"
        mgr.addTransaction(new Transaction(6, 2, TransactionType.DEBIT,   50.0, 2000));
        mgr.addTransaction(new Transaction(7, 2, TransactionType.DEBIT,   10.0, 2010));
        mgr.addTransaction(new Transaction(8, 2, TransactionType.CREDIT,  20.0, 2020));
        mgr.addTransaction(new Transaction(9, 2, TransactionType.DEBIT,    5.0, 2030)); // $2

        // "A3: tx29 added LAST but timestamp=2005 is EARLIER than tx27(2010) and tx28(2020)"
        // "After sort: tx26(2000),tx29(2005),tx27(2010),tx28(2020)"
        // "index 3 = tx28 = CREDIT → $1"
        mgr.addTransaction(new Transaction(26, 3, TransactionType.DEBIT,  50.0, 2000));
        mgr.addTransaction(new Transaction(27, 3, TransactionType.DEBIT,  10.0, 2010));
        mgr.addTransaction(new Transaction(28, 3, TransactionType.CREDIT, 20.0, 2020)); // 4th after sort → $1
        mgr.addTransaction(new Transaction(29, 3, TransactionType.DEBIT,   5.0, 2005)); // sorts to 2nd

        Map<Integer, Double> fees = mgr.getTransactionFees();

        assertAlmost(3.0, fees.get(1), 0.0001);
        assertAlmost(2.0, fees.get(2), 0.0001);
        assertAlmost(1.0, fees.get(3), 0.0001);
        System.out.println("  A1=$" + fees.get(1) + "  A2=$" + fees.get(2) + "  A3=$" + fees.get(3) + "  PASS");
    }
}




-----------------------------------------------------------------------------------------------------------






import java.util.*;

// ╔══════════════════════════════════════════════════════════════════════╗
//  Q3 — GYM MEMBERSHIP SYSTEM
//  Paste into: https://onlinegdb.com/online_java_compiler → Run
//
//  TASKS:
//  Task 1 (Bug Fix) : Member.equals() — missing override
//  Task 2.1         : addWorkout()
//  Task 2.2         : getAverageWorkoutDurations()
//  Task 3           : getPaymentDues() + getDueBySatus()
// ╚══════════════════════════════════════════════════════════════════════╝

// ── ENUM: MembershipStatus ─────────────────────────────────────────────
// "Three tiers — BRONZE is free, SILVER and GOLD are paid memberships"
enum MembershipStatus {
    BRONZE, // "default tier — 1st workout free, $10/hr after"
    SILVER, // "paid tier — first 3 free, $8/hr after"
    GOLD    // "premium tier — first 5 free, $6/hr after"
}

// ── CLASS: Workout ─────────────────────────────────────────────────────
// "Represents one gym session. Times are in minutes from midnight."
class Workout {
    private int id;        // "unique ID for this workout session"
    private int startTime; // "when workout started, in minutes from midnight"
    private int endTime;   // "when workout ended, in minutes from midnight"

    Workout(int id, int startTime, int endTime) {
        this.id        = id;
        this.startTime = startTime;
        this.endTime   = endTime;
    }

    public int getId()        { return id; }
    public int getStartTime() { return startTime; }
    public int getEndTime()   { return endTime; }

    // "getDuration returns how long the workout lasted in minutes"
    // "endTime - startTime e.g. startTime=10, endTime=20 → 10 minutes"
    public int getDuration()  { return endTime - startTime; }
}

// ── CLASS: Member ──────────────────────────────────────────────────────
// "Represents one gym member with ID, name, and membership tier"
class Member {
    public int memberId;
    public String name;
    public MembershipStatus membershipStatus;

    Member(int id, String name, MembershipStatus status) {
        this.memberId         = id;
        this.name             = name;
        this.membershipStatus = status;
    }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 1 — BUG FIX: override equals()
    //
    //  SAY: "Without overriding equals(), Java uses the default which
    //        compares memory addresses. Two different new Member(1,...)
    //        objects are at different addresses so they are NEVER equal
    //        even with identical data. The fix is comparing by memberId.
    //        I must also override hashCode with the same field because
    //        Java's contract requires: if a.equals(b) then a.hashCode()
    //        must equal b.hashCode()."
    // ════════════════════════════════════════════════════════════════════

    // "@Override tells Java I am replacing the equals() from Object class"
    @Override
    public boolean equals(Object o) {
        // "if both references point to the exact same memory address → equal"
        if (this == o) return true;

        // "if the other object is not even a Member → cannot be equal"
        if (!(o instanceof Member)) return false;

        // "cast the Object to Member so I can access the memberId field"
        Member m = (Member) o;

        // "THE FIX — compare by memberId which is unique per member"
        // "If IDs match, they represent the same member"
        return this.memberId == m.memberId;
    }

    // "MUST override hashCode when overriding equals — Java contract"
    // "If a.equals(b) is true, a.hashCode() must equal b.hashCode()"
    // "Without this, Members used as HashMap keys or in HashSets break"
    @Override
    public int hashCode() {
        return Integer.hashCode(memberId); // "use memberId as hash basis"
    }

    @Override
    public String toString() {
        return "Member ID: " + memberId + ", Name: " + name + ", Status: " + membershipStatus;
    }
}

// ── CLASS: MembershipStatistics ────────────────────────────────────────
// "Container to return 3 values from getMembershipStatistics() in one object"
class MembershipStatistics {
    public int totalMembers;
    public int totalPaidMembers; // "SILVER + GOLD count"
    public double conversionRate; // "(paid / total) × 100"

    MembershipStatistics(int t, int p, double r) {
        totalMembers = t; totalPaidMembers = p; conversionRate = r;
    }
}

// ── CLASS: Membership ──────────────────────────────────────────────────
// "Main class — manages all members and their workouts"
class Membership {

    // "List of all Member objects in the gym"
    public List<Member> members = new ArrayList<>();

    // "Map grouping workouts by member ID"
    // "Key = memberId, Value = list of all Workout objects for that member"
    Map<Integer, List<Workout>> workoutsByMember = new HashMap<>();

    // "addMember appends a member to the list"
    void addMember(Member m) { members.add(m); }

    // "updateMembership finds the member by ID and changes their tier"
    void updateMembership(int memberId, MembershipStatus status) {
        for (Member m : members) {
            if (m.memberId == memberId) {
                m.membershipStatus = status;
                break; // "stop looping once found"
            }
        }
    }

    // "getMembershipStatistics counts total members, paid members, conversion rate"
    MembershipStatistics getMembershipStatistics() {
        int premium = 0; // "count of SILVER + GOLD members"

        for (Member m : members) {
            // "SILVER and GOLD are paid tiers"
            // ".equals() works for enum comparison — == also works"
            if (m.membershipStatus.equals(MembershipStatus.GOLD)
             || m.membershipStatus.equals(MembershipStatus.SILVER)) {
                premium++;
            }
            // "BRONZE members fall through — premium stays unchanged"
        }

        // "Ternary: if members list is empty → return 0.0 to avoid division by zero"
        // "(double) cast: forces decimal division — 4/6=0 without cast, 4/6.0=0.666 with cast"
        // "× 100.0: converts fraction to percentage"
        double rate = members.size() == 0
            ? 0.0
            : (premium / (double) members.size()) * 100.0;

        // "Wrap all 3 values in one object and return"
        return new MembershipStatistics(members.size(), premium, rate);
    }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 2.1 — addWorkout(memId, wk)
    //
    //  SAY: "I use computeIfAbsent which does two things in one line.
    //        If the key is absent, it creates a new ArrayList, puts it
    //        in the map, and returns it. If the key exists, it returns
    //        the existing list. Then .add(wk) appends the workout.
    //        The return statement stops the loop immediately once the
    //        member is found — no point checking remaining members."
    // ════════════════════════════════════════════════════════════════════
    public void addWorkout(Integer memId, Workout wk) {

        // "Loop through members list to verify this memberId exists"
        for (Member member : members) {

            // "Check if current member's ID matches the requested memberId"
            if (member.memberId == memId) {

                // "computeIfAbsent(key, mapping):"
                // "IF key NOT in map → run lambda to create new ArrayList, put in map"
                // "IF key IS in map → return existing list"
                // "Then .add(wk) adds the workout to whichever list was returned"
                workoutsByMember
                    .computeIfAbsent(memId, k -> new ArrayList<>())
                    .add(wk);

                // "Member found and workout added — stop looping immediately"
                // "return in a void method = exit the method right now"
                return;
            }
        }
        // "If loop ends without returning → member not found → silently ignore"
        // "No exception, no error — the question says to just ignore it"
    }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 2.2 — getAverageWorkoutDurations()
    //
    //  SAY: "I loop over workoutsByMember.entrySet() — not over all members.
    //        This automatically handles members with no workouts. Member 37
    //        had no workouts added so key 37 is not in workoutsByMember,
    //        so it is not in entrySet(), so it never appears in the result."
    // ════════════════════════════════════════════════════════════════════
    public Map<Integer, Double> getAverageWorkoutDurations() {

        // "Result map — key = memberId, value = average duration in minutes"
        Map<Integer, Double> avgByMember = new HashMap<>();

        // "entrySet() gives all key-value pairs from workoutsByMember"
        // "entry.getKey() = memberId, entry.getValue() = List<Workout>"
        for (Map.Entry<Integer, List<Workout>> entry : workoutsByMember.entrySet()) {

            double totalDuration = 0.0; // "running total of minutes for this member"

            // "Inner loop: sum up all workout durations for this member"
            for (Workout workout : entry.getValue()) {
                // "getDuration() = endTime - startTime in minutes"
                totalDuration += workout.getDuration();
            }

            // "Average = total minutes ÷ number of workouts"
            // "entry.getValue().size() = number of workouts this member has"
            avgByMember.put(entry.getKey(), totalDuration / entry.getValue().size());
        }

        // "Members with no workouts are not in workoutsByMember → not in result ✓"
        return avgByMember;
    }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 3 — getPaymentDues() + getDueBySatus()
    //
    //  SAY: "The ceiling division (duration+59)/60 is the key formula.
    //        In Java, integer division truncates — 80/60 gives 1, not 2.
    //        Adding 59 forces the truncation to round up instead.
    //        The inner loop starting at freeLimit automatically skips
    //        the free workouts — no if-check needed inside the loop.
    //        And totalDue must be inside the outer loop to reset for each member."
    // ════════════════════════════════════════════════════════════════════
    public Map<Integer, Double> getPaymentDues() {

        // "Result map: key = memberId, value = total amount owed in dollars"
        Map<Integer, Double> dueByMember = new HashMap<>();

        // "Process each member separately"
        for (Member member : members) {

            // "Based on tier, call getDueBySatus with freeLimit and charge rate"
            if (member.membershipStatus.equals(MembershipStatus.BRONZE)) {
                // "BRONZE: freeLimit=1 (1st workout free), charge=$10/hr"
                dueByMember.put(member.memberId, getDueBySatus(member.memberId, 1, 10));

            } else if (member.membershipStatus.equals(MembershipStatus.SILVER)) {
                // "SILVER: freeLimit=3 (first 3 free), charge=$8/hr"
                dueByMember.put(member.memberId, getDueBySatus(member.memberId, 3, 8));

            } else {
                // "GOLD: freeLimit=5 (first 5 free), charge=$6/hr"
                // "else covers GOLD since only 3 enum values exist"
                dueByMember.put(member.memberId, getDueBySatus(member.memberId, 5, 6));
            }
        }

        return dueByMember; // "e.g. {12: 12.0, 22: 0.0, 31: 0.0}"
    }

    // "getDueBySatus calculates the actual payment for one member"
    // "memberId = which member, freeLimit = free workout count, charge = $/hr"
    public double getDueBySatus(int memberId, int freeLimit, int charge) {

        double due = 0.0; // "start at $0 owed"

        // "Get this member's workout list, or empty list if no workouts"
        // "getOrDefault prevents NullPointerException if member never added workouts"
        List<Workout> workouts = workoutsByMember.getOrDefault(memberId, new ArrayList<>());

        // "If workouts count is within free limit → nothing to pay → return 0"
        // "<= means: if GOLD member has exactly 5 workouts → 5<=5 → all free"
        if (workouts.size() <= freeLimit) {
            return due; // "returns 0.0"
        }

        else {
            int totalHours = 0;

            // "CRITICAL: loop starts at freeLimit index — SKIPS the free workouts"
            // "BRONZE freeLimit=1: i starts at 1 → index 0 (1st workout) is free"
            // "SILVER freeLimit=3: i starts at 3 → indices 0,1,2 are free"
            // "GOLD   freeLimit=5: i starts at 5 → indices 0,1,2,3,4 are free"
            for (int i = freeLimit; i < workouts.size(); i++) {

                // "Get this billable workout's duration in minutes"
                int duration = workouts.get(i).getDuration();

                // "Integer division: 80/60 = 1 (drops the remainder)"
                int hours = duration / 60;

                // "Ceiling logic: if there are leftover minutes, round UP"
                // "duration % 60 = remainder minutes"
                // "If remainder > 0 → not exact hours → hours + 1"
                // "If remainder = 0 → exact hours → keep hours as is"
                // "Examples: 80min→hours=1+1=2 ✓  60min→hours=1+0=1 ✓  10min→hours=0+1=1 ✓"
                totalHours += ((duration % 60) > 0 ? hours + 1 : hours);
            }

            // "Total payment = total billable hours × rate per hour"
            due = totalHours * charge;
        }

        return due;
    }
}

// ── MAIN: runs all 4 tests ─────────────────────────────────────────────
public class Q3_GymCollection_Commented {

    public static void main(String[] args) {
        testTask1_BugFix();
        testTask2_AddWorkoutAndAverage();
        testTask3_PaymentDues();
        System.out.println("\nAll tasks PASSED!");
    }

    static void testTask1_BugFix() {
        System.out.println("Task 1 (Bug Fix): Member.equals()...");
        // "Two DIFFERENT objects with the SAME data"
        Member m1 = new Member(1, "John Doe", MembershipStatus.BRONZE);
        Member m2 = new Member(1, "John Doe", MembershipStatus.BRONZE);
        Member m3 = new Member(2, "Jane Doe", MembershipStatus.GOLD);

        // "Without fix: m1.equals(m2) → false (different addresses)"
        // "With fix: m1.equals(m2) → true (same memberId)"
        assert  m1.equals(m2) : "same memberId → should be equal";
        assert !m1.equals(m3) : "different memberId → should NOT be equal";

        List<Member> list = new ArrayList<>();
        list.add(m1);
        // "This is the line from testMembership() that was failing before fix"
        assert list.get(0).equals(m2) : "list.get(0).equals(testMember) should be true";

        System.out.println("  m1.equals(m2)=" + m1.equals(m2) + "  m1.equals(m3)=" + m1.equals(m3) + "  PASS");
    }

    static void testTask2_AddWorkoutAndAverage() {
        System.out.println("Task 2: addWorkout + getAverageWorkoutDurations...");
        Membership gym = new Membership();
        gym.addMember(new Member(12, "John",   MembershipStatus.SILVER));
        gym.addMember(new Member(22, "Alex",   MembershipStatus.BRONZE));
        gym.addMember(new Member(31, "Marie",  MembershipStatus.GOLD));
        gym.addMember(new Member(37, "George", MembershipStatus.SILVER)); // no workouts

        // "Member 12: durations 10+55+10=75 / 3 = avg 25.0"
        gym.addWorkout(12, new Workout(11, 10, 20));     // 10 min
        gym.addWorkout(12, new Workout(47, 100, 155));   // 55 min
        gym.addWorkout(12, new Workout(78, 1000, 1010)); // 10 min

        // "Member 22: durations 20+80=100 / 2 = avg 50.0"
        gym.addWorkout(22, new Workout(24, 15, 35));    // 20 min
        gym.addWorkout(22, new Workout(56, 120, 200));  // 80 min

        // "Member 31: durations 45+100=145 / 2 = avg 72.5"
        gym.addWorkout(31, new Workout(32, 45, 90));    // 45 min
        gym.addWorkout(31, new Workout(62, 300, 400));  // 100 min

        // "memberId=4 doesn't exist → silently ignored"
        gym.addWorkout(4, new Workout(99, 0, 60));

        Map<Integer, Double> avg = gym.getAverageWorkoutDurations();

        assert Math.abs(avg.get(12) - 25.0) < 0.1 : "M12 expected 25.0";
        assert Math.abs(avg.get(22) - 50.0) < 0.1 : "M22 expected 50.0";
        assert Math.abs(avg.get(31) - 72.5) < 0.1 : "M31 expected 72.5";
        assert !avg.containsKey(37) : "M37 should NOT appear (no workouts)";

        System.out.println("  M12=" + avg.get(12) + "  M22=" + avg.get(22)
            + "  M31=" + avg.get(31) + "  M37 absent=" + !avg.containsKey(37) + "  PASS");
    }

    static void testTask3_PaymentDues() {
        System.out.println("Task 3: getPaymentDues...");
        Membership gym = new Membership();

        // "BRONZE Bob: freeLimit=1, $10/hr"
        // "w1=free, w2=80min→2hrs=$20, w3=30min→1hr=$10 → total $30"
        gym.addMember(new Member(1, "Bronze Bob", MembershipStatus.BRONZE));
        gym.addWorkout(1, new Workout(1, 0, 60));  // free
        gym.addWorkout(1, new Workout(2, 0, 80));  // 80min→(80%60>0)→hours+1=2hrs→$20
        gym.addWorkout(1, new Workout(3, 0, 30));  // 30min→(30%60>0)→hours+1=1hr →$10

        // "SILVER Sue: freeLimit=3, $8/hr"
        // "w1,w2,w3=free, w4=120min→2hrs=$16, w5=61min→2hrs=$16 → total $32"
        gym.addMember(new Member(2, "Silver Sue", MembershipStatus.SILVER));
        gym.addWorkout(2, new Workout(1, 0, 60));
        gym.addWorkout(2, new Workout(2, 0, 60));
        gym.addWorkout(2, new Workout(3, 0, 60));
        gym.addWorkout(2, new Workout(4, 0, 120)); // 120min→exact 2hrs→$16
        gym.addWorkout(2, new Workout(5, 0, 61));  // 61min→rounds up→2hrs→$16

        // "GOLD Gary: freeLimit=5, $6/hr"
        // "5 free + w6=90min→2hrs=$12 → total $12"
        gym.addMember(new Member(3, "Gold Gary", MembershipStatus.GOLD));
        for (int i=1; i<=5; i++) gym.addWorkout(3, new Workout(i, 0, 60));
        gym.addWorkout(3, new Workout(6, 0, 90)); // 90min→rounds up→2hrs→$12

        // "GOLD Grace: only 4 workouts — all within free limit of 5 → $0"
        gym.addMember(new Member(4, "Gold Grace", MembershipStatus.GOLD));
        for (int i=1; i<=4; i++) gym.addWorkout(4, new Workout(i, 0, 60));

        Map<Integer, Double> due = gym.getPaymentDues();

        assert Math.abs(due.get(1) - 30.0) < 0.01 : "Bob expected $30";
        assert Math.abs(due.get(2) - 32.0) < 0.01 : "Sue expected $32";
        assert Math.abs(due.get(3) - 12.0) < 0.01 : "Gary expected $12";
        assert Math.abs(due.get(4) -  0.0) < 0.01 : "Grace expected $0";

        System.out.println("  Bob=$" + due.get(1) + "  Sue=$" + due.get(2)
            + "  Gary=$" + due.get(3) + "  Grace=$" + due.get(4) + "  PASS");
    }
}




--------------------------------------------------------------------------------------------------





import java.util.*;

// ╔══════════════════════════════════════════════════════════════════════╗
//  Q4 — RUN COLLECTION (OBSTACLE COURSE)
//  Paste into: https://onlinegdb.com/online_java_compiler → Run
//
//  TASKS:
//  Task 1 (Bug Fix) : personalBest() — no filter on complete flag
//  Task 2           : bestOfBests()  — reads column by column
//  Task 3           : chanceOfPersonalBest() — Monte Carlo simulation
// ╚══════════════════════════════════════════════════════════════════════╝

// ── CLASS: Course ──────────────────────────────────────────────────────
// "Stores information about one obstacle course — its name and total obstacle count"
class Course {
    String title;       // "name of the course"
    int obstacleCount;  // "total number of obstacles e.g. 4"

    Course(String title, int count) {
        this.title         = title;
        this.obstacleCount = count;
    }

    // "equals() needed so we can compare courses when adding runs"
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Course)) return false;
        Course c = (Course) o;
        // "Two courses are equal if both title AND obstacle count match"
        return c.title.equals(this.title) && c.obstacleCount == this.obstacleCount;
    }

    @Override
    public int hashCode() { return title.hashCode() * obstacleCount; }
}

// ── CLASS: Run ─────────────────────────────────────────────────────────
// "One attempt at completing the obstacle course"
class Run {
    Course course;               // "which course this run belongs to"
    boolean complete;            // "true ONLY when ALL obstacles are done"
    List<Integer> obstacleTimes; // "recorded times per obstacle completed so far"

    Run(Course c) {
        course        = c;
        complete      = false;           // "starts as incomplete"
        obstacleTimes = new ArrayList<>();
    }

    // "addObstacleTime: records the next obstacle's time in sequence"
    void addObstacleTime(int t) {
        if (complete) throw new IllegalStateException("run is already full");

        obstacleTimes.add(t); // "append this obstacle's time to the list"

        // "When we have recorded times for ALL obstacles → mark as complete"
        if (obstacleTimes.size() == course.obstacleCount) {
            complete = true;
        }
    }

    // "getRunTime: returns sum of all recorded obstacle times"
    // "If incomplete → returns the partial sum (time so far)"
    int getRunTime() {
        return obstacleTimes.stream().mapToInt(Integer::intValue).sum();
    }
}

// ── CLASS: RunCollection ───────────────────────────────────────────────
// "Manages all runs for one specific course and provides 3 query methods"
class RunCollection {
    Course course;               // "the course all these runs are for"
    List<Run> runs = new ArrayList<>(); // "all Run objects for this course"

    RunCollection(Course c) { course = c; }

    // "addRun: validates the run belongs to this course then adds it"
    void addRun(Run run) {
        if (!run.course.equals(course))
            throw new IllegalArgumentException("Run's course doesn't match");
        runs.add(run);
    }

    int getNumRuns() { return runs.size(); }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 1 — BUG FIX: personalBest()
    //
    //  SAY: "The bug is that personalBest was finding the minimum across
    //        ALL runs including incomplete ones. Run4 only did 3 of 4
    //        obstacles — its total of 13 is a partial time, not a valid
    //        result. The fix is one filter — .filter(r -> r.complete)"
    // ════════════════════════════════════════════════════════════════════

    // "OPTION 1: Stream with filter — clean and concise"
    public int personalBest() {
        return runs.stream()
            .filter(r -> r.complete)        // "THE FIX — keep only complete runs"
            .mapToInt(r -> r.getRunTime())  // "convert each run to its total time"
            .min()                          // "find the smallest (fastest) time"
            .orElse(Integer.MAX_VALUE);     // "if NO complete runs → return MAX safely"
    }

    // "OPTION 2: For loop — easier to write under interview pressure"
    public int personalBest_ForLoop() {
        int best = Integer.MAX_VALUE; // "start with worst possible value"
        for (Run run : runs) {
            // "THE FIX — skip incomplete runs"
            // "continue jumps to the next iteration immediately"
            if (!run.complete) continue;
            // "check if this complete run is faster than current best"
            if (run.getRunTime() < best) best = run.getRunTime();
        }
        return best; // "returns 17 — Run2 was the fastest complete run"
    }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 2 — bestOfBests()
    //
    //  SAY: "bestOfBests reads the data COLUMN by COLUMN instead of row
    //        by row. For each obstacle position, I look across all runs
    //        to find the fastest time at that position.
    //        The guard — run.obstacleTimes.size() > i — is the most
    //        important line. Without it, accessing index 3 on Run4 which
    //        only has 3 elements throws IndexOutOfBoundsException."
    // ════════════════════════════════════════════════════════════════════
    public int bestOfBests() {
        int total = 0; // "running sum — I add the best time per obstacle"

        // "OUTER LOOP: goes through each OBSTACLE POSITION (reading column by column)"
        // "i=0 = obstacle 1, i=1 = obstacle 2, etc."
        for (int i = 0; i < course.obstacleCount; i++) {

            int obstacleMin = Integer.MAX_VALUE; // "best time for this obstacle so far"

            // "INNER LOOP: check every run's time at obstacle position i"
            // "(reading down the column)"
            for (Run run : runs) {

                // "THE GUARD — most important line in this method"
                // "Only access index i if this run has data there"
                // "Run4 has 3 elements. When i=3, the check 3>3 is FALSE → skipped"
                // "Without this guard → IndexOutOfBoundsException crash on Run4"
                if (run.obstacleTimes.size() > i) {

                    // "Math.min returns the smaller of two values"
                    obstacleMin = Math.min(obstacleMin, run.obstacleTimes.get(i));
                }
            }

            total += obstacleMin; // "add the best time for this obstacle"
        }

        // "Return sum of best times per obstacle"
        // "e.g. O1=3 + O2=4 + O3=3 + O4=5 = 15"
        return total;
    }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 3 — chanceOfPersonalBest(inProgress)
    //
    //  SAY: "This is a simulation problem. I build historical obstacle
    //        times, then run 10000 imaginary completions. Each completion
    //        randomly picks a time for each remaining obstacle from
    //        historical data. I count how many beat personal best and
    //        divide by 10000. I start from completedObstacles — not 0 —
    //        because those obstacles are already done."
    // ════════════════════════════════════════════════════════════════════
    public double chanceOfPersonalBest(Run inProgress) {

        // "Get the target to beat — from COMPLETE runs only"
        int personalBest = personalBest();

        int totalObstacles = course.obstacleCount;

        // "STEP 1: Build historical data"
        // "historical.get(i) = all known times for obstacle i across ALL runs"
        List<List<Integer>> historical = new ArrayList<>();

        // "Create one empty inner list per obstacle position"
        for (int i = 0; i < totalObstacles; i++) {
            historical.add(new ArrayList<>());
        }

        // "Fill historical data from all existing runs — complete and incomplete"
        for (Run run : runs) {
            for (int i = 0; i < run.obstacleTimes.size(); i++) {
                // "Add this run's time at obstacle i to the historical list for i"
                historical.get(i).add(run.obstacleTimes.get(i));
            }
        }

        int currentTotal       = inProgress.getRunTime();        // "time accumulated so far"
        int completedObstacles = inProgress.obstacleTimes.size(); // "how many done"

        // "Early exit — if already over personal best → 0% chance"
        if (currentTotal > personalBest) return 0.0;

        // "STEP 2: Run 10000 simulated completions"
        int trials  = 10000;
        int success = 0; // "count of trials that beat personal best"
        Random random = new Random();

        for (int t = 0; t < trials; t++) {

            int simTotal = currentTotal; // "each simulation starts from current time"

            // "Only simulate REMAINING obstacles"
            // "j starts at completedObstacles — not 0 — because those are done"
            for (int j = completedObstacles; j < totalObstacles; j++) {

                // "Pick a RANDOM historical time for obstacle j"
                // "random.nextInt(n) returns a random value from 0 to n-1"
                int randomIndex = random.nextInt(historical.get(j).size());
                simTotal += historical.get(j).get(randomIndex);

                // "Early exit per trial — already failed, stop simulating"
                if (simTotal > personalBest) break;
            }

            // "Count this trial as success if it beat personal best"
            if (simTotal <= personalBest) success++;
        }

        // "STEP 3: Probability = successful trials / total trials"
        // "(double) cast ensures decimal division e.g. 9300/10000 = 0.93"
        return success / (double) trials;
    }
}

// ── MAIN: runs all 3 tests ─────────────────────────────────────────────
public class Q4_RunCollection_Commented {

    public static void main(String[] args) {
        testTask1_BugFix();
        testTask2_BestOfBests();
        testTask3_ChanceOfPersonalBest();
        System.out.println("\nAll tasks PASSED!");
    }

    // "Helper: builds a RunCollection from a 2D int array"
    static RunCollection makeRC(Course course, int[][] data) {
        RunCollection rc = new RunCollection(course);
        for (int[] times : data) {
            Run run = new Run(course);
            for (int t : times) run.addObstacleTime(t); // "add each obstacle time"
            rc.addRun(run);
        }
        return rc;
    }

    static void testTask1_BugFix() {
        System.out.println("Task 1 (Bug Fix): personalBest...");
        //    Obstacles:  O1  O2  O3  O4
        //    Run 1:       3   4   5   6   total=18  complete
        //    Run 2:       4   4   4   5   total=17  complete ← real best
        //    Run 3:       4   5   4   6   total=19  complete
        //    Run 4:       5   5   3   —   total=13  INCOMPLETE ← buggy winner
        Course c = new Course("Test", 4);
        RunCollection rc = makeRC(c, new int[][]{
            {3,4,5,6}, {4,4,4,5}, {4,5,4,6}, {5,5,3}
        });

        // "Both versions should return 17, not 13"
        assert rc.personalBest()         == 17 : "stream expected 17";
        assert rc.personalBest_ForLoop() == 17 : "forLoop expected 17";

        System.out.println("  stream=" + rc.personalBest()
            + "  forLoop=" + rc.personalBest_ForLoop() + "  (not 13)  PASS");
    }

    static void testTask2_BestOfBests() {
        System.out.println("Task 2: bestOfBests...");
        // "O1=min(3,4,4,5)=3, O2=min(4,4,5,5)=4, O3=min(5,4,4,3)=3, O4=min(6,5,6)=5 → 15"
        // "Run4 contributes O3=3 even though it is incomplete!"
        Course c = new Course("Test", 4);
        RunCollection rc = makeRC(c, new int[][]{
            {3,4,5,6}, {4,4,4,5}, {4,5,4,6}, {5,5,3}
        });

        assert rc.bestOfBests() == 15 : "expected 15, got " + rc.bestOfBests();
        System.out.println("  bestOfBests=" + rc.bestOfBests() + "  (3+4+3+5=15)  PASS");
    }

    static void testTask3_ChanceOfPersonalBest() {
        System.out.println("Task 3: chanceOfPersonalBest (Monte Carlo)...");
        Course c = new Course("Test", 10);
        int[][] data = {
            {32,37},{31,29,34,25,25,39},{25,34,38,24,26,39,33},
            {39,21,39,34,39,29,31,22,28,20},{23,22,35,33,36,21,29,37,24,34},
            {28,34,28,22,40,28,31,33,25,20},{20,38,40,28,34,22},
            {36,39,20,32,38,24,22},{40,20,21,37,32,30,40,25,37,30},
            {21,35,30,37,32,40,26,29,29}
        };
        RunCollection rc = makeRC(c, data);

        // "In-progress: first 3 obstacles done at 19 seconds each"
        Run inProgress = new Run(c);
        inProgress.addObstacleTime(19);
        inProgress.addObstacleTime(19);
        inProgress.addObstacleTime(19);

        double chance = rc.chanceOfPersonalBest(inProgress);

        // "Monte Carlo has slight randomness — use range check not exact value"
        assert chance >= 0.85 && chance <= 1.0 : "expected 0.85-1.0, got " + chance;
        System.out.println("  chanceOfPersonalBest=" + String.format("%.4f", chance) + "  (~0.93)  PASS");
    }
}














--------------------------------------------------------------------------------------------------------------





import java.util.*;

// ╔══════════════════════════════════════════════════════════════════════╗
//  Q5 — STOCK COLLECTION
//  Paste into: https://onlinegdb.com/online_java_compiler → Run
//
//  TASKS:
//  Task 1 (Bug Fix) : getMaxPrice() / getMinPrice() / getAvgPrice()
//                     crash on empty list
//  Task 2           : getBiggestChange()  — sort dates as strings
// ╚══════════════════════════════════════════════════════════════════════╝

// ── CLASS: Stock ───────────────────────────────────────────────────────
// "Identifies one company's stock by symbol and name"
class Stock {
    String symbol; // "ticker symbol e.g. 'AAPL'"
    String name;   // "full company name e.g. 'Apple Inc.'"

    Stock(String sym, String name) { this.symbol = sym; this.name = name; }

    // "equals() needed so addPriceRecord can verify the stock matches"
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Stock)) return false;
        Stock s = (Stock) o;
        // "Two stocks are equal if BOTH symbol AND name match"
        return symbol.equals(s.symbol) && name.equals(s.name);
    }

    // "Objects.hash combines multiple fields into one hash code"
    @Override
    public int hashCode() { return Objects.hash(symbol, name); }
}

// ── CLASS: PriceRecord ─────────────────────────────────────────────────
// "One price data point — which stock, what price, on what date"
// "Date format is 'YYYY-MM-DD' — alphabetical sort = chronological sort"
class PriceRecord {
    Stock stock;   // "which stock this price belongs to"
    int price;     // "stock price as integer e.g. 110"
    String date;   // "date string 'YYYY-MM-DD' — IMPORTANT: sorts alphabetically = chronologically"

    PriceRecord(Stock s, int p, String d) { stock = s; price = p; date = d; }
}

// ── CLASS: StockCollection ─────────────────────────────────────────────
// "Manages all price records for ONE specific stock"
class StockCollection {

    // "List of all price records — added in any order"
    List<PriceRecord> priceRecords = new ArrayList<>();
    Stock stock; // "the stock this collection is for"

    StockCollection(Stock s) { stock = s; }

    // "addPriceRecord validates the record belongs to this stock"
    void addPriceRecord(PriceRecord pr) {
        if (!pr.stock.equals(stock)) throw new IllegalArgumentException("Stock mismatch");
        priceRecords.add(pr);
    }

    int getNumPriceRecords() { return priceRecords.size(); }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 1A — BUG FIX: getMaxPrice()
    //
    //  SAY: "The bug is that .max().getAsInt() is called on the stream.
    //        When the list is empty, .max() returns OptionalInt.empty().
    //        Calling .getAsInt() on an empty OptionalInt throws
    //        NoSuchElementException. The fix is checking isEmpty() first
    //        and returning -1 as the sentinel value."
    // ════════════════════════════════════════════════════════════════════
    int getMaxPrice() {
        // "THE FIX — guard against empty list"
        // "Return -1 as sentinel value meaning 'no data'"
        if (priceRecords.isEmpty()) return -1;

        // "Stream: mapToInt converts each PriceRecord to its price (int)"
        // ".max() returns OptionalInt — safe to call getAsInt() now since list not empty"
        return priceRecords.stream().mapToInt(r -> r.price).max().getAsInt();
    }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 1B — BUG FIX: getMinPrice()
    //  "Same bug and same fix — just using .min() instead of .max()"
    // ════════════════════════════════════════════════════════════════════
    int getMinPrice() {
        // "THE FIX — same guard as getMaxPrice"
        if (priceRecords.isEmpty()) return -1;
        return priceRecords.stream().mapToInt(r -> r.price).min().getAsInt();
    }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 1C — BUG FIX: getAvgPrice()
    //
    //  SAY: "getAvgPrice divides by priceRecords.size(). When empty,
    //        size=0, so it divides by zero — ArithmeticException crash.
    //        The fix is the same pattern — check isEmpty first."
    // ════════════════════════════════════════════════════════════════════
    double getAvgPrice() {
        // "THE FIX — guard against division by zero"
        if (priceRecords.isEmpty()) return -1.0; // "-1.0 as double sentinel"

        // "Sum all prices then divide by count"
        double total = priceRecords.stream().mapToInt(r -> r.price).sum();
        return total / priceRecords.size(); // "safe because we know size > 0"
    }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 2 — getBiggestChange()
    //
    //  SAY: "The key insight is that YYYY-MM-DD date strings sort correctly
    //        with plain alphabetical comparison — no date parsing needed.
    //        I initialise biggestChange with the first pair to always have
    //        a valid starting value. I compare using Math.abs because the
    //        question asks for the biggest absolute change — a drop of 30
    //        is a bigger change than a rise of 20."
    // ════════════════════════════════════════════════════════════════════
    Object[] getBiggestChange() {

        // "Need at least 2 records to have any consecutive pair to compare"
        if (priceRecords.size() < 2) return null;

        // "Create a SORTED COPY — I do not want to modify the original list"
        List<PriceRecord> sorted = new ArrayList<>(priceRecords);

        // "Sort by date string — KEY INSIGHT: YYYY-MM-DD format sorts alphabetically = chronologically"
        // "Lambda: (r1, r2) -> r1.date.compareTo(r2.date) compares two records by their date strings"
        sorted.sort((r1, r2) -> r1.date.compareTo(r2.date));

        // "Initialise biggestChange with the FIRST consecutive pair (index 0 and 1)"
        // "This ensures I always have a valid starting value before the loop"
        int biggestChange  = sorted.get(1).price - sorted.get(0).price;
        String earlierDate = sorted.get(0).date; // "earlier of the two days"
        String laterDate   = sorted.get(1).date; // "later of the two days"

        // "Check all remaining consecutive pairs: (1,2), (2,3)..."
        // "Start at i=1 because I already initialised with pair (0,1)"
        for (int i = 1; i < sorted.size() - 1; i++) {

            PriceRecord curr = sorted.get(i);     // "earlier date in this pair"
            PriceRecord next = sorted.get(i + 1); // "later date in this pair"

            // "price change between these two consecutive days"
            // "positive = price went up, negative = price went down"
            int change = next.price - curr.price;

            // "Compare ABSOLUTE values — we want the biggest MAGNITUDE of change"
            // "Math.abs(-30) = 30 which is bigger than Math.abs(+20) = 20"
            if (Math.abs(change) > Math.abs(biggestChange)) {
                biggestChange = change;     // "update — keep signed value (+/-)"
                earlierDate   = curr.date;  // "update to this pair's earlier date"
                laterDate     = next.date;  // "update to this pair's later date"
            }
        }

        // "Return as Object[] so I can mix int and String in the same array"
        // "result[0] = change (int), result[1] = earlierDate (String), result[2] = laterDate (String)"
        return new Object[]{biggestChange, earlierDate, laterDate};
    }
}

// ── MAIN: runs all tests ───────────────────────────────────────────────
public class Q5_StockCollection {

    public static void main(String[] args) {
        testTask1_BugFix();
        testTask2_BiggestChange();
        System.out.println("\nAll tasks PASSED!");
    }

    static StockCollection make(Stock stock, Object[][] data) {
        StockCollection sc = new StockCollection(stock);
        for (Object[] row : data)
            sc.addPriceRecord(new PriceRecord(stock, (int) row[0], (String) row[1]));
        return sc;
    }

    static void testTask1_BugFix() {
        System.out.println("Task 1 (Bug Fix): empty list does not crash...");
        Stock s = new Stock("AAPL", "Apple Inc.");

        // "EMPTY — all three should return -1 without crashing"
        StockCollection empty = new StockCollection(s);
        assert empty.getMaxPrice() == -1                       : "empty max expected -1";
        assert empty.getMinPrice() == -1                       : "empty min expected -1";
        assert Math.abs(empty.getAvgPrice() - (-1.0)) < 0.001 : "empty avg expected -1.0";
        System.out.println("  EMPTY → max=-1 min=-1 avg=-1.0 (no crashes)");

        // "Non-empty: prices 110, 112, 90, 105 → max=112 min=90 avg=104.25"
        StockCollection sc = make(s, new Object[][]{
            {110,"2023-06-29"},{112,"2023-07-01"},{90,"2023-06-28"},{105,"2023-07-06"}
        });
        assert sc.getMaxPrice() == 112                     : "max expected 112";
        assert sc.getMinPrice() == 90                      : "min expected 90";
        assert Math.abs(sc.getAvgPrice() - 104.25) < 0.01 : "avg expected 104.25";
        System.out.println("  max=" + sc.getMaxPrice() + "  min=" + sc.getMinPrice() + "  avg=" + sc.getAvgPrice() + "  PASS");
    }

    static void testTask2_BiggestChange() {
        System.out.println("Task 2: getBiggestChange...");
        Stock s = new Stock("AAPL", "Apple Inc.");

        // "null when fewer than 2 records"
        assert new StockCollection(s).getBiggestChange() == null : "empty → null";

        // "After sort: 90(06-25)→110(06-29)→112(07-01)→105(07-06)"
        // "Changes: +20, +2, -7 → biggest absolute = +20"
        StockCollection sc1 = make(s, new Object[][]{
            {110,"2023-06-29"},{112,"2023-07-01"},{90,"2023-06-25"},{105,"2023-07-06"}
        });
        Object[] r1 = sc1.getBiggestChange();
        assert (int)r1[0] == 20           : "change expected 20";
        assert r1[1].equals("2023-06-25") : "earlier expected 2023-06-25";
        assert r1[2].equals("2023-06-29") : "later expected 2023-06-29";
        System.out.println("  Test1: " + Arrays.toString(r1) + "  PASS");

        // "After sort: 210(1999-12-30)→180(2000-01-01)→190(2000-01-03)→200(2000-01-04)"
        // "Changes: -30, +10, +10 → biggest absolute = -30"
        StockCollection sc2 = make(s, new Object[][]{
            {200,"2000-01-04"},{210,"1999-12-30"},{190,"2000-01-03"},{180,"2000-01-01"}
        });
        Object[] r2 = sc2.getBiggestChange();
        assert (int)r2[0] == -30          : "change expected -30";
        assert r2[1].equals("1999-12-30") : "earlier expected 1999-12-30";
        assert r2[2].equals("2000-01-01") : "later expected 2000-01-01";
        System.out.println("  Test2: " + Arrays.toString(r2) + "  PASS");
    }
}







-----------------------------------------------------------------------------------------------------------




import java.util.*;

// ╔══════════════════════════════════════════════════════════════════════╗
//  Q6 — TOLL BOOTH LOG ANALYSIS
//  Paste into: https://onlinegdb.com/online_java_compiler → Run
//
//  LOG FORMAT: "34400.409 SXY288 210E ENTRY"
//               timestamp  plate  loc+dir  boothType
//
//  SPEED FORMULA: speed = (distance_km × 3600) / time_seconds
//  Booths are 10km apart → distance between any two consecutive booths = 10km
//
//  TASKS:
//  Task 1 (Bug Fix) : LogEntry.toString() — String passed to %f format
//  Task 2           : countJourneys()
//  Task 3           : catchSpeeders()
// ╚══════════════════════════════════════════════════════════════════════╝

// ── CLASS: LogEntry ────────────────────────────────────────────────────
// "Parses and stores one toll booth log line"
class LogEntry {

    private final String timestamp;    // "stored as String e.g. '34400.409'"
    private final String licensePlate; // "vehicle's plate e.g. 'KTB918'"
    private final String boothType;    // "ENTRY, EXIT, or MAINROAD"
    private final int location;        // "km from highway start e.g. 210"
    private final String direction;    // "EAST or WEST"

    // "Constructor: splits the log line on spaces to extract 4 fields"
    LogEntry(String logLine) {
        // "split on space → [timestamp, plate, location+direction, boothType]"
        String[] tokens = logLine.split(" ");

        // "tokens[0] = '44776.619' → store as String (not double!)"
        this.timestamp    = tokens[0];

        // "tokens[1] = 'KTB918' → license plate"
        this.licensePlate = tokens[1];

        // "tokens[3] = 'MAINROAD' → booth type"
        this.boothType    = tokens[3];

        // "tokens[2] = '310E' → location is '310', direction is 'E'"
        // "substring(0, len-1) removes the last character (E or W)"
        // "parseInt converts '310' string to int 310"
        this.location = Integer.parseInt(
            tokens[2].substring(0, tokens[2].length() - 1)
        );

        // "Last character of '310E' is 'E' → EAST, otherwise WEST"
        this.direction = tokens[2].endsWith("E") ? "EAST" : "WEST";
    }

    // "THE KEY GETTER — parses the stored String and returns a double"
    // "Float.parseFloat('34400.409') → 34400.409 as a float/double"
    // "This is what %f in toString() needs — a numeric type, not a String"
    public double getTimestamp()    { return Float.parseFloat(timestamp); }

    public String getLicensePlate() { return licensePlate; }
    public String getBoothType()    { return boothType; }
    public int getLocation()        { return location; }
    public String getDirection()    { return direction; }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 1 — BUG FIX: toString()
    //
    //  SAY: "The bug is that the private field 'timestamp' is stored as
    //        a String like '34400.409' but %f in the format string expects
    //        a floating point number — a double or float. Passing a String
    //        to %f causes MissingFormatArgumentException at runtime.
    //        The fix is to call getTimestamp() which parses the String
    //        to a double using Float.parseFloat — that %f can handle."
    // ════════════════════════════════════════════════════════════════════
    @Override
    public String toString() {
        return String.format(
            "<LogEntry timestamp: %f  license: %s  location: %d  direction: %s  booth: %s>",
            // "%f expects double/float — getTimestamp() returns double ✓"
            // "THE FIX: was 'timestamp' (String) → now getTimestamp() (double)"
            getTimestamp(),
            licensePlate, // "%s = String format — correct"
            location,     // "%d = int format — correct"
            direction,    // "%s = String format — correct"
            boothType     // "%s = String format — correct"
        );
    }
}

// ── CLASS: LogFile ─────────────────────────────────────────────────────
// "Manages a list of LogEntry objects and provides analysis methods"
class LogFile {

    List<LogEntry> logEntries = new ArrayList<>();

    // "Constructor accepts string array instead of a file — no file I/O needed for testing"
    LogFile(String[] lines) {
        for (String line : lines)
            if (!line.trim().isEmpty()) // "skip blank lines"
                logEntries.add(new LogEntry(line.trim()));
    }

    LogEntry get(int i) { return logEntries.get(i); }
    int size()          { return logEntries.size(); }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 2 — countJourneys()
    //
    //  SAY: "The insight is that every complete journey ends at exactly
    //        one EXIT booth. So instead of tracking ENTRY to EXIT pairs,
    //        I simply count EXIT entries in the log."
    // ════════════════════════════════════════════════════════════════════
    public int countJourneys() {
        int count = 0; // "initialise counter"

        // "Loop through every log entry"
        for (LogEntry e : logEntries) {

            // "Every complete journey ends at exactly ONE EXIT booth"
            // "So counting EXIT entries = counting complete journeys"
            // ".equals() for String comparison — not =="
            if (e.getBoothType().equals("EXIT")) {
                count++; // "this EXIT marks the end of one complete journey"
            }
        }

        return count; // "total number of complete journeys"
    }

    // ════════════════════════════════════════════════════════════════════
    //  TASK 3 — catchSpeeders()
    //
    //  SAY: "I need 4 tracking maps — one per piece of state I need to
    //        remember between booths for each plate. I reset all 4 for
    //        a plate at ENTRY because a new journey starts clean.
    //        The speed formula multiplies by 3600 to convert from
    //        km per second to km per hour — 3600 = seconds in one hour.
    //        Math.abs is critical for distance because west-bound cars
    //        have decreasing location numbers."
    // ════════════════════════════════════════════════════════════════════
    public List<String> catchSpeeders() {

        // "Result list — one entry per speeding journey"
        // "A plate can appear multiple times if it has multiple speeding journeys"
        List<String> tickets = new ArrayList<>();

        // "4 tracking maps — one per piece of state per license plate"
        Map<String, Double>  prevTime     = new HashMap<>(); // "last timestamp"
        Map<String, Integer> prevLocation = new HashMap<>(); // "last location km"
        Map<String, Integer> over120Count = new HashMap<>(); // "segments >= 120 this journey"
        Map<String, Boolean> ticketed     = new HashMap<>(); // "already ticketed this journey?"

        // "Process each log entry in chronological order"
        for (LogEntry e : logEntries) {

            String plate = e.getLicensePlate(); // "which vehicle"
            String type  = e.getBoothType();    // "ENTRY, MAINROAD, or EXIT"

            if (type.equals("ENTRY")) {
                // "New journey starting — RESET all tracking for this plate"
                prevTime.put(plate, e.getTimestamp());   // "record entry timestamp"
                prevLocation.put(plate, e.getLocation()); // "record entry km position"
                over120Count.put(plate, 0);               // "reset soft-speeding counter"
                ticketed.put(plate, false);               // "this journey starts clean"

            } else if (prevTime.containsKey(plate)) {
                // "This is MAINROAD or EXIT and we have prior data for this plate"
                // "Calculate speed for this segment (from previous booth to here)"

                // "Time between previous booth and this one in seconds"
                double timeDiff = e.getTimestamp() - prevTime.get(plate);

                // "Distance in km — always 10km between consecutive booths"
                // "Math.abs because WEST-bound cars have DECREASING locations"
                // "Without Math.abs: 260-270 = -10 → negative speed → WRONG"
                int distDiff = Math.abs(e.getLocation() - prevLocation.get(plate));

                // "SPEED FORMULA: (distance × 3600) / time"
                // "× 3600 converts from km/second to km/hour"
                // "3600 = number of seconds in 1 hour"
                double speed = (distDiff * 3600.0) / timeDiff;

                // "Only check speed rules if not already ticketed this journey"
                if (!ticketed.get(plate)) {

                    if (speed >= 130.0) {
                        // "Rule 1: single segment >= 130 km/h → IMMEDIATE ticket"
                        ticketed.put(plate, true);

                    } else if (speed >= 120.0) {
                        // "Rule 2 candidate: between 120-130 km/h — soft speeding"
                        // "Increment the soft-speeding counter for this journey"
                        int cnt = over120Count.get(plate) + 1;
                        over120Count.put(plate, cnt);

                        // "If TWO or more such segments in same journey → Rule 2 ticket"
                        if (cnt >= 2) ticketed.put(plate, true);
                    }
                }

                // "Update tracking maps for the NEXT segment calculation"
                prevTime.put(plate, e.getTimestamp());
                prevLocation.put(plate, e.getLocation());

                // "If this is an EXIT — journey over"
                // "If they were speeding this journey → add plate to tickets list"
                if (type.equals("EXIT") && ticketed.get(plate)) {
                    tickets.add(plate); // "one entry per speeding journey"
                }
            }
            // "If MAINROAD/EXIT but plate not in prevTime → car was on highway before log started"
            // "The question says assume complete journeys — so we ignore this case"
        }

        return tickets; // "e.g. ['TST002', 'TST003']"
    }
}

// ── MAIN: runs all tests ───────────────────────────────────────────────
public class Q6_TollBooth_Commented {

    public static void main(String[] args) {
        testTask1_BugFix();
        testTask2_CountJourneys();
        testTask3_CatchSpeeders();
        System.out.println("\nAll tasks PASSED!");
    }

    static void testTask1_BugFix() {
        System.out.println("Task 1 (Bug Fix): LogEntry.toString() no crash...");

        // "Parse a sample log line"
        String logLine = "44776.619 KTB918 310E MAINROAD";
        LogEntry entry = new LogEntry(logLine);

        // "Verify all 5 fields were parsed correctly"
        assert Math.abs(entry.getTimestamp() - 44776.619f) < 0.1 : "timestamp wrong";
        assert entry.getLicensePlate().equals("KTB918")           : "plate wrong";
        assert entry.getLocation() == 310                          : "location wrong";
        assert entry.getDirection().equals("EAST")                : "direction wrong";
        assert entry.getBoothType().equals("MAINROAD")            : "boothType wrong";

        // "toString() MUST NOT throw — this was the bug before fix"
        String result = entry.toString(); // "would crash here before the fix"
        assert result.contains("44776")  : "should contain timestamp";
        assert result.contains("KTB918") : "should contain plate";

        System.out.println("  toString() = " + result);
        System.out.println("  PASS");

        // "Also test WEST direction parsing"
        LogEntry entry2 = new LogEntry("52160.132 ABC123 400W ENTRY");
        assert entry2.getLocation() == 400          : "location should be 400";
        assert entry2.getDirection().equals("WEST") : "direction should be WEST";
        System.out.println("  West direction: loc=" + entry2.getLocation() + "  dir=" + entry2.getDirection() + "  PASS");
    }

    static void testTask2_CountJourneys() {
        System.out.println("Task 2: countJourneys...");

        // "3 complete journeys: JOX304(1), THX138(2)"
        String[] lines = {
            "90750.191 JOX304 250E ENTRY",
            "91081.684 JOX304 260E MAINROAD",
            "91082.101 THX138 110E ENTRY",
            "91483.251 JOX304 270E MAINROAD",
            "91873.920 THX138 120E MAINROAD",
            "91874.493 JOX304 280E EXIT",  // journey 1 ends
            "91982.102 THX138 290E EXIT",  // journey 2 ends
            "92301.302 THX138 300E ENTRY",
            "92371.302 THX138 310E EXIT"   // journey 3 ends
        };

        LogFile lf = new LogFile(lines);
        assert lf.countJourneys() == 3 : "expected 3, got " + lf.countJourneys();
        System.out.println("  countJourneys=" + lf.countJourneys() + "  (JOX304:1 + THX138:2)  PASS");
    }

    static void testTask3_CatchSpeeders() {
        System.out.println("Task 3: catchSpeeders...");

        String[] lines = {
            // "TST001: 10km in 500sec = 72km/h → safe"
            "1000.000 TST001 100E ENTRY",
            "1500.000 TST001 110E MAINROAD", // 72 km/h
            "2000.000 TST001 120E EXIT",

            // "TST002: 10km in 275sec = (10×3600)/275 = 130.91 km/h ≥ 130 → Rule 1 TICKET"
            "1000.000 TST002 270W ENTRY",
            "1275.000 TST002 260W EXIT",

            // "TST003: two segments at 122km/h ≥ 120 → Rule 2 TICKET"
            "3000.000 TST003 200E ENTRY",
            "3295.000 TST003 210E MAINROAD", // 122 km/h → 1st flag
            "3590.000 TST003 220E EXIT",     // 122 km/h → 2nd flag → ticket
        };

        LogFile lf = new LogFile(lines);
        List<String> tickets = lf.catchSpeeders();

        System.out.println("  Tickets issued: " + tickets);

        assert  tickets.contains("TST002") : "TST002 should be ticketed (130.91 km/h)";
        assert  tickets.contains("TST003") : "TST003 should be ticketed (2x 122 km/h)";
        assert !tickets.contains("TST001") : "TST001 should NOT be ticketed (72 km/h)";

        System.out.println("  TST001 safe | TST002+TST003 ticketed  PASS");
    }
}
