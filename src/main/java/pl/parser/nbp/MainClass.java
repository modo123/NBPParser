package pl.parser.nbp;

public class MainClass
{
    public static void main(String[] args)
    {
        //input e.g : EUR 2013-01-28 2013-01-31
        TxtFile dates = new TxtFile(args[0]);
        dates.parseInputData(args[1], args[2]);
    }

}
