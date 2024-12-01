import java.rmi.Naming;

public class RMIServer {
    private static final String HOST = "localhost";
    public static void main(String[] args) {
        try {
            Service service = new Service();
            Naming.rebind("rmi://" + HOST + "/Handler", service);
            System.out.println("RMI server started.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
