import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainApplication {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private static final String CHROMEDRIVER_PATH = "/Users/totsukakensuke/delta_work/chromedriver";
    private static final String SCREENSHOT_PATH = "/Users/totsukakensuke/Desktop/screenshottest/" + sdf.format(new Date())  + "/";

    public static void main(String[] args) throws IOException {
        // totsukakensuke
        String userId = args[0];
        // ファイル読みこみ
        File file = new File("/Users/" + userId + "/delta_work/sorce/java/test/target/acountInfo.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> acountInfo = new ArrayList<>();
        String data;
        while ((data = bufferedReader.readLine()) != null) {
            if(! data.contains("//")) {
                acountInfo.add(data);
            }
        }
        System.out.println(acountInfo);
        System.setProperty("webdriver.chrome.driver", CHROMEDRIVER_PATH);
        WebDriver driver = new ChromeDriver();
        final String URL = "https://travel.yahoo.co.jp/";
        // traveltopへ遷移
        driver.get(URL);
        // 検索ボックス入力
        WebElement searchboxElement = driver.findElement(By.id("qs_searchbox_keyword"));
        searchboxElement.sendKeys("a");

        // Waitオブジェクト作成
        // 最大10分間待機
        Wait<WebDriver> wait = new WebDriverWait(driver, 120);

        // テキストボックスに4文字入るまでは待機
        ExpectedCondition<Boolean> waitCondition= new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                // 数字４文字がテキストエリアに入った場合先に進む
                // return driver.findElement(By.id("qs_searchbox_keyword")).getAttribute("value").length() == 4;
                return driver.findElement(By.id("qs_searchbox_keyword")).getAttribute("value").matches("\\d{4}");
            }
        };
        wait.until(waitCondition);

        // 検索ボタンのclick
        WebElement buttonElement = driver.findElement(By.id("qs_dl_submit"));
        buttonElement.click();

        // screen shotの保存
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String path = scrFile.toString();
        FileUtils.copyFile(scrFile, new File(SCREENSHOT_PATH + path.substring(path.lastIndexOf("/") + 1)));
    }
}