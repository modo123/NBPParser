package pl.parser.nbp;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class XmlFile
{
    private List<String> xmlAddresses = new ArrayList<>();
    private List<Double> buyingRates = new ArrayList<>();
    private List<Double> sellingRates = new ArrayList<>();

    public XmlFile() {
    }

    public void addXmlAddressToList(String xmlAddress)
    {
        xmlAddresses.add(xmlAddress);
    }

    public void getDataFromXml(String currencyCode)
    {
        currencyCode = currencyCode.toUpperCase();

        for (int i = 0; i < xmlAddresses.size(); i++)
        {
            String allUrlAddress = "http://www.nbp.pl/kursy/xml/" + xmlAddresses.get(i) + ".xml";
            try
            {
                URL url = new URL(allUrlAddress);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                int responseCode = con.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK)
                {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(allUrlAddress);

                    NodeList positionsList = doc.getElementsByTagName("pozycja");

                    for (int j = 0; j < positionsList.getLength(); j++)
                    {
                        Element element = (Element) positionsList.item(j);

                        NodeList currencyCodes = element.getElementsByTagName("kod_waluty");
                        Element currencyCodeElement = (Element) currencyCodes.item(0);

                        if (getCharacterDataFromElement(currencyCodeElement).equals(currencyCode))
                        {
                            NodeList buyingRate = element.getElementsByTagName("kurs_kupna");
                            Element buyingRateElement = (Element) buyingRate.item(0);

                            NodeList sellingRate = element.getElementsByTagName("kurs_sprzedazy");
                            Element sellingRateElement = (Element) sellingRate.item(0);

                            String buyingRateValue = getCharacterDataFromElement(buyingRateElement);
                            String sellingRateValue = getCharacterDataFromElement(sellingRateElement);

                            buyingRateValue = buyingRateValue.replaceAll(",", ".");
                            sellingRateValue = sellingRateValue.replaceAll(",", ".");

                            buyingRates.add(Double.parseDouble(buyingRateValue));
                            sellingRates.add(Double.parseDouble(sellingRateValue));

                            break;
                        }
                    }
                }
                else
                {
                    System.out.println("Connection failed");
                }
            }
            catch (Exception ex)
            {

            }
        }

        System.out.println(Calculation.calculateAverageBuyingRate(buyingRates));
        System.out.println(Calculation.calculateSellingRatesStandardDeviation(sellingRates));
    }

    public static String getCharacterDataFromElement(Element e)
    {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData)
        {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        else
        {
            return "";
        }
    }
}
