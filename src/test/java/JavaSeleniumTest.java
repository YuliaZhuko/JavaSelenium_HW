import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


public class JavaSeleniumTest {
    private WebDriver driver;
    private Logger log = (Logger) LogManager.getLogger(JavaSeleniumTest.class);
    private final Properties prop = new Properties();
    private FileInputStream fileInputStream;
    private static final String PATH_TO_CONFIG_PROPERTIES = "src/test/resources/config.properties";

    private String LOGIN, PASSWORD;
    private String name = "Валера";
    private String sName = "Филимонов";

    private String latinName = "Valera";
    private String latinSurname = "Philimonov";

    private String blogName = "Phil_Valera";

    private String dateOfBirth = "11.11.2003";

    private String country = "Россия";
    private String city = "Александров";
    private String levelOfEnglish = "Элементарный уровень (Elementary)";
    private String skypeLogin = "Haliavkino";
    private String ingeneer = "Инженер";
    private String company = "ООО Барракуда";
    public String gender = "Мужской";
    @BeforeAll
    public static void webDriverSetup(){
        WebDriverManager.chromedriver().setup();

    }
    public void setUpFullScreen(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-fullscreen");
        driver = new ChromeDriver(options);
    }
      @AfterEach
    public void setDown(){
        if (driver != null){
            driver.close();
            driver.quit();
        }
    }
    @Test
    public void otusTest() throws FileNotFoundException, InterruptedException, NoSuchElementException {
        log.debug("Тестовое сообщение");
//      Set incognito and full screen modes
        setUpFullScreen();
//      Open URL
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(" https://otus.ru");
//      Authorization and   go to the section about yourself
        auth();
//      filling in the fields of the section
        waitingForElement(By.xpath("//input[@name='country']/following-sibling::div"),By.cssSelector("button[title='Россия']"));
        clearAndEnter(By.name("fname"),name);
        clearAndEnter(By.name("lname"),sName);
        clearAndEnter(By.name("fname_latin"),latinName);
        clearAndEnter(By.name("lname_latin"),latinSurname);
        clearAndEnter(By.name("blog_name"),blogName);

        clearAndEnter(By.cssSelector("input[name='date_of_birth']"),dateOfBirth);
        waitingForElement(By.xpath("//input[@name='english_level']/following-sibling::div"),By.cssSelector("button[title='Элементарный уровень (Elementary)']"));
        waitingForElement(By.xpath("//input[@name='city']/following-sibling::div"),By.cssSelector("button[title='Александров']"));

        WebElement readyToMove = driver.findElement(By.xpath("//span[contains(text(),'Да')]"));
        readyToMove.click();

        WebElement flexible = (driver.findElement(By.cssSelector("input[value='flexible']")));
        WebElement remote = (driver.findElement(By.cssSelector("input[value='remote']")));
        WebElement emailPreferable = (driver.findElement(By.cssSelector("input[name='is_email_preferable']")));
      //  WebElement remote = (driver.findElement(By.xpath("//input[@value='remote']/following-sibling::span")));

        System.out.println("Состояние гибкого графика было " + flexible.isSelected());
        if (flexible.isSelected()) {
            System.out.println("Гибкий график: " + flexible.isSelected());
        }
        else  {
        flexible.click();
            System.out.println("Нажимаю Гибкий график, теперь он нажат " + flexible.isSelected() );
        }
        System.out.println("Состояние удаленной работы было " + remote.isSelected());
        if (remote.isSelected()) {
            System.out.println("удаленная работа: " + remote.isSelected());
        }
        else  {
         driver.findElement(By.xpath("//input[@value='remote']/following-sibling::span"));
            System.out.println("Удаленная работа, теперь  нажата " + remote.isSelected() );
        }
        System.out.println("Состояние предпочтительно email было " + emailPreferable.isSelected());
        if (emailPreferable.isSelected()) {
            System.out.println("Предпочтительно email выбран: " + emailPreferable.isSelected());
        }
        else  {
           driver.findElement(By.xpath("//input[@name='is_email_preferable']/following-sibling::span")).click();
            System.out.println("Нажимаю предпочтит способ связи email " + emailPreferable.isSelected() );
        }

        //Отжать  кнопку Skype
        driver.findElements(By.cssSelector("label>div")).get(3).click();
       waitingForElement(By.cssSelector("button[data-empty='Способ связи']"),By.cssSelector("span.placeholder"));

//      Нажать кнопку Skype

       driver.findElement(By.cssSelector("button[title='Skype']")).click();

        String skype = "input[name='contact-0-value']";
        clearAndEnter(By.cssSelector(skype),skypeLogin);
        //Нажать кнопку выпадающий списк полов
        driver.findElement(By.cssSelector("select[name='gender']")).click();
        driver.findElement(By.cssSelector("option[value='m']")).click();

        //Нажать кнопку Компания, очистить и заполнить разде

        clearAndEnter(By.cssSelector("input[name='company']"),company);
        //Нажать кнопку Должность, очистить и сохранить

        clearAndEnter(By.cssSelector("input[name='work']"),ingeneer);
//        Нажать сохранить

        driver.findElement(By.cssSelector("button[name='continue']")).click();
      // Закрыть сессию драйвера
        driver.close();
        driver.quit();
//        Открыть https://otus.ru в "чистом браузере"
        setUpFullScreen();
//        Авторизоваться на сайте.  Войти в личный кабинет
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(" https://otus.ru");
        auth();
//        Проверить, что в разделе "О себе" отображаются указанные ранее данные
        Assertions.assertEquals(name, readValueAsAtribute(By.name("fname")),"Имя не совпадает!");
        Assertions.assertEquals(sName, readValueAsAtribute(By.name("lname")),"Фамилия не русском совпадает!");
        Assertions.assertEquals(latinName, readValueAsAtribute(By.name("fname_latin")),"Имя на латинском не совпадает!");
        Assertions.assertEquals(latinSurname, readValueAsAtribute(By.name("lname_latin")),"Фамилия на латинском не совпадает!");
        Assertions.assertEquals(blogName, readValueAsAtribute(By.name("blog_name")),"Имя в блоге м не совпадает!");
        Assertions.assertEquals(country, readValueAsText(By
                .xpath("//input[@name='country']/following-sibling::div")), "Страна отличается от ожидаемой");
        Assertions.assertEquals(city, readValueAsText(By
                .xpath("//input[@name='city']/following-sibling::div")), city +" отличается от ожидаемой");
        Assertions.assertEquals(levelOfEnglish, readValueAsText(By
                .xpath("//input[@name='english_level']/following-sibling::div")), levelOfEnglish +" отличается от ожидаемого");


        Assertions.assertTrue(driver.findElement(By.cssSelector("input[value='flexible']")).isSelected());
        Assertions.assertTrue(driver.findElement(By.cssSelector("input[value='remote']")).isSelected());
        Assertions.assertTrue(driver.findElement(By.cssSelector("#id_ready_to_relocate_1")).isSelected());
        Assertions.assertTrue(driver.findElement(By.cssSelector("input[name='is_email_preferable']")).isSelected());

        Assertions.assertEquals(skypeLogin, readValueAsAtribute(By.cssSelector("input[name='contact-0-value']")),"Skype логин не совпадает");
        Assertions.assertEquals(gender,readValueAsText(By.cssSelector("option[selected]")),"Пол не совпадает");
        Assertions.assertEquals(company,readValueAsAtribute(By.cssSelector("input[name='company']")), "Компания не совпадает");
        Assertions.assertEquals(ingeneer,readValueAsAtribute(By.cssSelector("input[name='work']")), "Профессия не совпадает");
        cookie();

    }

