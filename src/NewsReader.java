import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsReader {
    public static List<News> readNewsFromFile(String filePath, String dateRequest) throws IOException {
        List<News> newsList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String date = line.trim();
                String title = br.readLine().trim();
                String author = br.readLine().trim();
                String content = br.readLine().trim();

                if(date.equals(dateRequest)) {
                    News news = new News(date, title, author, content);
                    newsList.add(news);
                }
                br.readLine();
            }
        }
        return newsList;
    }
}
