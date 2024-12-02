import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Handler extends Remote {
    public String addNews(News news) throws RemoteException;
    public List<News> getNews(String date) throws RemoteException;
    public boolean isAdmin(String pass) throws RemoteException;
    public void clearOldNews(String adminPassword) throws RemoteException;

}