    public void cookie() {

        driver.manage().addCookie((new Cookie("Cookie1", "Otus1")));
        driver.manage().addCookie((new Cookie("Cookie2", "Otus2")));
        Cookie cookie = new Cookie("Cooki3", "Otus3");
        driver.manage().addCookie((new Cookie("Cookie4", "Otus4")));
        System.out.println(driver.manage().getCookies());
    }

    private void auth() throws FileNotFoundException {
        WebDriverWait waiter = new WebDriverWait(driver,15);
      //  WebElement buttonEnter = driver.findElement(By.cssSelector(".enxKCy"));
        waiter.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".enxKCy"))));
        driver.findElement(By.cssSelector(".enxKCy")).click();
        readProperties();
        driver.findElement(By.name("email")).sendKeys(LOGIN);
        driver.findElement(By.cssSelector("[type='password']")).sendKeys(PASSWORD);
        driver.findElement(By.xpath("//button/*[contains(text(),'Войти')]"))
                .click();
        driver.findElement(By.cssSelector(".fJMWHf")).click();
        driver.findElement(By.linkText("Мой профиль")).click();

    }

    private void clearAndEnter(By by, String text){
        driver.findElement(by).clear();
        driver.findElement(by).sendKeys(text);
    }
        public void readProperties() throws FileNotFoundException {
        try{
            fileInputStream = new FileInputStream(PATH_TO_CONFIG_PROPERTIES);
            prop.load(fileInputStream);

            LOGIN = prop.getProperty("USER_LOGIN");
            PASSWORD = prop.getProperty("USER_PASS");

        }
        catch (IOException e) {
            System.out.println("Ошибка в программе: файл " + PATH_TO_CONFIG_PROPERTIES + " не обнаружен");
            e.printStackTrace();
        }

    }
    public String readValueAsAtribute(By by){
        return driver.findElement(by).getAttribute("value");
    }

    public void waitingForElement(By firstClick, By secondClick){
        driver.findElement(firstClick).click();
        WebDriverWait wait = new WebDriverWait(driver,15);
        WebElement webElement = driver.findElement(secondClick);
        wait.until(ExpectedConditions.visibilityOf(webElement));
        driver.findElement(secondClick).click();
    }
    public String readValueAsText (By by){
        return driver.findElement(by).getText();
    }

}


