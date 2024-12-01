import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.time.LocalDate;
import java.util.List;


public class Client {
    private static final String HOST = "localhost";
    private static final String SERVICE_PATH = "rmi://" + HOST + "/Handler";
    private Handler handler;
    private BufferedReader reader;
    private List<News> newsList;

    public Client() {
        try {
            handler = (Handler) Naming.lookup(SERVICE_PATH);
            reader = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                System.out.println("Enter command:\n1. add_news\n2. quit\n3. <date (dd.mm.yyyy)>");
                String command = reader.readLine();

                if (command.equals("add_news")) {
                    addNews();
                } else if (command.equals("quit")) {
                    break;
                } else {
                    getNews(command);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addNews() throws Exception {
        System.out.println("Enter admin password:");
        String pass = reader.readLine();
        if (handler.isAdmin(pass)) {
            System.out.println("Enter title:");
            String title = reader.readLine();
            System.out.println("Enter author:");
            String author = reader.readLine();
            System.out.println("Enter content:");
            String content = "";
            String temp;
            while (!(temp = reader.readLine()).equals("end")) {
                content += temp + " ";
                System.out.println(content);
            }

            LocalDate today = LocalDate.now();

            int year = today.getYear();
            int month = today.getMonthValue();
            int day = today.getDayOfMonth();
            String dayStr;
            if(day<10){
                dayStr = "0" + day;
            } else {
                dayStr = "" + day;
            }
            String date = dayStr + "." + month + "." + year;
            News news = new News(date, title, author, content);
            System.out.println(handler.addNews(news));
        } else {
            System.out.println("Invalid password.");
        }
    }

    private void getNews(String date) throws Exception {
        newsList = handler.getNews(date);
        if (!newsList.isEmpty()) {
            for (News news : newsList) {
                System.out.println(news.toString());
            }
        } else {
            System.out.printf("No news found for date:%s\n", date);
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}