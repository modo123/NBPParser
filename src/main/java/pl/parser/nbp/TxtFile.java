package pl.parser.nbp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;


public class TxtFile
{
    private String currencyCode, xmlAddress;
    private List<LocalDate> datesList = new ArrayList<>();

    XmlFile xmlFile = new XmlFile();

    public TxtFile(String currencyCode)
    {
        this.currencyCode = currencyCode;
    }

    public void saveDatesToList(String date1, String date2)
    {
        LocalDate startDate = LocalDate.parse(date1);
        LocalDate endDate = LocalDate.parse(date2);
        long differenceOfDays = DAYS.between(startDate, endDate);

        for (int i = 0; i < differenceOfDays + 1; i++)
        {
            datesList.add(startDate.plusDays(i));
        }
    }

    public void parseInputData(String date1, String date2)
    {
        saveDatesToList(date1, date2);

        startDownloadingData();
    }

    public void startDownloadingData()
    {
        for (LocalDate tmp : datesList)
        {
            String basicIsoDate = tmp.format(DateTimeFormatter.BASIC_ISO_DATE);
            getDataFromTxt(basicIsoDate);
        }

        xmlFile.getDataFromXml(currencyCode);
    }

    public void getDataFromTxt(String date)
    {
        String year = date.substring(0,4);
        String urlAddress = "https://www.nbp.pl/kursy/xml/dir" + year + ".txt";
        try
        {
            URL url = new URL(urlAddress);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                String readedLine = "";
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String tmpDate = date.substring(2);

                while ((readedLine = in.readLine()) != null)
                {
                    if (readedLine.startsWith("c") && readedLine.endsWith(tmpDate))
                    {
                        xmlAddress = readedLine;
                        xmlFile.addXmlAddressToList(xmlAddress);

                        break;
                    }
                }

                in.close();
            }
            else
            {
                System.out.println("Connection failed");
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

}
