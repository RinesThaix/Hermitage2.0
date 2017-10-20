package mem.kitek.server.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import mem.kitek.server.Bootstrap;

public class Connector implements Runnable {

    private final static ExecutorService executor = Executors.newCachedThreadPool();

    private static List<Connector> list = new LinkedList<>();
    private String name;
    private String user;
    private String pass;
    private String url;
    private Connection connection;
    private boolean interrupted = false;

    public static void shutdownAll() {
        for (Connector c : list) {
            c.shutdown();
            c.close();
            c.interrupt();
        }
    }

    private AtomicBoolean queryInProgress = new AtomicBoolean(false);
    private AtomicBoolean queryAddEnabled = new AtomicBoolean(true);
    private BlockingQueue<String> queryQueue = new ArrayBlockingQueue<>(1 << 10);

    @Deprecated
    public Connector(String threadName, String user, String pass, String url) {
        this.name = threadName;
        this.user = user;
        this.pass = pass;
        this.url = url;
    }

    public boolean addToQueue(String query) {
        if(this.queryAddEnabled.get())
            return this.queryQueue.offer(query);
        throw new IllegalStateException("This connector isn't accepting queries");
    }

    public boolean addToQueue(String query, Object... args) {
        return addToQueue(String.format(query, args));
    }

    public boolean insert(String table, Object... allValues) {
        return insert(false, table, allValues);
    }

    public boolean insertInstantly(String table, Object... allValues) {
        return insert(true, table, allValues);
    }

    public boolean insert(boolean instantly, String table, Object... allValues) {
        String values = Lists.newArrayList(allValues).stream()
                .map(o -> o instanceof String ? "'" + o.toString() + "'" : o.toString())
                .collect(Collectors.joining(", "));
        String query = String.format("INSERT INTO %s VALUES (%s)", table, values);
        if(instantly) {
            query(query);
            return true;
        }
        return addToQueue(query);
    }

    public void run() {
        try {
            while(!interrupted) {
                this.queryInProgress.set(true);
                String query = this.queryQueue.take();
                try(ResultSet ignored = query(query)) {} catch (SQLException ex) {
                    ex.printStackTrace();
                }
                this.queryInProgress.set(false);
            }
        } catch (InterruptedException e) {}
    }
    
    public String getName() {
        return name;
    }
    
    public void interrupt() {
        interrupted = true;
    }

    public boolean shutdown() {
        this.queryAddEnabled.set(false);
        synchronized (Connector.class) {
            while(!this.queryQueue.isEmpty())
                try {
                    Thread.sleep(500l);
                }catch (InterruptedException e) {}
        }
        return true;
    }

    public boolean initialize() {
        if(!checkConnection()) {
            Bootstrap.getLogger().info("[%s] Unable to connect to database!", getName());
            return false;
        }
        Bootstrap.getLogger().info("[%s] Successfully connected to database!", getName());
        executor.execute(this);
        return true;
    }

    private boolean checkDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return true;
        }catch (ClassNotFoundException e) {
            Bootstrap.getLogger().warn("MySQL driver class missing: " + e.getMessage() + "!");
        }
        return false;
    }

    public boolean checkConnection() {
        return open() != null;
    }

    public Connection open() {
        if(!checkDriver())
            return null;
        try {
            if(this.connection == null)
                return DriverManager.getConnection(this.url, this.user, this.pass);
            if(this.connection.isValid(0))
                return this.connection;
            return DriverManager.getConnection(this.url, this.user, this.pass);
        }catch (SQLException e) {
            Bootstrap.getLogger().warn("Could not be resolved because of an SQL Exception!", e);
        }
        return null;
    }

    public boolean close() {
        this.connection = open();
        try {
            if(this.connection != null) {
                this.connection.close();
                this.connection = null;
                return true;
            }
        }catch (Exception e) {
            Bootstrap.getLogger().warn(e, "Failed to close database connection!");
        }
        return false;
    }

    public ResultSet query(String query, Object... args) {
        return query(String.format(query, args));
    }

    public ResultSet query(String query) {
        Statement statement;
        ResultSet result = null;
        this.queryInProgress.set(true);
        try {
            this.connection = open();
            statement = this.connection.createStatement();
            if(getStatement(query).equals(Statements.SELECT)) {
                result = statement.executeQuery(query);
                return result;
            }
            if(getStatement(query).equals(Statements.INSERT)) {
                statement.executeUpdate(query, 1);
                result = statement.getGeneratedKeys();
                return result;
            }
            statement.executeUpdate(query);
            return result;
        }catch (SQLException e) {
            Bootstrap.getLogger().warn(e, "Error in SQL query: " + query + "!");
        }finally {
            this.queryInProgress.set(false);
            return result == null ? result : new SexyResultSet(result);
        }
    }

    public PreparedStatement prepare(String query) {
        Connection connection;
        PreparedStatement ps = null;
        try {
            connection = open();
            return connection.prepareStatement(query);
        }catch (SQLException e) {
            if(!e.toString().contains("not return ResultSet"))
                Bootstrap.getLogger().warn(e, "Error in SQL prepare-query: " + query + "!");
        }
        return ps;
    }

    protected Statements getStatement(String query) {
        String trimmed = query.trim();
        for(Statements sts : Statements.values())
            if(trimmed.startsWith(sts.name()))
                return sts;
        return Statements.SELECT;
    }

    protected enum Statements {
        SELECT, INSERT, UPDATE, DELETE, DO, REPLACE, LOAD, HANDLER, CALL, CREATE, ALTER, DROP, TRUNCATE, RENAME
    }
}
