import java.io.IOException;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.regex.Matcher;

import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Parser {
    static DBEngine db = null;

    public static void main(String[] args) {
        db = DBEngine.getInstance(args[0], args[1], args[2]);
        long start = 99000L;
        long end = 99999L;
        String url = "";
        for (long i = start; i <= end; i++) {
            url = "http://www.lenta.com/show/product/" + zeroLeftPad(5, i) + "/";
            System.out.println(url);
            Item ad = parseItem(url);
            if (ad != null) {
                addToDB(ad);
            } else {
                System.out.println("FAILED: " + url);
            }
        }
    }

    private static String zeroLeftPad(int digitNumber, long value) {
        StringBuffer sb = new StringBuffer();
        if (value < 0) {
            sb.append("-");
        }
        String valueString = String.valueOf(Math.abs(value));
        while (sb.length() < digitNumber - valueString.length()) {
            sb.append("0");
        }
        return sb.append(valueString).toString();
    }

    public static Item parseItem(String url) {
        Item item = null;
        try {
            Document doc = Jsoup.connect(url).get();
            if (doc != null) {
                item = new Item();
                Elements el = null;
                item.setname((el = doc.getElementsByClass("goods__title")).size() > 0 ? el.text() : null);
                if (el.size() == 0) return null;
                item.setdescription((el = doc.getElementsByAttributeValueContaining("id", "catalogItemBlock")).size() > 0 ? el.get(0).getElementsByTag("p").get(0).text() : null);
                item.setold_price((el = doc.getElementsByClass("price_block__old-price")).size() > 0 ? el.text() : null);
                item.setnew_price((el = doc.getElementsByClass("price_block__cur-price")).size() > 0 ? el.text() : null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

    private static boolean addToDB(Item item) {
        return db.processQuery("INSERT INTO \"lenta\"(\"name\",\"description\",\"old_price\",\"new_price\") VALUES ('" + item.getname() + "','" + item.getdescription() + "','" + item.getold_price() + "','" + item.getnew_price() + "')");
    }
}
