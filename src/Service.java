import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
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

    @Override
    public void clearOldNews(String adminPassword) throws RemoteException {
        try {
            LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
            List<News> updatedNewsList = new ArrayList<>();

            newsList = NewsReader.readAllNewsFromFile("./news.txt");

            for (News news : newsList) {
                LocalDate newsDate = LocalDate.parse(news.getDate(), News.DATE_FORMATTER);
                if (!newsDate.isBefore(oneMonthAgo)) {
                    updatedNewsList.add(news);
                }
            }

            newsList = updatedNewsList;
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./news.txt"))) {
            for (int i = 0; i < newsList.size(); i++) {
                writer.write(newsList.get(i).toString2());
                if (i < newsList.size() - 1) {
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new RemoteException("Failed to update news file: " + e.getMessage());
        }
    }

}