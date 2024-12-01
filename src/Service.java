import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Service extends UnicastRemoteObject implements Handler{
    private List<News> newsList;
    private String password;

    public Service() throws RemoteException {
        super();
        try{
        newsList = new ArrayList<News>();
        BufferedReader br = new BufferedReader(new FileReader("./password.txt"));
        password = br.readLine();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String addNews(News news) throws RemoteException {
        newsList.add(news);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./news.txt", true))) {
            writer.newLine();
            writer.write(news.toString2());
        } catch (IOException e) {
            return "Impossible to add news: " + e.getMessage();
        }
        return "News added to the file.";
    }

    @Override
    public List<News> getNews(String date) throws RemoteException {
        try{
        newsList = NewsReader.readNewsFromFile("./news.txt", date);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        return newsList;
    }

    @Override
    public boolean isAdmin(String pass){
        return password.equals(pass);
    }
}