import java.util.HashMap;

interface InMemoryDB {
    int get(String key);

    void put(String key, int val);

    void begin_transaction();

    void commit();

    void rollback();

    static void main(String[] args) {

    }
}

class Database implements InMemoryDB {
    private boolean transaction = false;
    
    public HashMap<String, Integer> db = new HashMap<>();

    public HashMap<String, Integer> committedDb = new HashMap<>();

    public int get(String key) {
        try {
            return committedDb.get(key);
        }
        catch(Exception e) {
            System.out.println("Error: Null value");
        }
        return 0;
    }

    public void put(String key, int val) {
        if(transaction) {
            System.out.println("Value of " + key + " set to " + val);
            db.put(key, val);
        }
        else {
            System.out.println("No Transaction in Progress");
        }
    }

    public void begin_transaction() {
        if(!transaction) {
            System.out.println("Transaction started");
            transaction = true;
        }
    }

    public void commit() {
        committedDb = db;
        System.out.println("Changes committed");
        transaction = false;
    }

    public void rollback() {
        db = committedDb;
        System.out.println("Changes rolled back");
        transaction = false;
    }
}

class Main {
    public static void main(String[] args) {
        Database db = new Database();

    // should return null, because A doesn’t exist in the DB yet
    db.get("A");

    // should throw an error because a transaction is not in progress
    db.put("A", 5);

    // starts a new transaction
    db.begin_transaction();

    // set’s value of A to 5, but its not committed yet
    db.put("A", 5);

    // should return null, because updates to A are not committed yet
    db.get("A");

    // update A’s value to 6 within the transaction
    db.put("A", 6);

    // commits the open transaction
    db.commit();

    // should return 6, that was the last value of A to be committed
    System.out.println("Value of A is "+ db.get("A"));

    // throws an error, because there is no open transaction
    db.commit();

    // throws an error because there is no ongoing transaction
    db.rollback();

    // should return null because B does not exist in the database
    db.get("B");

    // starts a new transaction
    db.begin_transaction();

    // Set key B’s value to 10 within the transaction
    db.put("B", 10);

    // Rollback the transaction - revert any changes made to B
    db.rollback();

    // Should return null because changes to B were rolled back
    db.get("B");

    }
}